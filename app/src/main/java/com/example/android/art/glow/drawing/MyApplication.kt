package com.example.android.art.glow.drawing

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.admob.max.dktlibrary.adjust.AdjustUtils
import com.admob.max.dktlibrary.application.AdsApplication
import com.example.android.art.glow.drawing.utils.Common

class MyApplication : AdsApplication(), ActivityLifecycleCallbacks {
    override fun onCreateApplication() {
        AdjustUtils.initAdjust(this, getString(R.string.adjust_token_key), false)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Common.setLocale(this@MyApplication, Common.getPreLanguage(this@MyApplication))
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }


}