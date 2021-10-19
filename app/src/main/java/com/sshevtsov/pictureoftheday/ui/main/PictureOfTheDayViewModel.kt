package com.sshevtsov.pictureoftheday.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sshevtsov.pictureoftheday.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheDayViewModel(
    private val liveDataToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    fun getData(date: String): LiveData<PictureOfTheDayData> {
        sendServerRequest(date)
        return liveDataToObserve
    }

    private fun sendServerRequest(date: String) {
        liveDataToObserve.value = PictureOfTheDayData.Loading
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, date).enqueue(
                object : Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        val serverResponse = response.body()
                        if (response.isSuccessful && serverResponse != null) {
                            liveDataToObserve.value = PictureOfTheDayData.Success(serverResponse)
                        } else {
                            val message = response.message()
                            liveDataToObserve.value = if (message.isNullOrBlank())
                                PictureOfTheDayData.Error(Throwable("Unidentified error"))
                            else {
                                PictureOfTheDayData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                        liveDataToObserve.value = PictureOfTheDayData.Error(t)
                    }

                }
            )
        }
    }
}