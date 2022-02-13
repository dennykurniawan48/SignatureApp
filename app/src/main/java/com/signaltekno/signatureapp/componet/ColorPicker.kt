package com.signaltekno.signatureapp.componet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.signaltekno.signatureapp.R
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun ColorPicker(
    onColorSelected: (Color) -> Unit
) {
    Text(
        text = "Color Picker with draggable",
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier.padding(8.dp)
    )
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenWidthInPx = with(LocalDensity.current){ screenWidth.toPx() }
    var activeColor by remember{ mutableStateOf(Color.Red)}

    val max = screenWidth-16.dp
    val min = 0.dp
    val (minPx, maxPx) = with(LocalDensity.current){min.toPx() to max.toPx()}

    var dragOffset by remember{ mutableStateOf(0f)}

    Box(modifier = Modifier.padding(8.dp)){
        Spacer(modifier = Modifier
            .height(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(brush = colorMapGradient(screenWidthInPx))
            .align(Alignment.Center)
            .pointerInput("painter") {
                detectTapGestures { offset ->
                    dragOffset = offset.x
                    activeColor = getActiveColor(dragOffset, screenWidthInPx)
                    onColorSelected.invoke(activeColor)
                }
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.fiber_manual_record),
            contentDescription = null,
            tint = activeColor,
            modifier = Modifier
                .offset {
                    IntOffset(dragOffset.roundToInt(), 0)
                }
                .border(
                    border = BorderStroke(4.dp, MaterialTheme.colors.onSurface),
                    shape = CircleShape
                )
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState{ delta->
                        val newValue = dragOffset+delta
                        dragOffset = newValue.coerceIn(minPx, maxPx)
                        activeColor = getActiveColor(dragOffset, screenWidthInPx)
                        onColorSelected.invoke(activeColor)
                    }
                )
        )
    }
}

fun colorMapGradient(screenWidthInPx: Float) = Brush.horizontalGradient(
    colors = createColorMap(),
    startX = 0f,
    endX = screenWidthInPx
)

fun getActiveColor(dragPosition: Float, screenWidthInPx: Float): Color{
    val hue = (dragPosition/screenWidthInPx)*360f
    val randomSaturation = 90 + Random.nextFloat() * 10
    val randomLightness = 50 + Random.nextFloat() * 10
    return Color(
        android.graphics.Color.HSVToColor(
            floatArrayOf(
                hue,
                randomSaturation,
                randomLightness
            )
        )
    )
}

fun createColorMap(): List<Color>{
    val colorList = mutableListOf<Color>()
    for(i in 0..360 step 2){
        val randomSaturation = 90 + Random.nextFloat() * 10
        val randomLightness = 50 + Random.nextFloat() * 10
        val hsv = android.graphics.Color.HSVToColor(
            floatArrayOf(
                i.toFloat(),
                randomSaturation,
                randomLightness
            )
        )
        colorList.add(Color(hsv))
    }
    return colorList
}
