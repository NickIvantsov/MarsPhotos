package com.ivantsov.marsphotos.data.api

import com.ivantsov.marsphotos.data.model.PhotoItem
import com.ivantsov.marsphotos.data.model.PhotoList
import retrofit2.http.GET

interface ApiService {

    @GET("photos")
    suspend fun getPhotos(): List<PhotoItem>
}