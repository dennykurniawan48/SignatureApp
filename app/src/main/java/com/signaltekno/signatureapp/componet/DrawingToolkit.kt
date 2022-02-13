package com.signaltekno.signatureapp.componet

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.signaltekno.signatureapp.R
import com.signaltekno.signatureapp.ui.theme.graySurface

@Composable
fun DrawingToolkit(
        drawColor: Color,
        usedColor: MutableSet<Color>,
        onDrawColorChanges: (Color) -> Unit,
        onDrawBrushChanges: (Float)->Unit
    ) {
        var showBrushes by remember{ mutableStateOf(false) }
        var strokes = remember {
            (1..50 step 5).toList()
        }
        
    Column(modifier=Modifier.padding(horizontal = 8.dp)) {
        ColorPicker(
            onColorSelected = {
                color -> onDrawColorChanges.invoke(color)
            }
        )
        Row(
            modifier= Modifier
                .horizontalGradientBackground(listOf(graySurface, Color.Black))
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .horizontalScroll(rememberScrollState())
                .animateContentSize()
        ){
            usedColor.forEach{
                IconButton(onClick = { onDrawColorChanges.invoke(it)
                    Log.d("Path", it.toString())
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Pick",
                        tint = it,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }
        }
        FloatingActionButton(onClick = { showBrushes = !showBrushes }, modifier = Modifier.padding(vertical = 4.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_brush),
                contentDescription = "Brush",
                tint = drawColor
            )
        }
        AnimatedVisibility(visible = showBrushes) {
            LazyColumn{
                items(strokes){
                    IconButton(onClick = {
                        onDrawBrushChanges(it.toFloat())
                        showBrushes = false
                    },
                        modifier = Modifier
                            .padding(8.dp)
                            .border(
                                border = BorderStroke(
                                    width = with(LocalDensity.current) { it.toDp() },
                                    color = Color.Gray
                                ),
                                shape = CircleShape
                            )) {

                    }
                }
            }
        }
    }}

fun Modifier.horizontalGradientBackground(colors: List<Color>) = gradientBackground(colors){ gradientColor, size ->
    Brush.horizontalGradient(
        colors = colors,
        startX = 0f,
        endX = size.width
    )
}

fun Modifier.gradientBackground(
    colors: List<Color>,
    brushProvider: (List<Color>, Size) -> Brush
): Modifier = composed {
    var size by remember { mutableStateOf(Size.Zero) }
    val gradient = remember(colors, size) { brushProvider(colors, size) }
    drawWithContent {
        size = this.size
        drawRect(brush = gradient)
        drawContent()
    }
}