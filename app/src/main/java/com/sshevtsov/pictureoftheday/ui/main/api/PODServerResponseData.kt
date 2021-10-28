package com.sshevtsov.pictureoftheday.ui.main.api

import com.google.gson.annotations.SerializedName

data class PODServerResponseData(
    @SerializedName("date") val date: String?,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("hdurl") val hdurl: String?
)
