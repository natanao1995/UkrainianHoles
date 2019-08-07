package com.example.ukrainianholes.feature.home

import androidx.lifecycle.MutableLiveData
import com.example.ukrainianholes.architecture.base.BaseViewModel
import com.example.ukrainianholes.architecture.base.Result
import kotlinx.coroutines.launch

class HomeViewModel(
    private val interactor: HomeInteractor
) : BaseViewModel() {

    val lastWinsLiveData = MutableLiveData<Result<List<LastWin>>>()

    fun getLastWins() {
        launch {
            lastWinsLiveData.value = interactor.getLastWins()
        }
    }
}