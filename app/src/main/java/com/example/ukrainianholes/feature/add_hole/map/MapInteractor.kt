package com.example.ukrainianholes.feature.add_hole.map

import android.content.Context
import android.location.Geocoder
import com.example.ukrainianholes.architecture.base.*
import com.example.ukrainianholes.data.LatLngAddress
import com.example.ukrainianholes.data.remote.ApiService
import com.example.ukrainianholes.util.FileUtil
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MapInteractor(
    private val context: Context,
    private val geocoder: Geocoder,
    private val api: ApiService
) : BaseInteractor() {
    suspend fun getLatLngAddress(latLng: LatLng): Result<LatLngAddress> = withContext(Dispatchers.IO) {
        return@withContext try {
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.firstOrNull()
            ResultSuccess(
                LatLngAddress(
                    latLng,
                    address?.getAddressLine(0) ?: "Невідома адреса"
                )
            )
        } catch (e: Exception) {
            ResultError<LatLngAddress>()
        }
    }

    suspend fun getLatLngAddress(name: String): Result<LatLngAddress> = withContext(Dispatchers.IO) {
        return@withContext try {
            val address = geocoder.getFromLocationName(name, 1)?.firstOrNull()
            if (address == null) {
                ResultError<LatLngAddress>()
            } else {
                ResultSuccess(
                    LatLngAddress(
                        LatLng(address.latitude, address.longitude),
                        address.getAddressLine(0) ?: "Невідома адреса"
                    )
                )
            }
        } catch (e: Exception) {
            ResultError<LatLngAddress>()
        }
    }

    suspend fun uploadFile(photoPath: String?): Result<Long> = withContext(Dispatchers.IO) {
        if (photoPath == null) {
            return@withContext ResultError<Long>()
        }
        val type = FileUtil.getMimeType(context, photoPath) ?: return@withContext ResultError<Long>()

        val file = File(photoPath)
        if (!file.exists() || !file.isFile) {
            return@withContext ResultError<Long>()
        }

        val requestFile = RequestBody.create(MediaType.parse(type), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        processRequest {
            api.uploadFile(body)
        }.mapTo { it.id }
    }
}