package com.ivantsov.marsphotos.data.api

import com.ivantsov.marsphotos.data.model.PhotoItem

interface ApiHelper {

    suspend fun getPhotos(): List<PhotoItem>

}