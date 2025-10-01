package com.example.android.art.glow.drawing.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import com.example.android.art.glow.drawing.databinding.DialogColorPickerBinding

class CustomColorPickerDialog (context: Context, private val onClickListener: (Int) -> Unit) : Dialog(context) {
    private val binding by lazy { DialogColorPickerBinding.inflate(layoutInflater) }

    private var newColor = Color.BLACK

    init {
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            colorPickerView.setOnColorChangeEndListener { currentColor ->
                selectedColor.backgroundTintList =  ColorStateList.valueOf(currentColor)
                newColor = currentColor
            }

            hueSlider.setOnHueChangeEndListener { _, argbColor ->
                selectedColor.backgroundTintList =  ColorStateList.valueOf(argbColor)
                colorPickerView.color = argbColor
                newColor = argbColor
            }


            cancelBtn.setOnClickListener {
                dismiss()
            }

            okBtn.setOnClickListener {
                onClickListener(newColor)
                dismiss()
            }
        }
    }
}