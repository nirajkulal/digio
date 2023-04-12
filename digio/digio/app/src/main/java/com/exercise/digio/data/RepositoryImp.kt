package com.exercise.digio.data

import android.util.Base64
import com.exercise.digio.data.network.models.*
import com.exercise.digio.data.network.retrofit.RetrofitInstance
import com.exercise.digio.domain.Repository
import okhttp3.ResponseBody
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val network: RetrofitInstance
) : Repository {
    override suspend fun fileUpload(file: ByteArray, name: String, phone: String): UploadResponse? {
        return network.getRetrofit().upload(
            body = UploadBody(
                signers = arrayListOf(
                    Signers(
                        identifier = phone,
                        name = name,
                        reason = "File Upload"
                    )
                ),
                file_data = Base64.encodeToString(file, Base64.NO_WRAP),
                file_name = "test.pdf"
            )
        ).let {
            println("RESPONSE: ${it.raw()}")
            return it.body()
        }
    }

    override suspend fun getDocDetails(docId: String): DocumentDetailsResponse? {
        return network.getRetrofit().getDocDetails(documentId = docId).body()
    }

    override suspend fun downloadDoc(docId: String): ResponseBody? {
        return network.getRetrofit().downloadDoc(documentId = "DID22120221494935658NJEOP2QQNA4H").let {
            println("RESPONSE ${it.raw()}")
            it.body()
        }
    }
}