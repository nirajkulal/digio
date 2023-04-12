package com.exercise.digio.application.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val mainVM: MainVM by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                selectPDF()
            }
        }

    private val activityResultContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            // Handle the returned Uri
            println("URI ${it.data}")
            val uri: Uri? = it.data?.data
            uri?.let { uri ->
                val inputStream = contentResolver.openInputStream(uri)
                inputStream?.readBytes()?.let {
                    mainVM.setEvent(
                        MainEvents.UploadFile(
                            it
                        )
                    )
                }
            }
        }

    private fun selectPDF() {
        // Launch intent
        activityResultContracts.launch(
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "application/pdf"
            }
        )
    }

    private fun fetchFile() {
        // check permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager
                .PERMISSION_GRANTED
        ) {
            // When permission is not granted
            // Result permission
            requestPermissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } else {
            // When permission is granted
            // Create method
            selectPDF()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainVM.setEvent(MainEvents.Validate)
        setContent {
            MainPage(
                state = mainVM.state.collectAsState().value,
                onSelectDocument = {
                    fetchFile()
                },
                onNameChange = {
                    mainVM.setEvent(MainEvents.OnNameChange(it))
                },
                onPhoneChange = {
                    mainVM.setEvent(MainEvents.OnPhoneChane(it))
                },
                onGetDocumentDetails = {
                    mainVM.setEvent(MainEvents.OnGetDocDetails)
                },
                downloadPdf = {
                    mainVM.setEvent(MainEvents.DownloadDoc(this))
                }
            )
        }
    }


}
