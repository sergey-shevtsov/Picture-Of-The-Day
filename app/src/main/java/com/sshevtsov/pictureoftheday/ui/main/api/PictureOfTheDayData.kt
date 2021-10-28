package com.sshevtsov.pictureoftheday.ui.main.api

sealed class PictureOfTheDayData {
    object Loading : PictureOfTheDayData()
    data class Success(val serverResponseData: PODServerResponseData) : PictureOfTheDayData()
    data class Error(val error: Throwable) : PictureOfTheDayData()
}
