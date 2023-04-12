package com.exercise.digio.application.views

import android.app.Activity
import android.os.Environment
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.exercise.digio.application.MviEffect
import com.exercise.digio.application.MviEvent
import com.exercise.digio.application.MviState
import com.exercise.digio.application.MviViewModel
import com.exercise.digio.domain.DownloadUseCase
import com.exercise.digio.domain.GetDocumentDetailsUseCase
import com.exercise.digio.domain.UploadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val uploadUseCase: UploadUseCase,
    private val getDocumentDetailsUseCase: GetDocumentDetailsUseCase,
    private val downloadUseCase: DownloadUseCase
) : MviViewModel<MainState, MainEvents, MainEffects>(
    MainState()
) {
    override suspend fun handleEvents(event: MainEvents) {

        when (event) {

            is MainEvents.OnNameChange -> {
                updateState {
                    copy(
                        name = event.name
                    )
                }
                setEvent(MainEvents.Validate)
            }
            is MainEvents.OnPhoneChane -> {
                updateState {
                    copy(
                        phone = event.phone
                    )
                }
                setEvent(MainEvents.Validate)
            }
            is MainEvents.UploadFile -> {
                if (currentState().uploadError == null) {
                    viewModelScope.launch {
                        val res = uploadUseCase.uploadPdf(
                            event.pdfBytes, currentState().name ?: "", currentState().phone ?: ""
                        )

                        res?.let {
                            updateState {
                                copy(
                                    documentID = res?.id
                                )
                            }
                        } ?: kotlin.run {
                            updateState {
                                copy(
                                    documentID = "upload failed"
                                )
                            }
                        }
                    }
                }else{
                    validateFields()
                }

            }
            MainEvents.OnGetDocDetails -> {
                viewModelScope.launch {
                    currentState().documentID?.let {
                        getDocumentDetailsUseCase.getDocumentDetails(it).let {
                            updateState {
                                copy(
                                    documentStatus = it?.status
                                )
                            }
                        }
                    }
                }
            }
            is MainEvents.DownloadDoc -> {
                currentState().documentID?.let {
                    viewModelScope.launch {
                        downloadUseCase.downloadDocument(it).let {
                            println("RESPONSE - ${it}")
                            currentState().name?.let { it1 ->
                                saveFile(it, event.context, it1)?.let {
                                    updateState {
                                        copy(
                                            downloadedPath = it
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            MainEvents.Validate -> {
                validateFields()
            }
        }
    }

    private fun saveFile(body: ResponseBody?, context: Activity, name: String): String? {
        if (body == null) {
            return ""
        }
        val path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File.createTempFile("sample", ".pdf", path)
        val os = FileOutputStream(file)
        os.write(body.bytes())
        os.close()
        return path?.path
    }

    private fun validateFields() {
        viewModelScope.launch {
            if (currentState().name == null || currentState().name?.isEmpty() == true) {
                updateState {
                    copy(
                        uploadError = "Please enter valid name"
                    )
                }
            } else if (currentState().phone == null) {
                updateState {
                    copy(
                        uploadError = "Please enter  email/phone"
                    )
                }
            } else if (currentState().phone?.isDigitsOnly() == true && currentState().phone?.length != 10) {
                updateState {
                    copy(
                        uploadError = "Please enter valid phone"
                    )
                }
            } else if (currentState().phone?.isDigitsOnly() == false && android.util.Patterns.EMAIL_ADDRESS.matcher(
                    currentState().phone
                ).matches()
                    .not()
            ) {
                updateState {
                    copy(
                        uploadError = "Please enter valid email"
                    )
                }
            } else {
                updateState {
                    copy(
                        uploadError = null
                    )
                }
            }
        }
    }

    override suspend fun handleEffects(effect: MainEffects) {}

}

data class MainState(
    var name: String? = null,
    var phone: String? = null,
    var documentStatus: String? = null,
    var pdfBytes: ByteArray? = null,
    var uploadError: String? = null,
    var documentID: String? = null,
    var downloadedPath: String? = null,
) : MviState

sealed class MainEvents : MviEvent {
    data class OnNameChange(var name: String) : MainEvents()
    data class OnPhoneChane(var phone: String) : MainEvents()
    data class UploadFile(var pdfBytes: ByteArray) : MainEvents()
    object OnGetDocDetails : MainEvents()
    object Validate : MainEvents()
    data class DownloadDoc(var context: Activity) : MainEvents()
}

sealed class MainEffects : MviEffect