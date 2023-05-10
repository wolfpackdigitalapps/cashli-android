package com.wolfpackdigital.cashli.shared.utils.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.slider.ColorfulSlider
import com.smarttoolfactory.slider.MaterialSliderColors
import com.smarttoolfactory.slider.MaterialSliderDefaults
import com.smarttoolfactory.slider.calculateFraction
import com.wolfpackdigital.cashli.R

@Suppress("LongParameterList", "LongMethod", "FunctionNaming")
@Composable
fun SliderWithLabel(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    trackHeight: Dp = dimensionResource(id = R.dimen.dimen_4dp),
    thumbRadius: Dp = dimensionResource(id = R.dimen.dimen_10dp),
    colors: MaterialSliderColors = MaterialSliderDefaults.defaultColors(),
    borderStroke: BorderStroke? = null,
    yOffset: Dp = 0.dp,
    horizontalPadding: Dp = 0.dp,
    label: @Composable () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier) {
        val maxWidth = constraints.maxWidth.toFloat()
        Column {
            val yOffsetInt = with(LocalDensity.current) {
                yOffset.toPx().toInt()
            }

            val horizontalPaddingPx = with(LocalDensity.current) {
                horizontalPadding.toPx().toInt()
            }

            var labelOffset by remember {
                mutableStateOf(
                    Offset(
                        x = scale(
                            valueRange.start, valueRange.endInclusive, value, 0f, maxWidth
                        ),
                        y = 0f
                    )
                )
            }
            var labelWidth by remember { mutableStateOf(0) }
            var sliderMinX by remember { mutableStateOf(0f) }
            var sliderMaxX by remember { mutableStateOf(maxWidth) }

            ColorfulSlider(
                value = value,
                onValueChange = { value, offset ->
                    labelOffset = offset
                    onValueChange(value)
                },
                valueRange = valueRange,
                steps = steps,
                trackHeight = trackHeight,
                thumbRadius = thumbRadius,
                borderStroke = borderStroke,
                coerceThumbInTrack = true,
                colors = colors,
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .onGloballyPositioned { coordinates ->
                        sliderMinX = coordinates.positionInWindow().x
                        sliderMaxX = sliderMinX + coordinates.size.width
                    }
            )
            val initialOffset = labelOffset.x.toInt() - labelWidth / 2 + horizontalPaddingPx
            val xOffset = when {
                initialOffset < sliderMinX -> sliderMinX
                initialOffset + labelWidth > sliderMaxX -> sliderMaxX - labelWidth
                else -> initialOffset
            }.toInt()

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = xOffset,
                            y = yOffsetInt
                        )
                    }
                    .onSizeChanged { labelWidth = it.width }
            ) {
                label()
            }
        }
    }
}

private fun lerp(start: Float, end: Float, amount: Float): Float {
    return (1 - amount) * start + amount * end
}

private fun scale(start1: Float, end1: Float, pos: Float, start2: Float, end2: Float) =
    lerp(start2, end2, calculateFraction(start1, end1, pos))
