package com.exercise.digio.domain

import okhttp3.ResponseBody
import javax.inject.Inject

class DownloadUseCase @Inject constructor(
    private val repository: Repository
){

    suspend fun downloadDocument(docId :String): ResponseBody? {
        return repository.downloadDoc(docId)
    }

}