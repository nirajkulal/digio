package com.exercise.digio.domain

import com.exercise.digio.data.network.models.UploadResponse
import javax.inject.Inject

class UploadUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun uploadPdf(pdfBytes: ByteArray, name: String, phone: String): UploadResponse? {
        return repository.fileUpload(
            pdfBytes,
            name,
            phone
        )
    }
}