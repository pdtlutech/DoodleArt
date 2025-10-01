package com.example.android.art.glow.drawing.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.android.art.glow.drawing.ads.AdsManager
import com.example.android.art.glow.drawing.ads.RemoteConfig
import com.example.android.art.glow.drawing.base.BaseActivity2
import com.example.android.art.glow.drawing.custom.DrawActivity
import com.example.android.art.glow.drawing.databinding.ActivityNativeFullBinding
import com.example.android.art.glow.drawing.inspiration.InspirationActivity
import com.example.android.art.glow.drawing.language.LanguageActivity
import com.example.android.art.glow.drawing.main.MainActivity
import com.example.android.art.glow.drawing.utils.Common.gone
import com.example.android.art.glow.drawing.utils.Common.visible

class NativeFullActivity : BaseActivity2<ActivityNativeFullBinding>(ActivityNativeFullBinding :: inflate ) {
    private val fromCustom by lazy { intent.getBooleanExtra("fromCustom", false) }
    private val toDrawPicture by lazy { intent.getBooleanExtra("toDrawPicture", false) }
    override fun initView() {

        Handler().postDelayed({
            if (RemoteConfig.native_full_home == "1") {
                AdsManager.showNativeFullScreen(this@NativeFullActivity,AdsManager.NATIVE_FULL_SPLASH,binding.flNative)
            }
            if (RemoteConfig.native_full_back == "1" && fromCustom){
                AdsManager.showNativeFullScreen(this@NativeFullActivity,AdsManager.NATIVE_FULL_BACK,binding.flNative)
            }
        },200)

        var count = 3
        val handler = Handler(Looper.getMainLooper())

        val countdownRunnable = object : Runnable {
            override fun run() {
                if (count > 0) {
                    binding.tvTime.text = "$count"
                    binding.tvTime.visible()
                    count--
                    handler.postDelayed(this, 1000)
                } else {
                    binding.tvTime.gone()
                    binding.btnClose.visible()
                }
            }
        }
        handler.post(countdownRunnable)

        binding.btnClose.setOnClickListener {
            when {
                fromCustom -> {
                    startActivity(Intent(this@NativeFullActivity, MainActivity::class.java))
                    finish()
                }

                toDrawPicture -> {
                    startActivity(Intent(this@NativeFullActivity, DrawActivity::class.java))
                    finish()
                }

                inspirationScreen == 0 -> {
                    startActivity(Intent(this@NativeFullActivity, InspirationActivity::class.java))
                    finish()
                }

                else -> {
                    nextActivity()
                }
            }
        }
    }
    private fun nextActivity() {
        val intent = Intent(this, LanguageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("isSplash", true)
        startActivity(intent)
    }

    companion object {
        var inspirationScreen = -1
    }
}