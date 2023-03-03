package com.ivantsov.marsphotos.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivantsov.marsphotos.data.api.ApiHelper
import com.ivantsov.marsphotos.data.reprository.PhotosRepository
import com.ivantsov.marsphotos.ui.main.viewmodel.MainViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(PhotosRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}