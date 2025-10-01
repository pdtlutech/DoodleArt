package com.example.android.art.glow.drawing.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat


object Utils {
    fun setGradientColorTextView(
        context: Context,
        textView: TextView,
        firstColor: Int,
        secondColor: Int
    ) {
        // Create the LinearGradient
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())

        val startColor = getColorString(context, firstColor)
        val endColor = getColorString(context, secondColor)

        val textShader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(
                Color.parseColor(startColor),
                Color.parseColor(endColor)
            ),  // Gradient colors
            null,  // Spread the gradient evenly
            Shader.TileMode.CLAMP // Prevent the gradient from repeating
        )

        // Set the gradient as the shader for the TextView paint
        textView.paint.shader = textShader
    }

    private fun getColorString(context: Context, value: Int): String =
        context.resources.getString(value)

    fun showVisible(showItems: List<View> = listOf(), hideItems: List<View> = listOf()) {
        if (showItems.isNotEmpty()) {
            showItems.onEach { it.visibility = View.VISIBLE }
        }
        if (hideItems.isNotEmpty()) {
            hideItems.onEach { it.visibility = View.GONE }
        }
    }

    fun hideAll(hideItems: List<View> = listOf()) {
        if (hideItems.isNotEmpty()) {
            hideItems.onEach { it.visibility = View.GONE }
        }
    }

    fun drawableToBitmap(context: Context, drawableId: Int): Bitmap? {
        val drawable: Drawable? = ContextCompat.getDrawable(context, drawableId)
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            drawable?.let {
                val bitmap = Bitmap.createBitmap(
                    it.intrinsicWidth,
                    it.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = android.graphics.Canvas(bitmap)
                it.setBounds(0, 0, canvas.width, canvas.height)
                it.draw(canvas)
                bitmap
            }
        }
    }
}