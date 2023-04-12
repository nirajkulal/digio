package com.exercise.digio.domain

import com.exercise.digio.data.network.models.DocumentDetailsResponse
import javax.inject.Inject

class GetDocumentDetailsUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun getDocumentDetails(docID: String): DocumentDetailsResponse? {
        return repository.getDocDetails(docID)
    }

}