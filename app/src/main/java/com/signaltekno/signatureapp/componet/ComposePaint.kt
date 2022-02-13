package com.signaltekno.signatureapp.componet

import android.app.Notification
import android.util.Log
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import com.signaltekno.signatureapp.model.PathState

@ExperimentalComposeUiApi
@Composable
fun ComposePaint() {
    var paths by remember{ mutableStateOf(mutableListOf<PathState>()) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Signature")
                },
                actions = {
                    IconButton(onClick = { paths = mutableListOf() }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Clear")
                    }
                }
            )
        }
    ){
        PainterBody(paths = paths){
            Log.d("Pathh","Path: ${it.path}")
            paths.add(it)
        }
    }
}
