package com.udacity


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonTextStr = ""
    private var status =""
    private val valueAnimator  = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f).setDuration(3000)

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, status ->
        when (status) {
            ButtonState.Loading -> {
                buttonTextStr = resources.getString(R.string.button_loading)
                valueAnimator.start()
                this.status = "load"
            }
            ButtonState.Completed -> {
                buttonTextStr = resources.getString(R.string.download)
                valueAnimator.cancel()
                this.status = "comp"

             }
        }

        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 100.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    init {
        buttonState = ButtonState.Completed
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
               // this.isEnabled = false

            }
            override fun onAnimationEnd(animation: Animator?) {
               // rotateButton.isEnabled = true
            }
        })

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //  set background color
        paint.color = context.getColor(R.color.colorPrimary)
        canvas?.drawRect(0f,0f,widthSize.toFloat(), heightSize.toFloat(), paint)
        // loading button
        paint.color = context.getColor(R.color.cardview_shadow_start_color)
        canvas?.drawRect(0f, 0f, widthSize * 0/360f, heightSize.toFloat(), paint)
        // set text and text color
        paint.color = context.getColor(R.color.white)
         canvas?.drawText(buttonTextStr, widthSize/2.0f, heightSize/2.0f + 30.0f, paint)
        // circle
        paint.color = context.getColor(R.color.colorAccent)
        canvas?.drawArc(widthSize - 200f,50f,widthSize - 100f,150f,0f, 0.toFloat(), true, paint)
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