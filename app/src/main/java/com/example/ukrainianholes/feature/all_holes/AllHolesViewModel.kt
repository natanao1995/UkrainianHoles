package com.example.ukrainianholes.feature.all_holes

import androidx.lifecycle.MutableLiveData
import com.example.ukrainianholes.architecture.base.BaseViewModel
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.data.remote.entity.Filter.MY
import com.example.ukrainianholes.data.remote.entity.Filter.NEAR
import com.example.ukrainianholes.data.remote.entity.HoleResponse
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class AllHolesViewModel(
    private val interactor: AllHolesInteractor
) : BaseViewModel() {
    val allHolesLiveData = MutableLiveData<Result<List<HoleResponse>>>()

    val selectedFilterLiveData = MutableLiveData(MY)
    private var latLng: LatLng? = null

    fun updateLatLng(latLng: LatLng) {
        this.latLng = latLng
    }

    fun updateFilter(filter: String) {
        launch {
            selectedFilterLiveData.value = filter
            getAllHoles()
        }
    }

    fun getAllHoles() {
        launch {
            selectedFilterLiveData.value?.let { filter ->
                if (filter == NEAR) {
                    latLng?.let {
                        allHolesLiveData.value = interactor.getAllHoles(filter, it.latitude, it.longitude)
                    }
                } else {
                    allHolesLiveData.value = interactor.getAllHoles(filter)
                }
            }
        }
    }
}