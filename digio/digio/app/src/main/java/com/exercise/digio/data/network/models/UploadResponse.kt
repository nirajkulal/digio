package com.exercise.digio.data.network.models

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("id") var id: String? = null
)