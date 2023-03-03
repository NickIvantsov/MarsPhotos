package com.ivantsov.marsphotos.ui.main.viewstate

import com.ivantsov.marsphotos.data.model.PhotoItem

sealed class MainState {

    object Idle : MainState()
    object Loading : MainState()
    data class Photos(val photoList: List<PhotoItem>) : MainState()
    data class Error(val error: Throwable?) : MainState()

}