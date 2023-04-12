package com.exercise.digio.application.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.exercise.digio.R

@Composable
fun MainPage(
    state: MainState,
    onSelectDocument: () -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onGetDocumentDetails: () -> Unit,
    downloadPdf: () -> Unit
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            EditTextField(
                placeHolder = stringResource(R.string.name),
                onValueChange = {
                    onNameChange(it)
                }
            )
            EditTextField(
                placeHolder = stringResource(R.string.phoneormail),
                onValueChange = {
                    onPhoneChange(it)
                }
            )
            state.uploadError?.let {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    text = it
                )
            }
            if (state.uploadError == null)
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        onSelectDocument()
                    }) {
                    Text(text = stringResource(R.string.select_doc))
                }

            state.documentID?.let {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally), text = it
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        onGetDocumentDetails()
                    }) {
                    Text(text = stringResource(R.string.get_doc_details))
                }
                state.documentStatus?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally), text = it
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        onClick = {
                            downloadPdf()
                        }) {
                        Text(text = stringResource(R.string.download))
                    }

                    state.downloadedPath?.let {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally), text = it
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditTextField(
    placeHolder: String,
    onValueChange: (value: String) -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        placeholder = {
            Text(text = placeHolder)
        }
    )
}