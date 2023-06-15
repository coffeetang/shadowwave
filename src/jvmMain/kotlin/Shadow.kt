import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs
import kotlin.math.sqrt

@Composable
fun light(lightState: Boolean) {
    var screenW by remember { mutableStateOf(0f) }
    var screenY by remember { mutableStateOf(0f) }
    val gridCount = 300
    val xUnit = screenW / gridCount
    val yUnit = screenY / gridCount
    val pRadius = xUnit.coerceAtMost(yUnit) / 2
    val xList = Array(gridCount) { xUnit + it * xUnit }
    val yList = Array(gridCount) { yUnit + it * yUnit }
    val circleSize by animateFloatAsState(
            if (lightState) 200f else 10f, animationSpec = TweenSpec(
            durationMillis = 1500, easing = LinearEasing
    )
    )
    var lightVisible by remember { mutableStateOf(true) }
    val lightAlpha by animateFloatAsState(if (lightVisible) 1f else 0f, animationSpec = TweenSpec(
            durationMillis = 1000, easing = LinearEasing
    ))

    val lightColor by animateColorAsState(
            if (lightState) Color(0xffF7DF07) else Color(0x2e000000),
            animationSpec = TweenSpec(
                    durationMillis = 1500, easing = LinearEasing
            )
    )

    val tapX = xList[xList.lastIndex / 2]
    val tapY = yList[yList.lastIndex / 6] + 100f + 30f

    Canvas(
            modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectTapGestures {
                    lightVisible = !lightVisible
                }
            }
    ) {
        screenW = size.width
        screenY = size.height
        for (xIndex in xList.indices) {
            for (yIndex in yList.indices) {
                val xdis = abs(xList[xIndex] - tapX)
                val ydis = abs(yList[yIndex] - tapY)
                val radius = sqrt((xdis * xdis + ydis * ydis).toDouble()).toInt()
                val div = radius / circleSize
                if (circleSize > 10f) {
                    drawCircle(
                            Color(0x33000000), pRadius * div / 3,
                            center = Offset(xList[xIndex], yList[yIndex]),
                            alpha = if (div < 1) div else 1f
                    )
                } else {
                    drawCircle(
                            Color(0x99000000), pRadius * 2,
                            center = Offset(xList[xIndex], yList[yIndex])
                    )
                }
            }
        }
        drawCircle(
                color = lightColor,
                radius = 30f,
                center = Offset(tapX, tapY),
                alpha = lightAlpha
        )
        drawLine(
                Color.Black, start = Offset(xList[xList.lastIndex / 2], 0f),
                end = Offset(xList[xList.lastIndex / 2], yList[yList.lastIndex / 6]), strokeWidth = 2f,
                alpha = lightAlpha
        )
        drawArc(
                color = Color.Black,
                startAngle = 0f,
                sweepAngle = -180f,
                useCenter = true,
                size = Size(200f, 200f),
                topLeft = Offset(xList[xList.lastIndex / 2] - 100f, yList[yList.lastIndex / 6]),
                alpha = lightAlpha
        )
    }
}