package com.example.android.art.glow.drawing.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.ads.AdsManager
import com.example.android.art.glow.drawing.ads.RemoteConfig
import com.example.android.art.glow.drawing.databinding.DialogConfirmationBinding
import com.example.android.art.glow.drawing.utils.Common.gone
import com.example.android.art.glow.drawing.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConfirmationDialog(
    private val context: Context,
    private val isDiscard: WarningType = WarningType.BACK,
    private val onClickListener: (Boolean) -> Unit
) : Dialog(context) {
    private val binding by lazy { DialogConfirmationBinding.inflate(layoutInflater) }

    init {
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private val okText by lazy {
        when (isDiscard) {
            WarningType.BACK -> context.resources.getString(R.string.back)
            WarningType.DISCARD -> context.resources.getString(R.string.save_to_sketch)
            WarningType.SAVE -> context.resources.getString(R.string.ok)
        }
    }

    private val cancelText by lazy {
        when (isDiscard) {
            WarningType.BACK -> context.resources.getString(R.string.cancel)
            WarningType.DISCARD -> context.resources.getString(R.string.discard)
            WarningType.SAVE -> context.resources.getString(R.string.no)
        }
    }

    private val title by lazy {
        when (isDiscard) {
            WarningType.BACK -> context.getString(R.string.discard_drawing)
            WarningType.DISCARD -> context.getString(R.string.discard_drawing)
            WarningType.SAVE -> context.getString(R.string.saved_successfully)
        }
    }

    private val description by lazy {
        when (isDiscard) {
            WarningType.BACK -> context.getString(R.string.undone_progress)
            WarningType.DISCARD -> context.getString(R.string.undone_progress)
            WarningType.SAVE -> context.getString(R.string.visit_sketches)
        }
    }

    private val icon by lazy {
        when (isDiscard) {
            WarningType.BACK -> R.drawable.icon_discard
            WarningType.DISCARD -> R.drawable.icon_discard
            WarningType.SAVE -> R.drawable.icon_saved
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            when (isDiscard) {
                WarningType.SAVE -> {
                    setUpSaveDisplaying()
                }
                WarningType.BACK -> {
                    setUpBackDisplayping()
                }
                else -> {
                    setUpNormalDisplaying()
                }
            }
        }

    }

    private fun setUpNormalDisplaying() {
        binding.apply {
            dialogIcon.setImageResource(icon)

            dialogIcon.visibility = View.VISIBLE
            animation.visibility = View.GONE


            dialogTitle.text = title
            dialogDescription.text = description

            okBtn.text = okText
            okBtn.isSelected = true

            cancelBtn.text = cancelText

            Utils.setGradientColorTextView(context, dialogTitle, R.color.lightOrange, R.color.green)

            okBtn.setOnClickListener {
                onClickListener(true)
                dismiss()
            }
            cancelBtn.setOnClickListener {
                onClickListener(false)
                dismiss()
            }
        }
    }

    private fun setUpBackDisplayping() {
        if(RemoteConfig.native_save == "1") {
            AdsManager.showNativeSmall(context as Activity,  AdsManager.NATIVE_SAVE, binding.frNative)
        } else {
            binding.frNative.gone()
        }
        setUpNormalDisplaying()
    }

    private fun setUpSaveDisplaying() {
            if(RemoteConfig.native_save == "1") {
                AdsManager.showNativeSmall(context as Activity,  AdsManager.NATIVE_SAVE, binding.frNative)
            } else {
                binding.frNative.gone()
            }

            binding.apply {
                animation.visibility = View.VISIBLE
                dialogIcon.visibility = View.GONE
                animation.playAnimation()
                dialogTitle.text = context.getString(R.string.progress_title)
                dialogDescription.text = context.getString(R.string.progress_description)

                okBtn.isEnabled = false
                okBtn.alpha = 0.5f
                cancelBtn.isEnabled = false
                cancelBtn.alpha = 0.5f

                CoroutineScope(Dispatchers.Main).launch {
                    okBtn.text = context.resources.getString(R.string.waiting)
                    delay(2000)
                    animation.visibility = View.GONE
                    dialogIcon.visibility = View.VISIBLE

                    okBtn.text = okText
                    okBtn.isEnabled = true
                    okBtn.alpha = 1f
                    cancelBtn.isEnabled = true
                    cancelBtn.alpha = 1f

                    dialogTitle.text = title
                    dialogDescription.text = description
                }

                okBtn.setOnClickListener {
                    onClickListener(true)
                    dismiss()
                }
                cancelBtn.setOnClickListener {
                    onClickListener(false)
                    dismiss()
                }

        }
    }
}

enum class WarningType {
    BACK,
    DISCARD,
    SAVE
}