package com.example.android.art.glow.drawing.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.databinding.DialogChoosePaperSizeBinding

class ChoosePaperSizeDialog(context: Context, private val onClickListener: (Int) -> Unit) :
    Dialog(context) {
    private val binding by lazy { DialogChoosePaperSizeBinding.inflate(layoutInflater) }

    private var newSize = 1

    init {
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {

            firstOption.setOnClickListener {
                newSize = 1
                firstOption.setBackgroundResource(R.drawable.background_corner_0_white)
                secondOption.setBackgroundResource(R.drawable.background_corner_0)
                thirdOption.setBackgroundResource(R.drawable.background_corner_0)
            }
            secondOption.setOnClickListener {
                newSize = 2
                secondOption.setBackgroundResource(R.drawable.background_corner_0_white)
                firstOption.setBackgroundResource(R.drawable.background_corner_0)
                thirdOption.setBackgroundResource(R.drawable.background_corner_0)
            }
            thirdOption.setOnClickListener {
                newSize = 3
                thirdOption.setBackgroundResource(R.drawable.background_corner_0_white)
                secondOption.setBackgroundResource(R.drawable.background_corner_0)
                firstOption.setBackgroundResource(R.drawable.background_corner_0)
            }

            cancelBtn.setOnClickListener {
                onClickListener(newSize)
                dismiss()
            }

            okBtn.setOnClickListener {
                onClickListener(newSize)
                dismiss()
            }
        }
    }
}