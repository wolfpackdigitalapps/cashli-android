package com.wolfpackdigital.cashli.shared.utils.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.wolfpackdigital.cashli.R

class ArcBackgroundView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private val path = Path()

    init {
        paint.isAntiAlias = true
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        // Draw background color
        canvas.drawColor(Color.TRANSPARENT)
        paint.color = ContextCompat.getColor(context, R.color.colorWhiteF9)
        val curveStartAndEndY = context.resources.getDimension(R.dimen.dimen_32dp)
        path.moveTo(0f, curveStartAndEndY)
        path.quadTo(
            measuredWidth / 2f,
            -curveStartAndEndY,
            measuredWidth.toFloat(),
            curveStartAndEndY
        )
        path.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat())
        path.lineTo(0f, measuredHeight.toFloat())
        path.lineTo(0f, curveStartAndEndY)
        canvas.drawPath(path, paint)
    }
}
