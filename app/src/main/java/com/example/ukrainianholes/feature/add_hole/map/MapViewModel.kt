package com.example.ukrainianholes.feature.add_hole.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.MutableLiveData
import com.example.ukrainianholes.architecture.base.BaseViewModel
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.architecture.base.ResultError
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.data.LatLngAddress
import com.example.ukrainianholes.util.FileUtil
import com.example.ukrainianholes.util.ImageUtil
import com.example.ukrainianholes.util.saveToFile
import com.google.android.gms.maps.model.LatLng
import com.markodevcic.peko.Peko
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import java.io.File

class MapViewModel(
    private val context: Context,
    private val interactor: MapInteractor
) : BaseViewModel() {

    val locationPermissionGrantedLiveData = MutableLiveData(false)
    val takePhotoFromGalleryIntent = MutableLiveData<Result<Intent>>()
    val takePhotoFromCameraIntent = MutableLiveData<Result<Intent>>()
    val latLngAddressLiveData = MutableLiveData<Result<LatLngAddress>>()
    val uploadedFileIdLiveData = MutableLiveData<Result<Long>>()

    var imageUri: Uri? = null
    private val authority = "${context.applicationContext.packageName}.provider"

    fun checkLocationPermissionGranted(activity: Activity) {
        launch {
            locationPermissionGrantedLiveData.value = try {
                val result = if (Peko.isRequestInProgress()) {
                    Peko.resumeRequest()
                } else {
                    Peko.requestPermissionsAsync(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                }

                result is PermissionResult.Granted
            } catch (e: Exception) {
                false
            }
        }
    }

    fun takePhotoFromGallery(activity: Activity) {
        launch {
            val isGranted = try {
                val result = if (Peko.isRequestInProgress()) {
                    Peko.resumeRequest()
                } else {
                    Peko.requestPermissionsAsync(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                }

                result is PermissionResult.Granted
            } catch (e: Exception) {
                false
            }

            if (isGranted) {
                val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

                takePhotoFromGalleryIntent.value = ResultSuccess(intent)
            } else {
                takePhotoFromGalleryIntent.value = ResultError(Exception())
            }
        }
    }

    fun takePhotoFromCamera(activity: Activity) {
        launch {
            val isGranted = try {
                val result = if (Peko.isRequestInProgress()) {
                    Peko.resumeRequest()
                } else {
                    Peko.requestPermissionsAsync(activity, Manifest.permission.CAMERA)
                }

                result is PermissionResult.Granted
            } catch (e: Exception) {
                false
            }

            if (isGranted) {
                val cacheDir = context.externalCacheDir
                if (cacheDir == null) {
                    takePhotoFromCameraIntent.value = ResultError(Exception())
                    return@launch
                }
                val photo = File(FileUtil.getTempPhotoPath(context))
                photo.parentFile.mkdirs()
                photo.createNewFile()
                imageUri = Uri.fromFile(photo)

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, authority, photo))

                takePhotoFromCameraIntent.value = ResultSuccess(intent)
            } else {
                takePhotoFromCameraIntent.value = ResultError(Exception())
            }
        }
    }

    fun uploadFileFromGallery(uri: Uri?) {
        launch {
            val photoPath = compressImage(uri)
            when (val result = interactor.uploadFile(photoPath)) {
                is ResultSuccess -> {
                    uploadedFileIdLiveData.value = ResultSuccess(result.data)
                }
                is ResultError -> {
                    uploadedFileIdLiveData.value = ResultError()
                }
            }
        }
    }

    fun uploadFileFromCamera() {
        launch {
            val photoPath = compressImage(imageUri)
            when (val result = interactor.uploadFile(photoPath)) {
                is ResultSuccess -> {
                    uploadedFileIdLiveData.value = ResultSuccess(result.data)
                }
                is ResultError -> {
                    uploadedFileIdLiveData.value = ResultError()
                }
            }
        }
    }

    private fun compressImage(imageUri: Uri?): String? {
        if (imageUri == null)
            return null

        val cacheDir = context.externalCacheDir ?: return null
        cacheDir.mkdirs()

        val photoFile = File(cacheDir, "compressed_photo.png")
        photoFile.delete()
        photoFile.createNewFile()

        val stream = context.contentResolver.openInputStream(imageUri) ?: return null
        val orientation = ExifInterface(stream).getAttribute(ExifInterface.TAG_ORIENTATION)

        val compressed = ImageUtil.compressImage(context, imageUri, 2048).saveToFile(photoFile, orientation)
        if (compressed) {
            return photoFile.absolutePath
        }

        return null
    }

    fun updateLatLngAddress(name: String) {
        launch {
            latLngAddressLiveData.value = interactor.getLatLngAddress(name)
        }
    }

    fun updateLatLngAddress(latLng: LatLng) {
        launch {
            latLngAddressLiveData.value = interactor.getLatLngAddress(latLng)
        }
    }
}