package com.example.ukrainianholes.feature.add_hole.add

import androidx.lifecycle.MutableLiveData
import com.example.ukrainianholes.architecture.base.BaseViewModel
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.architecture.base.ResultError
import com.example.ukrainianholes.architecture.base.mapTo
import com.example.ukrainianholes.data.remote.entity.AccidentRate.LOW
import com.example.ukrainianholes.data.remote.entity.AddHoleRequest
import kotlinx.coroutines.launch

class AddHoleViewModel(
    private val interactor: AddHoleInteractor
) : BaseViewModel() {
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
    }

    fun setComment(comment: String) {
        newHoleLiveData.value?.comment = comment
    }

    fun setAccidentRate(accidentRate: Int) {
        newHoleLiveData.value?.accidentRate = accidentRate
    }

    fun addPhoto(id: Long) {
        newHoleLiveData.value?.photos?.add(id)
    }

    fun removePhoto(id: Long) {
        newHoleLiveData.value?.photos?.remove(id)
    }
}