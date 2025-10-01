package com.example.android.art.glow.drawing.inspiration

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.example.android.art.glow.drawing.ads.AdsManager
import com.example.android.art.glow.drawing.ads.RemoteConfig
import com.example.android.art.glow.drawing.base.BaseActivity
import com.example.android.art.glow.drawing.custom.DrawActivity
import com.example.android.art.glow.drawing.databinding.ActivityInspirationBinding
import com.example.android.art.glow.drawing.main.MainActivity
import com.example.android.art.glow.drawing.splash.NativeFullActivity.Companion.inspirationScreen
import com.example.android.art.glow.drawing.utils.Common.gone
import com.example.android.art.glow.drawing.utils.Constant
import com.example.android.art.glow.drawing.utils.FileUtils

class InspirationActivity :
    BaseActivity<ActivityInspirationBinding>(ActivityInspirationBinding::inflate) {

    private val index by lazy {
        intent.getIntExtra("index", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inspirationScreen = -1

        binding.apply {
            val resourceId = Constant.allSamples[index]
            content.setImageResource(resourceId)

            backBtn.setOnClickListener {
                startActivity(Intent(this@InspirationActivity, MainActivity::class.java))
            }

            downloadBtn.setOnClickListener {
                val bitmap = BitmapFactory.decodeResource(resources, resourceId)
                FileUtils.downloadImage(bitmap, this@InspirationActivity)
            }

            shareBtn.setOnClickListener {

            }

            drawNow.setOnClickListener {
                if (RemoteConfig.inter_home == "1"){
                    AdsManager.showAdInter(this@InspirationActivity,
                        AdsManager.INTER_HOME,"INTER_HOME",object : AdsManager.AdListenerNew{
                            override fun onAdClosedOrFailed() {
                                startActivity(Intent(this@InspirationActivity, DrawActivity::class.java))
                            }
                        },true)
                }else{
                    startActivity(Intent(this@InspirationActivity, DrawActivity::class.java))
                }
            }

            showAdsCustom()
            showPreviewAds()
        }
    }

    private fun showPreviewAds() {
        binding.apply {
            if (RemoteConfig.native_preview != "0") {
                AdsManager.loadAndShowNativeSmall(
                    this@InspirationActivity,
                    binding.showNativePreview,
                    AdsManager.NATIVE_PREVIEW
                )
            }
        }
    }

    private fun showAdsCustom() {
        when (RemoteConfig.ads_preview) {
            "1" -> {
                AdsManager.showAdBanner(
                    this@InspirationActivity,
                    AdsManager.BANNER_PREVIEW,
                    binding.frBanner,
                    binding.line
                )
            }

            "2" -> {
                AdsManager.showAdBannerCollapsible(
                    this@InspirationActivity,
                    AdsManager.BANNER_PREVIEW_COLLAPSIBLE,
                    binding.frBanner,
                    binding.line
                )
            }

            "3" -> {
                binding.line.gone()
                AdsManager.showNativeSmallest(
                    this@InspirationActivity,
                    AdsManager.NATIVE_BOTTOM_PREVIEW,
                    binding.frBanner
                )
            }

            else -> {
                binding.frBanner.gone()
                binding.line.gone()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@InspirationActivity, MainActivity::class.java))
    }

    companion object {
        var inspirationIndex = -1
    }
}