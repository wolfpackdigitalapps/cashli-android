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
private const val MAX_SEEKBAR_RANGE = 100f

sealed class StyledSliderUIState(val value: LiveData<Float>, val invertedColorScheme: Boolean) {
    data class ClaimCashSliderUIState(
        val sliderValue: LiveData<Float>,
        val labelValue: LiveData<Float>,
        val isInvertedColorScheme: Boolean
    ) : StyledSliderUIState(sliderValue, isInvertedColorScheme)

    data class TipAmountSliderUIState(
        val sliderValue: LiveData<Float>,
        val tipAmountPerc: LiveData<Float>,
        val tipAmount: LiveData<Float>,
        val isInvertedColorScheme: Boolean,
    ) : StyledSliderUIState(sliderValue, isInvertedColorScheme)
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
                colorResource(id = R.color.claimCashSeekbarAltGradientStart),
                colorResource(id = R.color.claimCashSeekbarAltGradientCenter),
                colorResource(id = R.color.claimCashSeekbarAltGradientEnd)
            )
        } else {
            listOf(
                colorResource(id = R.color.claimCashSeekbarGradientStart),
                colorResource(id = R.color.claimCashSeekbarGradientCenter),
                colorResource(id = R.color.claimCashSeekbarGradientEnd)
            )
        }
    )

    val currentValue by uiState.value.observeAsState(0f)

    val labelText = when (uiState) {
        is StyledSliderUIState.ClaimCashSliderUIState -> {
            stringResource(id = R.string.dollar_amount_float, currentValue)
        }

        is StyledSliderUIState.TipAmountSliderUIState -> {
            val tipAmountPerc by uiState.tipAmountPerc.observeAsState(initial = 0f)
            val tipAmount by uiState.tipAmount.observeAsState(initial = 0f)
            stringResource(id = R.string.quiz_tip_amount_slider, tipAmountPerc, tipAmount)
        }
    }

    SliderWithLabel(
        value = currentValue,
        onValueChange = onAmountChanged,
        trackHeight = dimensionResource(id = R.dimen.dimen_10dp),
        thumbRadius = dimensionResource(id = R.dimen.dimen_12dp),
        valueRange = 0f..MAX_SEEKBAR_RANGE,
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
