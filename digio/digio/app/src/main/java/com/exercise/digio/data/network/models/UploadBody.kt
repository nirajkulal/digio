package com.exercise.digio.data.network.models

import com.google.gson.annotations.SerializedName

data class UploadBody(
    @SerializedName("signers") var signers: ArrayList<Signers>? = null,
    @SerializedName("expire_in_days") var expire_in_days: Int? = 10,
    @SerializedName("display_on_page") var display_on_page: String? = "all",
    @SerializedName("file_name") var file_name: String? = null,
    @SerializedName("file_data") var file_data: String? = null,
    )

data class Signers(
    @SerializedName("identifier") var identifier: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("reason") var reason: String? = null,
)
