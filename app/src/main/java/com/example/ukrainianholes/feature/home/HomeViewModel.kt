package com.example.ukrainianholes.feature.home

import androidx.lifecycle.MutableLiveData
import com.example.ukrainianholes.architecture.base.BaseViewModel
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.data.remote.entity.GetStatsResponse
import kotlinx.coroutines.launch

class HomeViewModel(
    private val interactor: HomeInteractor
) : BaseViewModel() {

    val statsLiveData = MutableLiveData<Result<GetStatsResponse>>()

    fun getStats() {
        launch {
            statsLiveData.value = interactor.getStats()
        }
    }
}