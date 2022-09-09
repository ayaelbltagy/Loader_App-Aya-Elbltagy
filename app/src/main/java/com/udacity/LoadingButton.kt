package com.udacity


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import kotlin.properties.Delegates


@SuppressLint("ObjectAnimatorBinding")
class LoadingButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonTextStr = ""
    private  lateinit var can : Canvas

   // private val valueAnimator  = ObjectAnimator.ofFloat(this,TRANSLATION_X,10f)
  @SuppressLint("ObjectAnimatorBinding")
  private val valueAnimator  = ObjectAnimator.ofInt(this, "progress", 0, 100)

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, status ->
        when (status) {
            ButtonState.Loading -> {
                buttonTextStr = resources.getString(R.string.button_loading)
                valueAnimator.start()

             }
            ButtonState.Completed -> {
                buttonTextStr = resources.getString(R.string.download)
                valueAnimator.cancel()
                valueAnimator.reverse()

             }
        }

        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 80.0f
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
        if (canvas != null) {
            can = canvas
        }
        //  set background color for button
        paint.color = context.getColor(R.color.colorPrimary)
        canvas?.drawRect(0f,0f,widthSize.toFloat(), heightSize.toFloat(), paint)

        // set text and text color for button
        paint.color = context.getColor(R.color.white)
         canvas?.drawText(buttonTextStr, widthSize/2.0f, heightSize/2.0f + 30.0f, paint)
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