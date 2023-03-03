package com.ivantsov.marsphotos.data.api.impl

import com.ivantsov.marsphotos.data.api.ApiHelper
import com.ivantsov.marsphotos.data.api.ApiService
import com.ivantsov.marsphotos.data.model.PhotoItem
import com.ivantsov.marsphotos.data.model.PhotoList

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getPhotos(): List<PhotoItem> {
        return apiService.getPhotos()
    }
}