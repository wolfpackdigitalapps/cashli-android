package com.wolfpackdigital.cashli.shared.utils.extensions

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import com.wolfpackdigital.cashli.presentation.entities.AlphaAnimationConfig

// Extensions related to views (Views, TextViews, etc...)

fun View.setAlphaAnimationVisibility(alphaAnimationConfig: AlphaAnimationConfig) {
    val animationListener = object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation?) {
            alphaAnimationConfig.actionOnStart.invoke()
        }

        override fun onAnimationStart(animation: Animation?) {
            alphaAnimationConfig.actionOnEnd.invoke()
        }

        override fun onAnimationRepeat(animation: Animation?) {
            alphaAnimationConfig.actionOnRepeat.invoke()
        }
    }
    val anim = AlphaAnimation(
        alphaAnimationConfig.startAlpha,
        alphaAnimationConfig.endAlpha
    ).apply {
        duration = alphaAnimationConfig.duration
        alphaAnimationConfig.repeatCountValue?.let { repeatCount = it }
        alphaAnimationConfig.repeatModeValue?.let { repeatMode = it }
        setAnimationListener(animationListener)
    }
    startAnimation(anim)
}

fun View.getFocusAndShowKeyboard() {
    this.isEnabled = true
    this.requestFocus()
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}
