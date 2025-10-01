package com.example.android.art.glow.drawing.intro

import android.annotation.SuppressLint
import android.content.Intent
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.ads.AdsManager
import com.example.android.art.glow.drawing.ads.RemoteConfig
import com.example.android.art.glow.drawing.base.BaseActivity2
import com.example.android.art.glow.drawing.main.MainActivity
import com.example.android.art.glow.drawing.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity2<ActivityIntroBinding>(ActivityIntroBinding::inflate),
    CallbackIntro {
    private var isBackground = false
    private var isShowInter = true

    private var image2 = mutableListOf<Int>()
    private var content2 = mutableListOf<String>()
    private var dots = mutableListOf<Int>()
    private var title2 = mutableListOf<String>()


    override fun initView() {
        setContentView(binding.root)
        isShowInter = true
        intro()
        if (RemoteConfig.native_intro_4 != "0") {
            AdsManager.loadNative(this, AdsManager.NATIVE_INTRO_4)
        }
        if (RemoteConfig.banner_home == "3") {
            AdsManager.loadNative(this, AdsManager.NATIVE_HOME)
        }
        if (RemoteConfig.inter_home == "1"){
            AdsManager.loadInter(this@IntroActivity,AdsManager.INTER_HOME)
        }
    }

    private fun nextScreen() {
        startActivity(
            Intent(this@IntroActivity, MainActivity::class.java).apply {
                putExtra("fromSplash", true)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        )

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (binding.viewpager.currentItem != 0) {
            binding.viewpager.currentItem -= 1
        } else {
            super.onBackPressed()
        }
    }

    private fun addToList2(imageV: Int, title: String, textV: String, dot: Int) {
        image2.add(imageV)
        content2.add(textV)
        title2.add(title)
        dots.add(dot)
    }

    private fun intro() {
        postToList()
        binding.viewpager.adapter = IntroAdapter(this, image2, title2, content2, dots)
    }

    private fun postToList() {
        addToList2(
            R.drawable.first_intro,
            getString(R.string.first_intro_title),
            getString(R.string.first_intro_description),
            R.drawable.dot_1
        )
        if (!AdsManager.isFailNativeFullScreen && !AdsManager.isTestDevice) {
            addToList2(
                0, "", "", 0
            )
        }
        addToList2(
            R.drawable.second_intro,
            getString(R.string.second_intro_title),
            getString(R.string.second_intro_description),
            R.drawable.dot_2
        )
        if (!AdsManager.isFailNativeFullScreen2 && !AdsManager.isTestDevice) {
            addToList2(
                0, "", "", 0
            )
        }
        addToList2(
            R.drawable.third_intro,
            getString(R.string.third_intro_title),
            getString(R.string.third_intro_description),
            R.drawable.dot_3
        )
    }

    override fun onStop() {
        super.onStop()
        isBackground = true
    }

    override fun onNext(position: Int) {
        if (position == image2.size) {
            if (RemoteConfig.inter_intro != "0") {
                AdsManager.loadAndShowInter(
                    this,
                    AdsManager.INTER_INTRO, "",
                    object : AdsManager.AdListenerNew {
                        override fun onAdClosedOrFailed() {
                            nextScreen()
                        }
                    })
            } else {
                nextScreen()
            }
        } else {
            binding.viewpager.currentItem += 1
        }
    }
}