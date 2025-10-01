package com.example.android.art.glow.drawing.utils

import android.app.Activity
import android.app.Application.MODE_MULTI_PROCESS
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.admob.max.dktlibrary.AppOpenManager
import com.example.ratingdialog.RatingDialog
//import com.admob.max.dktlibrary.AppOpenManager
import com.example.android.art.glow.drawing.R
//import com.example.ratingdialog.RatingDialog
import java.util.Locale

object Common {
    var countDone = 0
    var isShowRate = false
    var showTime = 0

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.inVisible() {
        visibility = View.INVISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun Context.toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
//
//    fun scheduleNotification(context: Context, time: Long) {
//        val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
//            .setInitialDelay(time, TimeUnit.SECONDS)
//            .build()
//
//        WorkManager.getInstance(context).enqueue(notificationRequest)
//    }

    fun getLang(mContext: Context): String? {
        val preferences =
            mContext.getSharedPreferences(mContext.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getString("KEY_LANG", "en")
    }

    fun setLang(context: Context, open: String?) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putString("KEY_LANG", open).apply()
    }

    fun getPreLanguageflag(mContext: Context): Int {
        val preferences = mContext.getSharedPreferences(mContext.packageName, MODE_MULTI_PROCESS)
        return preferences.getInt("KEY_FLAG", R.drawable.ic_english)
    }

    fun setPreLanguageflag(context: Context, flag: Int) {
        val preferences = context.getSharedPreferences(context.packageName, MODE_MULTI_PROCESS)
        preferences.edit().putInt("KEY_FLAG", flag).apply()
    }

    fun getPreLanguage(mContext: Context): String {
        val preferences = mContext.getSharedPreferences(mContext.packageName, MODE_MULTI_PROCESS)
        return preferences.getString("KEY_LANGUAGE", "en").toString()
    }

    fun setPreLanguage(context: Context, language: String?) {
        if (TextUtils.isEmpty(language)) return
        val preferences = context.getSharedPreferences(context.packageName, MODE_MULTI_PROCESS)
        preferences.edit().putString("KEY_LANGUAGE", language).apply()
    }

    fun setPosition(context: Context, open: Int) {
        val preferences = context.getSharedPreferences(context.packageName, MODE_MULTI_PROCESS)
        preferences.edit().putInt("KEY_POSITION", open).apply()
    }

    fun getPosition(mContext: Context): Int {
        val preferences = mContext.getSharedPreferences(mContext.packageName, MODE_MULTI_PROCESS)
        return preferences.getInt("KEY_POSITION", -1)
    }

    fun setLocale(context: Context, lang: String?) {
        val myLocale = lang?.let { Locale(it) }
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(myLocale)
        res.updateConfiguration(conf, dm)
    }

    fun setRemoteKey(context: Context, key: String, value: String) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putString(key, value).apply()
    }

    fun getRemoteKey(context: Context, key: String, default: String): String {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getString(key, default).toString()
    }

    fun showDialogRate(context: Activity) {
        val ratingDialog = RatingDialog.Builder(context)
            .session(1)
            .date(1)
            .ignoreRated(false)
            .setNameApp(context.getString(R.string.app_name))
            .setIcon(R.drawable.icon_app_logo)
            .setEmail("khanhvangirl0921@gmail.com")
            .setOnlickRate { _ ->
                AppOpenManager.getInstance().disableAppResumeWithActivity(context::class.java)
            }
            .isShowButtonLater(true)
            .isClickLaterDismiss(true)
            .setTextButtonLater("Maybe later")
            .setOnlickMaybeLate {

            }
            .ratingButtonColor(Color.parseColor("#004BBB"))
            .build()
        ratingDialog.setCanceledOnTouchOutside(false)
        ratingDialog.setOnDismissListener {

        }
        if (!ratingDialog.isShowing) {
            ratingDialog.show()
        }
    }

    fun logEventFirebase(context: Context, eventName: String) {
//        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
//        val bundle = Bundle()
//        bundle.putString("onEvent", context.javaClass.simpleName)
//        firebaseAnalytics.logEvent(eventName + "_" + BuildConfig.VERSION_CODE, bundle)
//        Log.d("===Event", eventName + "_" + BuildConfig.VERSION_CODE)
    }

    fun getCountOpenApp(mContext: Context): Int {
        val preferences = mContext.getSharedPreferences(
            mContext.packageName,
            Context.MODE_MULTI_PROCESS
        )
        return preferences.getInt("KEY_CountOpenApp", 0)
    }

    fun setCountOpenApp(context: Context, flag: Int) {
        val preferences = context.getSharedPreferences(
            context.packageName,
            Context.MODE_MULTI_PROCESS
        )
        preferences.edit().putInt("KEY_CountOpenApp", flag).apply()
    }

    fun getCountRate(mContext: Context): Int {
        val preferences = mContext.getSharedPreferences(
            mContext.packageName,
            Context.MODE_MULTI_PROCESS
        )
        return preferences.getInt("KEY_CountRate", 0)
    }

    fun setCountRate(context: Context, flag: Int) {
        val preferences = context.getSharedPreferences(
            context.packageName,
            Context.MODE_MULTI_PROCESS
        )
        preferences.edit().putInt("KEY_CountRate", flag).apply()
    }
}