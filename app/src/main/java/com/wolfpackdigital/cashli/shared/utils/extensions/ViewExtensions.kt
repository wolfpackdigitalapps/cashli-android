package com.wolfpackdigital.cashli.shared.utils.extensions

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.wolfpackdigital.cashli.presentation.entities.AlphaAnimationConfig

// Extensions related to views (Views, TextViews, etc...)
private const val SCROLL_DOWN = 1
private const val SCROLL_UP = -1

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

fun RecyclerView.reachedViewBottom() = !canScrollVertically(SCROLL_DOWN)

fun RecyclerView.reachedViewTop() = !canScrollVertically(SCROLL_UP)

fun RecyclerView.canScrollBothDirections() =
    canScrollVertically(SCROLL_DOWN) && canScrollVertically(SCROLL_UP)

fun View.handleExtraPadding(
    extraPaddingNeeded: Boolean = true,
    @DimenRes defaultPaddingResId: Int? = null,
    @DimenRes extraPaddingResId: Int? = null
) {
    val bottomPadding =
        if (extraPaddingNeeded)
            extraPaddingResId?.let { resources.getDimension(it).toInt() } ?: return
        else
            defaultPaddingResId?.let { resources.getDimension(it).toInt() } ?: return
    setPadding(0, 0, 0, bottomPadding)
}

fun View.getFocusAndShowKeyboard() {
    this.isEnabled = true
    this.requestFocus()
    showSoftKeyboard(this)
}
