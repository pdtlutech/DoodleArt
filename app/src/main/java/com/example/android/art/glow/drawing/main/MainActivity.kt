package com.example.android.art.glow.drawing.main

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.ads.AdsManager
import com.example.android.art.glow.drawing.ads.RemoteConfig
import com.example.android.art.glow.drawing.base.BaseActivity
import com.example.android.art.glow.drawing.databinding.ActivityMainBinding
import com.example.android.art.glow.drawing.language.LanguageActivity
import com.example.android.art.glow.drawing.main.fragment.CustomPictureFragment
import com.example.android.art.glow.drawing.main.fragment.SampleFragment
import com.example.android.art.glow.drawing.utils.Common
import com.example.android.art.glow.drawing.utils.Common.gone
import com.example.android.art.glow.drawing.utils.PermissionUtils
import com.example.android.art.glow.drawing.utils.Utils

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val result by lazy {
        intent.getIntExtra("FRAGMENT_TO_SWITCH", -1)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!PermissionUtils.havePermission(this)) {
            PermissionUtils.requestPermission(this)
        }
        println("result here is $result")
        if(result == 1) {
            openFragment(CustomPictureFragment())
            displaySelectedCustomPicture()
        } else {
            openFragment(SampleFragment())
            displaySelectedSample()
        }

        initView()
        showAdsHome()
        showNativeSmallHome()
    }

    private fun initView() {
        binding.btnLanguage.setImageResource(Common.getPreLanguageflag(this))
        binding.btnLanguage.setOnClickListener {
            startActivityForResult(Intent(this, LanguageActivity::class.java), 1)
        }
        binding.apply {
            inspiration.setOnClickListener {
                openFragment(SampleFragment())
                displaySelectedSample()
            }

            sketches.setOnClickListener {
                openFragment(CustomPictureFragment())
               displaySelectedCustomPicture()
            }
        }


        if (RemoteConfig.ads_draw == "3"){
            AdsManager.loadNative(this@MainActivity,AdsManager.NATIVE_DRAW)
        }
        if (RemoteConfig.native_full_back =="1"){
            AdsManager.loadNativeFullScreen(this@MainActivity,AdsManager.NATIVE_FULL_BACK,"")
        }
    }

    private fun displaySelectedCustomPicture() {
        binding.apply {
            Utils.setGradientColorTextView(
                this@MainActivity,
                sketches,
                R.color.lightOrange,
                R.color.green
            )
            Utils.setGradientColorTextView(
                this@MainActivity,
                inspiration,
                R.color.white,
                R.color.white
            )

            sketches.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.lightGray))
            inspiration.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.transparent))
        }
    }

    private fun displaySelectedSample() {
        binding.apply {
            Utils.setGradientColorTextView(
                this@MainActivity,
                inspiration,
                R.color.lightOrange,
                R.color.green
            )
            Utils.setGradientColorTextView(
                this@MainActivity,
                sketches,
                R.color.white,
                R.color.white
            )
            inspiration.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.lightGray))
            sketches.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.transparent))
        }
    }

    private fun showAdsHome(){
        when (RemoteConfig.banner_home){
            "1"->{
                AdsManager.showAdBanner(this@MainActivity,AdsManager.BANNER_HOME,binding.frBanner,binding.line)
            }
            "2"->{
                AdsManager.showAdBannerCollapsible(this@MainActivity,AdsManager.BANNER_HOME_COLLAPSIBLE,binding.frBanner,binding.line)
            }
            "3"->{
                binding.line.gone()
                AdsManager.showNativeSmallest(this@MainActivity,AdsManager.NATIVE_HOME,binding.frBanner)
            }
            else->{
                binding.frBanner.gone()
                binding.line.gone()
            }
        }
    }

    private fun showNativeSmallHome() {
        binding.apply {
            if (RemoteConfig.native_home != "0") {
                AdsManager.loadAndShowNativeSmall(
                    this@MainActivity,
                    smallLayout,
                    AdsManager.NATIVE_SMALL_HOME
                )
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }


    companion object {
        var isChangeTheme: Boolean = false
    }
}