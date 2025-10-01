package com.example.android.art.glow.drawing.custom

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.ads.AdsManager
import com.example.android.art.glow.drawing.ads.RemoteConfig
import com.example.android.art.glow.drawing.base.BaseActivity
import com.example.android.art.glow.drawing.custom.adapter.PickupIconAdapter
import com.example.android.art.glow.drawing.custom.adapter.PickupLineAdapter
import com.example.android.art.glow.drawing.custom_view.GlowMandalaView
import com.example.android.art.glow.drawing.custom_view.StrokeStyle
import com.example.android.art.glow.drawing.databinding.ActivityCustomBinding
import com.example.android.art.glow.drawing.dialog.ChoosePaperSizeDialog
import com.example.android.art.glow.drawing.dialog.ConfirmationDialog
import com.example.android.art.glow.drawing.dialog.WarningType
import com.example.android.art.glow.drawing.main.MainActivity
import com.example.android.art.glow.drawing.splash.NativeFullActivity
import com.example.android.art.glow.drawing.utils.Common
import com.example.android.art.glow.drawing.utils.Common.gone
import com.example.android.art.glow.drawing.utils.Constant
import com.example.android.art.glow.drawing.utils.FileUtils
import com.example.android.art.glow.drawing.utils.Utils

@SuppressLint("UseCompatLoadingForDrawables")
class DrawActivity : BaseActivity<ActivityCustomBinding>(ActivityCustomBinding::inflate) {

    private var selectedEraserColor = 0
    private var selectedColor = 0
    private var currentTextSize = 0
    private val allItems by lazy {
        listOf(
            binding.pickBackground,
            binding.pickLines,
            binding.pickStickers,
            binding.pickSymmetricArts,
            binding.arrowPickBackground,
            binding.arrowPickLines,
            binding.arrowPickStickers,
            binding.arrowPickSymmetricArts,
        )
    }

    private var isEraser = true
    // Variable to store the last saved value
    private var previousProgress = 30
    private var firstClick = true

    private val allLinesAdapter: PickupLineAdapter by lazy {
        PickupLineAdapter { color, isCustom ->
            isEraser = true
            binding.eraser.setImageResource(R.drawable.icon_eraser_disabled)
            binding.drawingView.setEraserMode(false, selectedEraserColor)
            println("allLinesAdapter: $color and $isCustom")
            val nowColor = if(isCustom) color else resources.getColor(color)
            binding.drawingView.setGlowColor(nowColor)
            binding.drawingView.setSelectedSvgResource(0)
            selectedColor = nowColor
            hideTheButton()
        }
    }
    private val allSymmetricAdapter: PickupIconAdapter by lazy {
        PickupIconAdapter { item ->
            isEraser = true
            binding.eraser.setImageResource(R.drawable.icon_eraser_disabled)
            binding.drawingView.setEraserMode(false, selectedEraserColor)
            binding.drawingView.setSymmetry(item)
            binding.drawingView.setSelectedSvgResource(0)
            binding.drawingView.setGlowColor(selectedColor)
            hideTheButton()
        }
    }

    private val allStickerAdapter: PickupIconAdapter by lazy {
        PickupIconAdapter { item ->
            isEraser = true
            binding.eraser.setImageResource(R.drawable.icon_eraser_disabled)
            binding.drawingView.setEraserMode(false, selectedEraserColor)
            binding.drawingView.setSelectedSvgResource(Constant.allStickers[item])
            hideTheButton()
        }
    }

    private fun nextFullScreen(){
        val intent = Intent(this@DrawActivity, NativeFullActivity::class.java)
        intent.putExtra("fromCustom",true)
        startActivity(intent)
    }

    private fun nextActivity(){
        val dialog = ConfirmationDialog(this@DrawActivity, isDiscard = WarningType.BACK) { result ->
            if(result) {
              navigateToMain()

            }
        }
        dialog.show()

    }

    private fun navigateToMain() {
        if (RemoteConfig.inter_back != "0") {
            AdsManager.loadAndShowInterSP(this@DrawActivity,AdsManager.INTER_BACK,"INTER_BACK",object :AdsManager.AdListenerWithNative{
                override fun onAdClosedOrFailed() {
                    startActivity(Intent(this@DrawActivity, MainActivity::class.java))
                }

                override fun onAdClosedOrFailedWithNative() {
                    if (RemoteConfig.native_full_back!="0"){
                        nextFullScreen()
                    }else{
                        startActivity(Intent(this@DrawActivity, MainActivity::class.java))
                    }
                }

            })
        } else {
            startActivity(Intent(this@DrawActivity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }
    }

    override fun onBackPressed() {
      navigateToMain()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var countRate = Common.getCountRate(this@DrawActivity)
        countRate++
        Common.setCountRate(this@DrawActivity,countRate)

        if (Common.getCountRate(this@DrawActivity)==2){
            Common.showDialogRate(this@DrawActivity)
        }
        selectDrawerSize()
        Utils.hideAll(allItems)
        resetAllOptions()
        binding.drawingView.setSelectedSvgResource(0)
        binding.drawingView.setGlowColor(isDefault = true)
        selectedEraserColor = resources.getColor(R.color.customBlack)
        binding.drawingView.setEraserMode(false)
        selectedColor = Color.RED

        binding.apply {
            drawingView.setOnClickListener {
                hideTheButton()
            }
            constraintlayout.setOnClickListener {
                hideTheButton()
            }

            root.setOnClickListener {
                hideTheButton()
            }

            home.setOnClickListener {
                nextActivity()
            }

            newFile.setOnClickListener {
                val dialog = ConfirmationDialog(this@DrawActivity, isDiscard = WarningType.DISCARD) { result ->
                    if (!result) {
                        binding.drawingView.clearCanvas()
                    } else {
                        val bitmap = getBitmapFromCustomView(drawingView)
                        FileUtils.downloadImage(bitmap, this@DrawActivity)
                    }
                }

                dialog.show()
            }

            save.setOnClickListener {
                displaySavingDialog()
            }

            if (symmetricArtIndex != -1) {
                drawingView.setSymmetry(Constant.allSymmetricValue[symmetricArtIndex])
            } else {
                drawingView.setSymmetry(1)
            }

            drawingView.setStrokeSize(30f)
            drawingView.setStrokeStyle(StrokeStyle.SOLID)

            eraser.setOnClickListener {
                drawingView.setSelectedSvgResource(0)
                setEraser()
            }

            redo.setOnClickListener {
                drawingView.redo()
            }
            undo.setOnClickListener {
                drawingView.undo()
            }

            drawingView.setOnTouchDrawListener(object : GlowMandalaView.OnTouchDrawListener {
                override fun onUserTouch(x: Float, y: Float, action: Int) {
                    Utils.hideAll(allItems)
                }
            })
        }

        initView()

        initFunction()
        showAdsCustom()

        if(RemoteConfig.native_save == "1") {
            AdsManager.loadNative(this, AdsManager.NATIVE_SAVE)
        }
    }

    private fun displaySavingDialog() {
        val dialog = ConfirmationDialog(this@DrawActivity, isDiscard = WarningType.SAVE) { result ->
            val bitmap = getBitmapFromCustomView(binding.drawingView)
            FileUtils.downloadImage(bitmap, this@DrawActivity)

            if(result) {
                val intent = Intent(this@DrawActivity, MainActivity::class.java)
                intent.putExtra("FRAGMENT_TO_SWITCH", 1)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
        dialog.show()
    }

    private fun setEraser(){
        binding.drawingView.setEraserMode(isEraser, selectedEraserColor)
        if (!isEraser){
            binding.drawingView.setGlowColor(selectedColor)
        }
        if(!isEraser) {
            binding.eraser.setImageResource(R.drawable.icon_eraser_disabled)
        } else {
            binding.eraser.setImageResource(R.drawable.icon_eraser)
        }

        isEraser = !isEraser
    }

    private fun showAdsCustom(){
        when (RemoteConfig.ads_draw){
            "1"->{
                AdsManager.showAdBanner(
                    this@DrawActivity,
                    AdsManager.BANNER_DRAW,
                    binding.frBanner,
                    binding.line
                )
            }
            "2"->{
                AdsManager.showAdBannerCollapsible(this@DrawActivity,AdsManager.BANNER_DRAW_COLLAPSIBLE,binding.frBanner,binding.line)
            }
            "3"->{
                binding.line.gone()
                AdsManager.showNativeSmallest(this@DrawActivity,AdsManager.NATIVE_DRAW,binding.frBanner)
            }
            else->{
                binding.frBanner.gone()
                binding.line.gone()
            }
        }
    }
    private fun selectDrawerSize() {

        val dialog = ChoosePaperSizeDialog(this) { option ->
            when (option) {
                1 -> {
                    setRatioDimension("9:16")
                }

                2 -> {
                    setRatioDimension("1:1")
                }

                3 -> {
                    setRatioDimension("4:5")
                }

                else -> {
                    //do nothing
                }
            }

        }
        dialog.show()
    }

    private fun setRatioDimension(s: String) {
        println("setRatioDimension: $s")

        val constraintLayout = binding.constraintlayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.setDimensionRatio(binding.drawingView.id, s)
        constraintSet.applyTo(constraintLayout)
    }

    private fun resetAllOptions() {
        backgroundIndex = -1
        lineIndex = -1
        stickerIndex = -1
        symmetricArtIndex = -1
    }

    private fun initView() {
        loadAllBackgrounds()
        loadAllSymmetric()
        loadAllLines()
        loadAllStickers()
    }

    private fun loadAllSymmetric() {
        binding.allSymmetricArts.adapter = allSymmetricAdapter
        allSymmetricAdapter.submitList(Constant.allSymmetricArts, symmetricArtIndex, true)
        binding.allSymmetricArts.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadAllLines() {
        binding.allPickLines.adapter = allLinesAdapter
        allLinesAdapter.submitList(Constant.allLines, lineIndex)
        binding.allPickLines.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadAllBackgrounds() {
        binding.sbProgress.progress = previousProgress
        binding.sbProgress.post {
            binding.currentValueTxt.text = "$previousProgress"
            updateThumbPosition()
        }

        binding.sbProgress.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    previousProgress = progress
                    firstClick = false
                }
                val value = seekBar.progress
                currentTextSize = value
                binding.currentValueTxt.text = "$value"
                updateThumbPosition()

                Log.d("Pos", "currentValueTxt X: ${binding.currentValueTxt.x}, currentValueTxt Y: ${binding.currentValueTxt.y}")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                binding.drawingView.setGlowColor(selectedColor, currentTextSize)
                hideTheButton()
            }

        })
        binding.sbProgress.progress = previousProgress
        binding.currentValueTxt.text = "$previousProgress"
        binding.sbProgress.post {
           updateThumbPosition()
       }
    }

    // Helper method to update currentValueTxt position above the thumb
    private fun updateThumbPosition() {
        val seekBar = binding.sbProgress


        if(firstClick) {
            binding.currentValueTxt.x = 158f
            binding.currentValueTxt.y = 0f

        } else  {
            val thumbX = (seekBar.width.toFloat() / seekBar.max) * seekBar.progress

            binding.currentValueTxt.x = if(binding.sbProgress.progress <= 1) {
               0f
            } else {
                thumbX - (binding.currentValueTxt.width / 2) - 5
            }
            binding.currentValueTxt.y = seekBar.y - binding.currentValueTxt.height
        }
        println("updateThumbPosition: ${binding.currentValueTxt.x} and ${binding.currentValueTxt.y}")

    }

    private fun loadAllStickers() {
        binding.allPickStickers.adapter = allStickerAdapter
        allStickerAdapter.submitList(Constant.allStickers, stickerIndex, false)
        binding.allPickStickers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initFunction() {
        binding.apply {
            background.setOnClickListener {
                loadAllBackgrounds()
                Utils.showVisible(
                    listOf(arrowPickBackground, pickBackground),
                    listOf(
                        arrowPickSymmetricArts,
                        arrowPickLines,
                        arrowPickStickers,
                        pickSymmetricArts,
                        pickLines,
                        pickStickers,
                    )
                )
            }

            symmetricArt.setOnClickListener {
                loadAllSymmetric()
                Utils.showVisible(
                    listOf(arrowPickSymmetricArts, pickSymmetricArts),
                    listOf(
                        arrowPickBackground,
                        arrowPickLines,
                        arrowPickStickers,
                        pickBackground,
                        pickLines,
                        pickStickers
                    )
                )
            }

            lines.setOnClickListener {
                loadAllLines()
                Utils.showVisible(
                    listOf(arrowPickLines, pickLines),
                    listOf(
                        arrowPickBackground,
                        arrowPickSymmetricArts,
                        arrowPickStickers,
                        pickBackground,
                        pickSymmetricArts,
                        pickStickers,
                    )
                )
            }


            sticker.setOnClickListener {
                loadAllStickers()
                Utils.showVisible(
                    listOf(arrowPickStickers, pickStickers),
                    listOf(
                        arrowPickBackground,
                        arrowPickSymmetricArts,
                        arrowPickLines,
                        pickBackground,
                        pickSymmetricArts,
                        pickLines,
                    )
                )
            }
        }
    }

    private fun hideTheButton() {
        Utils.hideAll(allItems)
    }

    companion object {
        var backgroundIndex = -1
        var symmetricArtIndex = -1
        var lineIndex = -1
        var stickerIndex = -1

        fun getBitmapFromCustomView(customView: View): Bitmap {
            // Create a bitmap with the width and height of the custom view
            val bitmap = Bitmap.createBitmap(customView.width, customView.height, Bitmap.Config.ARGB_8888)

            // Create a canvas to draw onto the bitmap
            val canvas = Canvas(bitmap)

            // Draw the custom view content on the canvas
            customView.draw(canvas)

            return bitmap
        }
    }

}