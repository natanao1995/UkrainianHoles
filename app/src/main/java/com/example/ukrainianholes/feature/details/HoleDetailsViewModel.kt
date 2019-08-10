package com.example.ukrainianholes.feature.details

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.example.ukrainianholes.Constants
import com.example.ukrainianholes.architecture.base.BaseViewModel
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.architecture.base.ResultError
import com.example.ukrainianholes.data.remote.entity.HoleResponse
import com.example.ukrainianholes.data.remote.entity.Photo
import kotlinx.coroutines.launch

class HoleDetailsViewModel(
    private val holeDetailsInteractor: HoleDetailsInteractor
) : BaseViewModel() {
    val holeLiveData = MutableLiveData<HoleResponse>()
    val shareHoleLiveData = MutableLiveData<Intent?>()
    val likeHoleLiveData = MutableLiveData<Result<Boolean>>()

    fun getCurrentHoleLocation(): Pair<Double?, Double?> {
        val hole = holeLiveData.value
        return Pair(hole?.lat, hole?.lng)
    }

    fun shareHole() {
        val photo = holeLiveData.value?.photos?.firstOrNull()
        if (photo == null) {
            shareHoleLiveData.value = null
            return
        }
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, "Поділитись посиланням на яму")
            putExtra(Intent.EXTRA_TEXT, getFullPhotoUrl(photo))
            type = "text/plain"
        }
        shareHoleLiveData.value = Intent.createChooser(sendIntent, "Поділитись посиланням на яму")
    }

    fun getFullPhotoUrl(photo: Photo?): String {
        return "${Constants.BASE_URL}file/${photo?.id}"
    }

    fun likeHole() = launch {
        val hole = holeLiveData.value
        if (hole == null) {
            likeHoleLiveData.postValue(ResultError())
            return@launch
        }
        val result = if (hole.like) {
            holeDetailsInteractor.unsetLike(hole.id)
        } else {
            holeDetailsInteractor.setLike(hole.id)
        }
        likeHoleLiveData.postValue(result)
    }

    fun setHole(hole: HoleResponse) {
        holeLiveData.value = hole
    }
}