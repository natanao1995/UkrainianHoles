package com.example.ukrainianholes.feature.map

import android.location.Geocoder
import com.example.ukrainianholes.architecture.base.BaseInteractor
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.architecture.base.ResultError
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.data.LatLngAddress
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapInteractor(
    private val geocoder: Geocoder
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
            ResultError<LatLngAddress>(exception = e)
        }
    }

    suspend fun getLatLngAddress(name: String): Result<LatLngAddress> = withContext(Dispatchers.IO) {
        return@withContext try {
            val address = geocoder.getFromLocationName(name, 1)?.firstOrNull()
            if (address == null) {
                ResultError<LatLngAddress>(exception = LocationNotFoundException("Локація $name не знайдена"))
            } else {
                ResultSuccess(
                    LatLngAddress(
                        LatLng(address.latitude, address.longitude),
                        address.getAddressLine(0) ?: "Невідома адреса"
                    )
                )
            }
        } catch (e: Exception) {
            ResultError<LatLngAddress>(exception = e)
        }
    }

    class LocationNotFoundException(message: String) : Exception(message)
}