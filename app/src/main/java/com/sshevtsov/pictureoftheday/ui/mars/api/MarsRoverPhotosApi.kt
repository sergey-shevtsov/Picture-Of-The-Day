package com.sshevtsov.pictureoftheday.ui.mars.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsRoverPhotosApi {

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getPhotoByDate(
        @Query("api_key") apiKey: String,
        @Query("earth_date") earthDate: String
    ): Call<MRPServerResponseData>

}