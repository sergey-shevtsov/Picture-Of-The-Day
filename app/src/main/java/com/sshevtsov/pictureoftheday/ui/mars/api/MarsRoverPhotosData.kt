package com.sshevtsov.pictureoftheday.ui.mars.api

sealed class MarsRoverPhotosData {
    object Loading : MarsRoverPhotosData()
    data class Success(val serverResponseData: MRPServerResponseData) : MarsRoverPhotosData()
    data class Error(val error: Throwable) : MarsRoverPhotosData()
}
