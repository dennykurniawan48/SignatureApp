package com.signaltekno.signatureapp.componet

import android.graphics.Paint
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.signaltekno.signatureapp.model.PathState

@ExperimentalComposeUiApi
@Composable
fun DrawingCanvas(drawBrush: Float, drawColor: Color, usedColor: MutableSet<Color>, paths: MutableList<PathState>, onUsedColorChanges: (MutableSet<Color>) -> Unit) {
    val currentPath = paths.last().path
    var movePath by remember{ mutableStateOf<Offset?>(null)}

    Canvas(modifier = Modifier.fillMaxSize()
        .padding(10.dp)
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentPath.moveTo(it.x, it.y)
                    usedColor.add(drawColor)
                }
                MotionEvent.ACTION_MOVE -> {
                    movePath = Offset(it.x, it.y)
                }
                else -> { movePath = null }
            }
            true
        }){

        movePath?.let {
            currentPath.lineTo(it.x, it.y)
            drawPath(
                path = currentPath,
                style = Stroke(drawBrush),
                color = drawColor
            )
        }

        paths.forEach {
            drawPath(
                path = it.path,
                color = it.color,
                style = Stroke(it.stroke)
            )
        }

    }
}
