package com.example.ukrainianholes.feature.add_hole.add

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.MutableLiveData
import com.example.ukrainianholes.architecture.base.*
import com.example.ukrainianholes.data.remote.entity.AccidentRate.LOW
import com.example.ukrainianholes.data.remote.entity.AddHoleRequest
import com.example.ukrainianholes.feature.add_hole.map.MapInteractor
import com.example.ukrainianholes.util.FileUtil
import com.example.ukrainianholes.util.ImageUtil
import com.example.ukrainianholes.util.notifyObserver
import com.example.ukrainianholes.util.saveToFile
import com.markodevcic.peko.Peko
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import java.io.File

class AddHoleViewModel(
    private val context: Context,
    private val interactor: AddHoleInteractor,
    private val mapInteractor: MapInteractor
) : BaseViewModel() {

    val takePhotoFromGalleryIntent = MutableLiveData<Result<Intent>>()
    val takePhotoFromCameraIntent = MutableLiveData<Result<Intent>>()
    val uploadedFileIdLiveData = MutableLiveData<Result<Long>>()

    var imageUri: Uri? = null
    private val authority = "${context.applicationContext.packageName}.provider"

    val newHoleLiveData = MutableLiveData(AddHoleRequest().apply {
        address = ""
        comment = ""
        accidentRate = LOW
    })
    val saveHoleResultLiveData = MutableLiveData<Result<Unit>>()

    fun addHole() {
        launch {
            newHoleLiveData.value?.let {
                if (it.isFilled()) {
                    saveHoleResultLiveData.value = interactor.addHole(it).mapTo { }
                } else {
                    saveHoleResultLiveData.value = ResultError()
                }
            }
        }
    }

    fun setLatLng(lat: Double, lng: Double) {
        newHoleLiveData.value?.lat = lat
        newHoleLiveData.value?.lng = lng
    }

    fun setAddress(address: String) {
        newHoleLiveData.value?.address = address
        newHoleLiveData.notifyObserver()
    }

    fun setComment(comment: String) {
        newHoleLiveData.value?.comment = comment
        newHoleLiveData.notifyObserver()
    }

    fun setAccidentRate(accidentRate: Int) {
        newHoleLiveData.value?.accidentRate = accidentRate
        newHoleLiveData.notifyObserver()
    }

    fun addPhoto(id: Long) {
        newHoleLiveData.value?.photos?.add(id)
        newHoleLiveData.notifyObserver()
    }

    fun removePhoto(id: Long) {
        newHoleLiveData.value?.photos?.remove(id)
        newHoleLiveData.notifyObserver()
    }

    fun uploadNewPhoto() {

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
            when (val result = mapInteractor.uploadFile(photoPath)) {
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
            when (val result = mapInteractor.uploadFile(photoPath)) {
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
}