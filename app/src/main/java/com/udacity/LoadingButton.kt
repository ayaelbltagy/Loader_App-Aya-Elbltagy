package com.udacity


import android.R.attr.path
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import kotlin.properties.Delegates


@SuppressLint("ObjectAnimatorBinding")
class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonTextStr = ""
    private lateinit var anim: RotateAnimation
    private lateinit var canvas: Canvas


    // private val valueAnimator = ObjectAnimator.ofFloat(this, TRANSLATION_X, 10f)


    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, status ->
        when (status) {
            ButtonState.Loading -> {
                buttonTextStr = resources.getString(R.string.button_loading)
                // valueAnimator.start()
                this.isClickable = false
            }
            ButtonState.Completed -> {
                buttonTextStr = resources.getString(R.string.download)
                // valueAnimator.cancel()
                //  valueAnimator.reverse()
                this.isClickable = true


            }

            ButtonState.Clicked -> {
                this.isClickable = false

            }
        }

        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 80.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
        buttonState = ButtonState.Completed

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            this.canvas = canvas
        }
        //  set background color for button
        paint.color = context.getColor(R.color.colorPrimary)
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        // set text and text color for button
        paint.color = context.getColor(R.color.white)
        canvas?.drawText(buttonTextStr, widthSize / 2.0f, heightSize / 2.0f + 30.0f, paint)

        // drawCircle
        if (buttonTextStr.equals("We are loading")) {
            paint.color = context.getColor(R.color.colorAccent)
            canvas?.drawCircle(widthSize / 1.20f, heightSize / 2.0f, 30f, paint)

        } else {

            animation?.cancel()
         }
    }

    private fun initAnimation(canvas: Canvas?) {
//        var animation = RotateAnimation(-360f, 0f, widthSize/1.20f, heightSize /2.0f)
//         animation.duration = 5000
//        animation.start()
              val animator = ObjectAnimator.ofFloat(this, View.ROTATION, -360f, 0f)
              animator.duration = 5000
              animator.start()



    }

    private fun createAnimation( ) {
        anim = RotateAnimation(0f, 360f, widthSize / 1.20f, heightSize / 2.0f)
        anim.setRepeatMode(Animation.RESTART)
        anim.setRepeatCount(Animation.INFINITE)
        anim.setDuration(10000L)
        startAnimation(anim)
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