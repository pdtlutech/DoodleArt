package com.example.android.art.glow.drawing.ads

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.utils.Common
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object RemoteConfig {
    var banner_splash = "1"
    var ads_splash = "2"
    var native_language = "1"
    var native_language_small = "1"
    var native_intro_1 = "1"
    var native_full_intro_2 = "1"
    var native_full_intro_3 = "1"
    var native_intro_4 = "1"
    var inter_intro = "1"
    var banner_home = "3"
    var inter_home = "1"
    var inter_home_count = "2"
    var inter_back = "1"
    var inter_back_count = "2"
    var native_full_back = "1"
    var onresume ="1"
    var ads_draw = "3"
    var native_save = "1"

    var native_full_home = "1"
    var native_home = "1"
    var native_preview = "1"
    var ads_preview = "1"

    var enable_ads = "1"
    var check_test_ad = "1"


    fun initRemoteConfig(completeListener: CompleteListener) {
        val mFirebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings: FirebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default)
        mFirebaseRemoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                mFirebaseRemoteConfig.activate().addOnCompleteListener {
                    completeListener.onComplete()
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                completeListener.onComplete()
            }
        })
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            Handler(Looper.getMainLooper()).postDelayed({
                completeListener.onComplete()
            }, 2000)
        }
    }

    fun getValueAbTest(context: Context, key: String): String {
        val mFirebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        Common.setRemoteKey(context, key, mFirebaseRemoteConfig.getString(key))
        Log.d("TAG======", "getValueAbTest: $key ${mFirebaseRemoteConfig.getString(key)}")
        return mFirebaseRemoteConfig.getString(key)
    }


    interface CompleteListener {
        fun onComplete()
    }
}