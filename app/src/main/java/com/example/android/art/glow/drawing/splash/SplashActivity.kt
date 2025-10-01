package com.example.android.art.glow.drawing.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.android.art.glow.drawing.base.BaseActivity2
import com.example.android.art.glow.drawing.databinding.ActivitySplashBinding
import com.example.android.art.glow.drawing.language.LanguageActivity
import com.example.android.art.glow.drawing.utils.Common
import com.example.android.art.glow.drawing.utils.Common.countDone
import kotlin.system.exitProcess

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity2<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun initView() {
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action == Intent.ACTION_MAIN
        ) {
            finish()
            return
        }
        Common.setPosition(this, -1)
        var countOpenApp = Common.getCountOpenApp(this)
        countOpenApp++
        Common.setCountOpenApp(this, countOpenApp)
        countDone = 0


        binding.tvStart.visibility = View.INVISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                nextActivity()
            }, 3000)

    }


    private fun nextActivity() {
        val intent = Intent(this, LanguageActivity::class.java)
        intent.putExtra("isSplash", true)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitProcess(-1)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        moveTaskToBack(true)
        exitProcess(-1)
    }

    override fun onResume() {
        super.onResume()

    }
}