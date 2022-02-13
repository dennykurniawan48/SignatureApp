package com.signaltekno.signatureapp.componet

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.signaltekno.signatureapp.model.PathState

@ExperimentalComposeUiApi
@Composable
fun PainterBody(
    paths: MutableList<PathState>,
    onAddPath: (path: PathState) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        var drawColor by remember { mutableStateOf(Color.Black) }
        var drawBrush by remember {
            mutableStateOf(5f)
        }
        var usedColor by remember {
            mutableStateOf(mutableSetOf(Color.Black, Color.White, Color.Gray))
        }

        onAddPath(PathState(Path(), drawColor, drawBrush))

        Log.d("Path", Path().toString())

        DrawingCanvas(
            drawBrush,
            drawColor,
            usedColor,
            paths
        ) {
            usedColor = it
        }

        DrawingToolkit(
            drawColor = drawColor,
            usedColor = usedColor,
            onDrawColorChanges = {
                drawColor = it
            },
            onDrawBrushChanges = {
                drawBrush = it
            }
        )
    }
}
