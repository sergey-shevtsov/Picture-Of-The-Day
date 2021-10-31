package com.sshevtsov.pictureoftheday.ui.mars.api

import com.google.gson.annotations.SerializedName

data class MRPServerResponseData(
    @SerializedName("photos") val photos: Array<Photo>?
) {
    data class Photo(
        @SerializedName("img_src") val imageSrc: String?
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MRPServerResponseData

        if (photos != null) {
            if (other.photos == null) return false
            if (!photos.contentEquals(other.photos)) return false
        } else if (other.photos != null) return false

        return true
    }

    override fun hashCode(): Int {
        return photos?.contentHashCode() ?: 0
    }
}