package com.exercise.digio.data.network.retrofit

import android.util.Base64
import com.exercise.digio.data.network.models.DocumentDetailsResponse
import com.exercise.digio.data.network.models.UploadBody
import com.exercise.digio.data.network.models.UploadResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @POST("/v2/client/document/uploadpdf")
    suspend fun upload(
        @Header("authorization") authorization: String = "Basic  $auth",
        @Header("content-type") type: String = "application/json",
        @Body body: UploadBody,
    ): Response<UploadResponse>

    @GET("/v2/client/document/{documentId}")
    suspend fun getDocDetails(
        @Header("authorization") authorization: String = "Basic  $auth",
        @Path("documentId") documentId: String
    ): Response<DocumentDetailsResponse>

    @GET("/v2/client/document/download")
    @Streaming
    suspend fun downloadDoc(
        @Header("authorization") authorization: String = "Basic  $auth",
        @Query("document_id") documentId: String
    ): Response<ResponseBody>

}

private val auth: String
    get() {
        return Base64.encodeToString(
            "AIZ67DUSNZ8TGWJV4DZ7DI3T5Z2LN2W2:ASN9AVKHU6HF41KTU71G3KNXLG1ET7BC".toByteArray(),
            Base64.NO_WRAP
        )
    }