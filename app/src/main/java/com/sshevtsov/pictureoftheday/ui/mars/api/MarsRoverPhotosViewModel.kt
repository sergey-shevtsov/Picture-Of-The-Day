package com.sshevtsov.pictureoftheday.ui.mars.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sshevtsov.pictureoftheday.BuildConfig
import com.sshevtsov.pictureoftheday.util.convertToNasaApiDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsRoverPhotosViewModel(
    private val liveDataToObserve: MutableLiveData<MarsRoverPhotosData> = MutableLiveData(),
    private val retrofitImpl: MRPRetrofitImpl = MRPRetrofitImpl()
) : ViewModel() {
    fun getData(date: Long): LiveData<MarsRoverPhotosData> {
        sendServerRequest(date.convertToNasaApiDate())
        return liveDataToObserve
    }

    private fun sendServerRequest(date: String) {
        liveDataToObserve.value = MarsRoverPhotosData.Loading
        val apiKey = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            liveDataToObserve.value = MarsRoverPhotosData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPhotoByDate(apiKey, date).enqueue(
                object : Callback<MRPServerResponseData> {
                    override fun onResponse(
                        call: Call<MRPServerResponseData>,
                        response: Response<MRPServerResponseData>
                    ) {
                        val serverResponse = response.body()
                        if (response.isSuccessful && serverResponse != null) {
                            if (serverResponse.photos.isNullOrEmpty()) {
                                liveDataToObserve.value =
                                    MarsRoverPhotosData.Error(Throwable("No photo for the selected date"))
                            }
                            liveDataToObserve.value = MarsRoverPhotosData.Success(serverResponse)
                        } else {
                            val message = response.message()
                            liveDataToObserve.value = if (message.isNullOrBlank()) {
                                MarsRoverPhotosData.Error(Throwable("Undefined error"))
                            } else {
                                MarsRoverPhotosData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<MRPServerResponseData>, t: Throwable) {
                        liveDataToObserve.value = MarsRoverPhotosData.Error(t)
                    }

                }
            )
        }
    }
}