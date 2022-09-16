package com.udacity


import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonTextStr = ""
    private var progress: Double = 0.0
    private var valueAnimator = AnimatorInflater.loadAnimator(context, R.animator.loading_animation) as ValueAnimator


    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, status ->
        when (status) {
            ButtonState.Loading -> {
                buttonTextStr = resources.getString(R.string.button_loading)
                valueAnimator.start()
                this.isClickable = false
            }
            ButtonState.Completed -> {
                buttonTextStr = resources.getString(R.string.download)
                valueAnimator.cancel()
                valueAnimator.reverse()
                this.isClickable = true
                invalidate()
                requestLayout()

            }

            ButtonState.Clicked -> {
                this.isClickable = false

            }
        }

        invalidate()
    }

    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()
        invalidate()
        requestLayout()
    }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 80.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
        buttonState = ButtonState.Completed

        valueAnimator.addUpdateListener(updateListener)

    }


    private val rect = RectF(
        740f,
        50f,
        810f,
        110f
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //  set background color for button
        paint.color = context.getColor(R.color.colorPrimary)
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        // set text and text color for button
        paint.color = context.getColor(R.color.white)
        canvas?.drawText(buttonTextStr, widthSize / 2.0f, heightSize / 2.0f + 30.0f, paint)

        if (buttonTextStr.equals("We are loading")) {
//            paint.color = context.getColor(R.color.colorAccent)
//            canvas?.drawCircle(widthSize / 1.20f, heightSize / 2.0f, 30f, paint)

            // draw dark rectangle and set text on it
            paint.color = context.getColor(R.color.colorPrimaryDark)
            canvas?.drawRect(0f, 0f, (width * (progress / 100)).toFloat(), height.toFloat(), paint)
            paint.color = context.getColor(R.color.white)
            canvas?.drawText(buttonTextStr, widthSize / 2.0f, heightSize / 2.0f + 30.0f, paint)
            // drawCircle
            paint.color = context.getColor(R.color.colorAccent)
            canvas?.drawArc(rect, 0f, (360 * (progress / 100)).toFloat(), true, paint)

        } else {
            // to reset all views
            animation?.cancel()
            invalidate()
            requestLayout()
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)

    }

}