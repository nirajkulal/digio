package com.exercise.digio.domain

import com.exercise.digio.data.network.models.DocumentDetailsResponse
import com.exercise.digio.data.network.models.UploadResponse
import okhttp3.ResponseBody

interface Repository {
    suspend fun fileUpload(file: ByteArray, name: String, phone: String): UploadResponse?
    suspend fun getDocDetails(docId: String): DocumentDetailsResponse?
    suspend fun downloadDoc(docId: String): ResponseBody?
}