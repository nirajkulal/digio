package com.exercise.digio.data.network.models

import com.google.gson.annotations.SerializedName

data class DocumentDetailsResponse(
    @SerializedName("agreement_status") var status: String? = null

)
