package com.wolfpackdigital.cashli.shared.utils.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.smarttoolfactory.slider.MaterialSliderDefaults
import com.smarttoolfactory.slider.SliderBrushColor
import com.wolfpackdigital.cashli.R

private const val LABEL_TEXT_SIZE = 12
const val MAX_TIP_SLIDER = 15f

sealed class StyledSliderUIState(
    val value: LiveData<Float>,
    val invertedColorScheme: Boolean,
    val minValue: Float,
    val maxValue: Float
) {
    data class ClaimCashSliderUIState(
        private val sliderValue: LiveData<Float>,
        private val isInvertedColorScheme: Boolean,
        private val minSliderValue: Float,
        private val maxSliderValue: Float
    ) : StyledSliderUIState(sliderValue, isInvertedColorScheme, minSliderValue, maxSliderValue)

    data class TipAmountSliderUIState(
        val tipAmount: LiveData<Float>,
        private val sliderValue: LiveData<Float>,
        private val isInvertedColorScheme: Boolean,
        private val minSliderValue: Float = 0f,
        private val maxSliderValue: Float = MAX_TIP_SLIDER,
    ) : StyledSliderUIState(sliderValue, isInvertedColorScheme, minSliderValue, maxSliderValue)
}

@Suppress("LongMethod", "FunctionNaming")
@Composable
fun StyledSlider(
    uiState: StyledSliderUIState,
    horizontalPadding: Dp = dimensionResource(id = R.dimen.dimen_28dp),
    onAmountChanged: (Float) -> Unit
) {
    val context = LocalContext.current
    val (borderColor, thumbColor, labelColor) = if (uiState.invertedColorScheme) {
        listOf(
            colorResource(id = R.color.colorGrayB6),
            colorResource(id = R.color.colorPrimaryDark),
            colorResource(id = R.color.colorGray76)
        )
    } else {
        listOf(
            colorResource(id = R.color.colorGray76),
            colorResource(id = R.color.colorWhite),
            colorResource(id = R.color.colorGrayB6)
        )
    }

    val trackColor = Brush.horizontalGradient(
        if (uiState.invertedColorScheme) {
            listOf(
                colorResource(id = R.color.colorSeekbarAltGradientEnd),
                colorResource(id = R.color.colorSeekbarAltGradientCenter),
                colorResource(id = R.color.colorSeekbarAltGradientStart)
            )
        } else {
            listOf(
                colorResource(id = R.color.colorSeekbarGradientStart),
                colorResource(id = R.color.colorSeekbarGradientCenter),
                colorResource(id = R.color.colorSeekbarGradientEnd)
            )
        }
    )

    val currentValue by uiState.value.observeAsState(uiState.minValue)

    val labelText = when (uiState) {
        is StyledSliderUIState.ClaimCashSliderUIState -> {
            stringResource(id = R.string.dollar_amount_float, currentValue)
        }

        is StyledSliderUIState.TipAmountSliderUIState -> {
            val tipAmount by uiState.tipAmount.observeAsState(initial = 0f)
            stringResource(
                id = R.string.quiz_tip_amount_slider,
                MAX_TIP_SLIDER - currentValue,
                tipAmount
            )
        }
    }

    SliderWithLabel(
        value = currentValue,
        onValueChange = onAmountChanged,
        trackHeight = dimensionResource(id = R.dimen.dimen_10dp),
        thumbRadius = dimensionResource(id = R.dimen.dimen_12dp),
        valueRange = uiState.minValue..uiState.maxValue,
        colors = MaterialSliderDefaults.materialColors(
            thumbColor = SliderBrushColor(color = thumbColor),
            activeTrackColor = SliderBrushColor(brush = trackColor),
            inactiveTrackColor = SliderBrushColor(color = Color.Transparent),
        ),
        borderStroke = BorderStroke(
            width = dimensionResource(id = R.dimen.dimen_1dp),
            color = borderColor
        ),
        horizontalPadding = horizontalPadding,
        label = {
            Text(
                text = labelText,
                color = labelColor,
                fontSize = LABEL_TEXT_SIZE.sp,
                fontFamily = FontFamily(typeface = context.resources.getFont(R.font.lato_bold)),
            )
        }
    )
}
