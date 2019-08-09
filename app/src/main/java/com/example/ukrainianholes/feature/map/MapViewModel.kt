package com.example.ukrainianholes.feature.map

import android.Manifest
import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.example.ukrainianholes.architecture.base.BaseViewModel
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.data.LatLngAddress
import com.google.android.gms.maps.model.LatLng
import com.markodevcic.peko.Peko
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch

class MapViewModel(
    private val interactor: MapInteractor
) : BaseViewModel() {

    val locationPermissionGrantedLiveData = MutableLiveData<Boolean>()
    val latLngAddressLiveData = MutableLiveData<Result<LatLngAddress>>()

    fun checkLocationPermissionGranted(activity: Activity) {
        launch {
            try {
                val result = if (Peko.isRequestInProgress()) {
                    Peko.resumeRequest()
                } else {
                    Peko.requestPermissionsAsync(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                }

                val isGranted = result is PermissionResult.Granted

                locationPermissionGrantedLiveData.value = isGranted
            } catch (e: Exception) {
                locationPermissionGrantedLiveData.value = false
            }
        }
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