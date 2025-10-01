package com.example.android.art.glow.drawing.ads

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.admob.max.dktlibrary.AdmobUtils
import com.admob.max.dktlibrary.AdmobUtils.NativeAdCallbackNew
import com.admob.max.dktlibrary.AppOpenManager
import com.admob.max.dktlibrary.CollapsibleBanner
import com.admob.max.dktlibrary.GoogleEBanner
import com.admob.max.dktlibrary.GoogleENative
import com.admob.max.dktlibrary.utils.admod.BannerHolderAdmob
import com.admob.max.dktlibrary.utils.admod.InterHolderAdmob
import com.admob.max.dktlibrary.utils.admod.NativeHolderAdmob
import com.admob.max.dktlibrary.utils.admod.RewardedInterstitialHolderAdmob
import com.admob.max.dktlibrary.utils.admod.callback.AdCallBackInterLoad
import com.admob.max.dktlibrary.utils.admod.callback.AdsInterCallBack
import com.admob.max.dktlibrary.utils.admod.callback.NativeAdmobCallback
import com.admob.max.dktlibrary.utils.admod.callback.NativeFullScreenCallBack
import com.admob.max.dktlibrary.utils.admod.callback.RewardAdCallback
import com.example.android.art.glow.drawing.R
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MediaAspectRatio
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object AdsManager {
    var timeInter = 0L
    var countInter = 0
    var isDebug = true
    var countClickHome = 0
    var countClickBack= 0
    var countClickTry = 0

    val BANNER_SPLASH = "ca-app-pub-6457899622273702/5054735727"
    const val AOA_SPLASH = "ca-app-pub-6457899622273702/4109852549"
    val INTER_SPLASH = InterHolderAdmob("ca-app-pub-6457899622273702/8793426919")
    val NATIVE_FULL_SPLASH = NativeHolderAdmob("ca-app-pub-6457899622273702/4694399809")
    val NATIVE_LANGUAGE_1 = NativeHolderAdmob("ca-app-pub-6457899622273702/7480345242")
    val NATIVE_LANGUAGE_2 = NativeHolderAdmob("ca-app-pub-6457899622273702/3741654054")
    val NATIVE_SMALL_LANGUAGE = NativeHolderAdmob("ca-app-pub-6457899622273702/3112465642")
    val NATIVE_INTRO_1 = NativeHolderAdmob("ca-app-pub-6457899622273702/8798539611")
    val NATIVE_FULLSCREEN_2 = NativeHolderAdmob("ca-app-pub-6457899622273702/5854604085")
    val NATIVE_FULLSCREEN_3 = NativeHolderAdmob("ca-app-pub-6457899622273702/8173220638")
    val NATIVE_INTRO_4 = NativeHolderAdmob("ca-app-pub-6457899622273702/1115490716")
    val INTER_INTRO = InterHolderAdmob("ca-app-pub-6457899622273702/5812810865")

    val BANNER_HOME = "ca-app-pub-6457899622273702/2729322262"
    val BANNER_HOME_COLLAPSIBLE = BannerHolderAdmob("ca-app-pub-6457899622273702/9728477903")
    val NATIVE_HOME = NativeHolderAdmob("ca-app-pub-6457899622273702/5984674013")
    val INTER_HOME = InterHolderAdmob("ca-app-pub-6457899622273702/6937419765")
    val INTER_BACK = InterHolderAdmob("ca-app-pub-6457899622273702/9923919029")
    val NATIVE_FULL_BACK = NativeHolderAdmob("ca-app-pub-6457899622273702/1227171355")

    val ONRESUME = "ca-app-pub-6457899622273702/6562785753"

    val BANNER_DRAW = "ca-app-pub-6457899622273702/5624338097"
    val BANNER_DRAW_COLLAPSIBLE = BannerHolderAdmob("ca-app-pub-6457899622273702/5163913912")
    val NATIVE_DRAW = NativeHolderAdmob("ca-app-pub-6457899622273702/2995075838")

    val NATIVE_SAVE = NativeHolderAdmob("ca-app-pub-6457899622273702/6840398606")

    val NATIVE_FULL = NativeHolderAdmob("ca-app-pub-6457899622273702/7345822729")
    val NATIVE_SMALL_HOME = NativeHolderAdmob("ca-app-pub-6457899622273702/6032741059")
    val NATIVE_PREVIEW = NativeHolderAdmob("ca-app-pub-6457899622273702/4719659388")

    val BANNER_PREVIEW = "ca-app-pub-6457899622273702/3823727724"
    val BANNER_PREVIEW_COLLAPSIBLE = BannerHolderAdmob("ca-app-pub-6457899622273702/3294897072")
    val NATIVE_BOTTOM_PREVIEW = NativeHolderAdmob("ca-app-pub-6457899622273702/1711186816")

    var isTestDevice = false

    fun checkAdsTest(ad: NativeAd?) {
//        if (RemoteConfig.check_test_ad != "1") {
//            isTestDevice = false
//        } else {
//            try {
//                val testAdResponse = ad?.headline.toString().replace(" ", "").split(":")[0]
//                Log.d("===Native", ad?.headline.toString().replace(" ", "").split(":")[0])
//                val testAdResponses = arrayOf(
//                    "TestAd",
//                    "Anunciodeprueba",
//                    "Annoncetest",
//                    "테스트광고",
//                    "Annuncioditesto",
//                    "Testanzeige",
//                    "TesIklan",
//                    "Anúnciodeteste",
//                    "Тестовоеобъявление",
//                    "পরীক্ষামূলকবিজ্ঞাপন",
//                    "जाँचविज्ञापन",
//                    "إعلانتجريبي",
//                    "Quảngcáothửnghiệm"
//                )
//                isTestDevice = testAdResponses.contains(testAdResponse)
//            } catch (_: Exception) {
//                isTestDevice = true
//                Log.d("===Native", "Error")
//            }
//        }
    }

    fun showNativeFullScreen(
        context: Context,
        nativeHolder: NativeHolderAdmob,
        view: ViewGroup
    ) {
        if (isTestDevice) {
            view.visibility = View.GONE
            return
        }
        if (!AdmobUtils.isNetworkConnected(context) || RemoteConfig.enable_ads == "0") {
            view.visibility = View.GONE
            return
        }
        AdmobUtils.showNativeFullScreenAdsWithLayout(
            context as Activity,
            nativeHolder,
            view,
            R.layout.ad_template_native_fullscreen,
            object :
                AdmobUtils.AdsNativeCallBackAdmod {
                override fun NativeFailed(massage: String) {
                }

                override fun NativeLoaded() {
                    view.visibility = View.VISIBLE
                }

                override fun onPaid(adValue: AdValue?, adUnitAds: String?) {
                    adValue?.let { postRevenueAdjust(it, context) }
                }
            })
    }


    fun loadAndShowNativeFullScreen(
        activity: Activity,
        viewGroup: ViewGroup,
        holder: NativeHolderAdmob,
    ) {
        if (isTestDevice) {
            viewGroup.visibility = View.GONE
            return
        }
        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            viewGroup.visibility = View.GONE
            return
        }
        AdmobUtils.loadAndShowNativeFullScreen(
            activity,
            holder.ads,
            viewGroup,
            R.layout.ad_template_native_fullscreen,
            MediaAspectRatio.SQUARE,
            object : NativeFullScreenCallBack {
                override fun onLoadFailed() {
                    viewGroup.visibility = View.GONE
                }

                override fun onLoaded(nativeAd: NativeAd) {
                }
            })
    }

    fun loadAndShowReward(
        activity: Activity,
        adsEnum: RewardedInterstitialHolderAdmob,
        callback: AdListenerReward,
    ) {
        var isEarn = false

        if (isTestDevice) {
            callback.onAdClose(true)
            return
        }

        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            callback.onAdClose(true)
            return
        }
        AdmobUtils.loadAndShowAdRewardWithCallback(
            activity,
            adsEnum.ads,
            object : RewardAdCallback {
                override fun onAdClosed() {
                    callback.onAdClose(isEarn)
                }

                override fun onAdShowed() {
                    Handler(Looper.getMainLooper()).postDelayed({
                        try {
                            AdmobUtils.dismissAdDialog()
                        } catch (_: Exception) {

                        }
                    }, 200)
                }

                override fun onAdFail(p0: String?) {

                }

                override fun onEarned() {
                    isEarn = true
                }

                override fun onPaid(p0: AdValue?, p1: String?) {
                    p0?.let { AdImpressionFacebookSDK(activity, it) }
                }

            },
            true
        )
    }

    interface AdListenerReward {
        fun onAdClose(isEarned: Boolean)
    }

    fun AdImpressionFacebookSDK(context: Context, ad: AdValue) {
        val logger = AppEventsLogger.newLogger(context)
        val params = Bundle()
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, ad.currencyCode)
        logger.logEvent(
            AppEventsConstants.EVENT_NAME_AD_IMPRESSION,
            ad.valueMicros / 1000000.0,
            params
        )
    }

    var isFailNativeFullScreen = false
    var isFailNativeFullScreen2 = false

    fun loadNativeFullScreen(
        context: Context,
        nativeHolder: NativeHolderAdmob, type: String,
    ) {
        if (isTestDevice) {
            isFailNativeFullScreen = true
            isFailNativeFullScreen2 = true
            return
        }
        if (!AdmobUtils.isNetworkConnected(context) || RemoteConfig.enable_ads == "0") {
            isFailNativeFullScreen = true
            isFailNativeFullScreen2 = true
            return
        }
        AdmobUtils.loadAndGetNativeFullScreenAds(
            context as Activity,
            nativeHolder,
            MediaAspectRatio.SQUARE,
            object :
                NativeAdCallbackNew {
                override fun onAdFail(error: String) {
                    Log.d("Admob", "onAdFail: ${nativeHolder.ads} $error")
                    if (type == "NATIVE_FULLSCREEN_2") {
                        isFailNativeFullScreen = true
                    }

                    if (type == "NATIVE_FULLSCREEN_3") {
                        isFailNativeFullScreen2 = true
                    }
                }

                override fun onAdPaid(adValue: AdValue?, adUnitAds: String?) {

                }

                override fun onClickAds() {

                }

                override fun onLoadedAndGetNativeAd(ad: NativeAd?) {

                }

                override fun onNativeAdLoaded() {

                }


            })
    }

    fun loadAndShowInterSP(
        context: Context,
        interHolder: InterHolderAdmob,
        type: String,
        callback: AdListenerWithNative
    ) {
        if (isTestDevice) {
            callback.onAdClosedOrFailed()
            return
        }
        if (RemoteConfig.enable_ads == "0" || !AdmobUtils.isNetworkConnected(context)) {
            callback.onAdClosedOrFailed()
            return
        }
        if (RemoteConfig.ads_splash != "2") {
            callback.onAdClosedOrFailed()
            return
        }
        when (type) {
            "INTER_HOME" -> {
                if (RemoteConfig.inter_home == "0") {
                    callback.onAdClosedOrFailed()
                    return
                }
                val x: Int = try {
                    RemoteConfig.inter_home_count.toInt()
                } catch (e: Exception) {
                    10
                }
                if (countClickHome % x == 0) {
                    countClickHome++
                } else {
                    countClickHome++
                    callback.onAdClosedOrFailed()
                    return
                }
            }
            "INTER_BACK" -> {
                if (RemoteConfig.inter_back == "0") {
                    callback.onAdClosedOrFailed()
                    return
                }
                val x: Int = try {
                    RemoteConfig.inter_back_count.toInt()
                } catch (e: Exception) {
                    10
                }
                if (countClickBack % x == 0) {
                    countClickBack++
                } else {
                    countClickBack++
                    callback.onAdClosedOrFailed()
                    return
                }
            }

        }

        AppOpenManager.getInstance().isAppResumeEnabled = true
        AdmobUtils.loadAndShowAdInterstitial(
            context as AppCompatActivity,
            interHolder,
            object : AdsInterCallBack {
                override fun onStartAction() {
                }

                override fun onEventClickAdClosed() {
                    callback.onAdClosedOrFailedWithNative()
                }

                override fun onAdShowed() {
                    AppOpenManager.getInstance().isAppResumeEnabled = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        try {
                            AdmobUtils.dismissAdDialog()
                        } catch (_: Exception) {

                        }
                    }, 800)
                }

                override fun onAdLoaded() {

                }

                override fun onAdFail(p0: String?) {
                    callback.onAdClosedOrFailed()
                    callback.onAdClosedOrFailedWithNative()
                }

                override fun onClickAds() {

                }

                override fun onPaid(p0: AdValue?, p1: String?) {
                    p0?.let { adImpressionFacebookSDK(context, it) }
                }
            },
            true
        )
    }

    fun loadAndShowInter(
        context: Context,
        interHolder: InterHolderAdmob,
        type: String,
        callback: AdListenerNew,
    ) {

//        if (isTestDevice) {
//            callback.onAdClosedOrFailed()
//            return
//        }

        if (System.currentTimeMillis() - timeInter < 15000) {
            callback.onAdClosedOrFailed()
            return
        }
        if (RemoteConfig.enable_ads == "0" || !AdmobUtils.isNetworkConnected(context)) {
            callback.onAdClosedOrFailed()
            return
        }

        AppOpenManager.getInstance().isAppResumeEnabled = true
        AdmobUtils.loadAndShowAdInterstitial(
            context as AppCompatActivity,
            interHolder,
            object : AdsInterCallBack {
                override fun onStartAction() {

                }

                override fun onEventClickAdClosed() {
                    timeInter = System.currentTimeMillis()
                    callback.onAdClosedOrFailed()
                }

                override fun onAdShowed() {
                    AppOpenManager.getInstance().isAppResumeEnabled = false
                    Handler().postDelayed({
                        try {
                            AdmobUtils.dismissAdDialog()
                        } catch (_: Exception) {

                        }
                    }, 800)
                }

                override fun onAdLoaded() {

                }

                override fun onAdFail(p0: String?) {
                    callback.onAdClosedOrFailed()
                }

                override fun onClickAds() {
                }

                override fun onPaid(p0: AdValue?, p1: String?) {

                }
            },
            true
        )
    }

    fun loadAndShowNative(
        activity: Activity,
        nativeAdContainer: ViewGroup,
        nativeHolder: NativeHolderAdmob,
    ) {

//        if (isTestDevice) {
//            nativeAdContainer.visibility = View.GONE
//            return
//        }

        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            nativeAdContainer.visibility = View.GONE
            return
        }
        AdmobUtils.loadAndShowNativeAdsWithLayoutAds(
            activity,
            nativeHolder,
            nativeAdContainer,
            R.layout.ad_template_medium,
            GoogleENative.UNIFIED_MEDIUM,
            object : AdmobUtils.NativeAdCallbackNew {
                override fun onLoadedAndGetNativeAd(ad: NativeAd?) {

                }

                override fun onNativeAdLoaded() {
                }

                override fun onAdFail(error: String) {
                    nativeAdContainer.visibility = View.VISIBLE
                }

                override fun onAdPaid(adValue: AdValue?, adUnitAds: String?) {
                    adValue?.let { postRevenueAdjust(it, activity) }
                }

                override fun onClickAds() {
                }
            })
    }

    fun loadAndShowNativeSmall(
        activity: Activity,
        nativeAdContainer: ViewGroup,
        nativeHolder: NativeHolderAdmob,
    ) {
        if (!AdmobUtils.isNetworkConnected(activity)) {
            nativeAdContainer.visibility = View.GONE
            return
        }
        AdmobUtils.loadAndShowNativeAdsWithLayoutAds(
            activity,
            nativeHolder,
            nativeAdContainer,
            R.layout.ad_template_small,
            GoogleENative.UNIFIED_SMALL,
            object : AdmobUtils.NativeAdCallbackNew {
                override fun onLoadedAndGetNativeAd(ad: NativeAd?) {
                }

                override fun onNativeAdLoaded() {
                }

                override fun onAdFail(error: String) {
                    nativeAdContainer.visibility = View.VISIBLE
                }

                override fun onAdPaid(adValue: AdValue?, adUnitAds: String?) {
                    adValue?.let { postRevenueAdjust(it, activity) }
                }

                override fun onClickAds() {
                }
            })
    }
    fun showAdInter(
        context: Activity,
        interHolder: InterHolderAdmob,
        type: String,
        callback: AdListenerNew, isReload: Boolean,
    ) {
        if (isTestDevice) {
            callback.onAdClosedOrFailed()
            return
        }
        if (RemoteConfig.enable_ads == "0" || !AdmobUtils.isNetworkConnected(context)) {
            callback.onAdClosedOrFailed()
            return
        }
        when (type) {
            "INTER_HOME" -> {
                if (RemoteConfig.inter_home == "0") {
                    callback.onAdClosedOrFailed()
                    return
                }
                val x: Int = try {
                    RemoteConfig.inter_home_count.toInt()
                } catch (e: Exception) {
                    10
                }
                if (countClickHome % x == 0) {
                    countClickHome++
                } else {
                    countClickHome++
                    callback.onAdClosedOrFailed()
                    return
                }
            }
            "INTER_BACK" -> {
                if (RemoteConfig.inter_back == "0") {
                    callback.onAdClosedOrFailed()
                    return
                }
                val x: Int = try {
                    RemoteConfig.inter_back_count.toInt()
                } catch (e: Exception) {
                    10
                }
                if (countClickBack % x == 0) {
                    countClickBack++
                } else {
                    countClickBack++
                    callback.onAdClosedOrFailed()
                    return
                }
            }

        }

        AppOpenManager.getInstance().isAppResumeEnabled = true
        AdmobUtils.showAdInterstitialWithCallbackNotLoadNew(context, interHolder, 10000, object :
            AdsInterCallBack {
            override fun onStartAction() {

            }

            override fun onEventClickAdClosed() {
                callback.onAdClosedOrFailed()
                if (isReload) {
                    loadInter(context, interHolder)
                }
            }

            override fun onAdShowed() {
                AppOpenManager.getInstance().isAppResumeEnabled = false
                Handler().postDelayed({
                    try {
                        AdmobUtils.dismissAdDialog()
                    } catch (e: Exception) {

                    }
                }, 800)
            }

            override fun onAdLoaded() {

            }

            override fun onAdFail(error: String?) {
                callback.onAdClosedOrFailed()
                if (isReload) {
                    loadInter(context, interHolder)
                }
            }

            override fun onClickAds() {

            }

            override fun onPaid(p0: AdValue?, p1: String?) {

            }
        }, true)
    }
    fun loadInter(context: Context, interHolder: InterHolderAdmob) {

        if (isTestDevice) {
            return
        }
        if (RemoteConfig.enable_ads == "0" || !AdmobUtils.isNetworkConnected(context)) {
            return
        }
        AdmobUtils.loadAndGetAdInterstitial(context, interHolder, object :
            AdCallBackInterLoad {

            override fun onAdClosed() {

            }

            override fun onEventClickAdClosed() {

            }

            override fun onAdShowed() {
                Handler().postDelayed({
                    try {
                        AdmobUtils.dismissAdDialog()
                    } catch (_: Exception) {

                    }
                }, 800)
                AppOpenManager.getInstance().isAppResumeEnabled = false
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd?, isLoading: Boolean) {

            }

            override fun onAdFail(message: String?) {

            }

            override fun onPaid(p0: AdValue?, p1: String?) {


            }

        })
    }

    fun loadNative(context: Context, nativeHolder: NativeHolderAdmob) {

        if (isTestDevice) {
            return
        }
        if (RemoteConfig.enable_ads == "0" || !AdmobUtils.isNetworkConnected(context)) {
            return
        }
        AdmobUtils.loadAndGetNativeAds(context, nativeHolder, object : NativeAdmobCallback {
            override fun onLoadedAndGetNativeAd(ad: NativeAd?) {
                checkAdsTest(ad)
            }

            override fun onNativeAdLoaded() {

            }

            override fun onAdFail(error: String?) {
                Log.e("Admob", "onAdFail: ${nativeHolder.ads}" + error)
            }

            override fun onPaid(p0: AdValue?, p1: String?) {


            }
        })
    }

    fun showNativeLanguage(activity: Activity, viewGroup: ViewGroup, holder: NativeHolderAdmob) {

        if (isTestDevice) {
            viewGroup.visibility = View.GONE
            return
        }
        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            viewGroup.visibility = View.GONE
            return
        }

        AdmobUtils.showNativeAdsWithLayout(activity,
            holder,
            viewGroup,
            R.layout.ad_template_medium,
            GoogleENative.UNIFIED_MEDIUM,
            object : AdmobUtils.AdsNativeCallBackAdmod {
                override fun NativeLoaded() {
                    viewGroup.visibility = View.VISIBLE
                }

                override fun onPaid(adValue: AdValue?, adUnitAds: String?) {

                }

                override fun NativeFailed(massage: String) {
                    viewGroup.visibility = View.GONE
                }

            })
    }

    fun showNative(
        activity: Activity,
        nativeHolder: NativeHolderAdmob,
        nativeAdContainer: ViewGroup,
    ) {

        if (isTestDevice) {
            nativeAdContainer.visibility = View.GONE
            return
        }
        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            nativeAdContainer.visibility = View.GONE
            return
        }
        showAdNativeWithSize(
            activity,
            nativeAdContainer,
            nativeHolder,
            GoogleENative.UNIFIED_MEDIUM,
            R.layout.ad_template_medium
        )
    }

    fun showNativeSmall(
        activity: Activity,
        nativeHolder: NativeHolderAdmob,
        nativeAdContainer: ViewGroup,
    ) {

        if (isTestDevice) {
            nativeAdContainer.visibility = View.GONE
            return
        }
        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            nativeAdContainer.visibility = View.GONE
            return
        }
        showAdNativeWithSize(
            activity,
            nativeAdContainer,
            nativeHolder,
            GoogleENative.UNIFIED_SMALL,
            R.layout.ad_template_small
        )
    }

    fun showNativeSmallest(
        activity: Activity,
        nativeHolder: NativeHolderAdmob,
        nativeAdContainer: ViewGroup,
    ) {
        if (isTestDevice) {
            nativeAdContainer.visibility = View.GONE
            return
        }
        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            nativeAdContainer.visibility = View.GONE
            return
        }
        showAdNativeWithSize(
            activity,
            nativeAdContainer,
            nativeHolder,
            GoogleENative.UNIFIED_SMALL,
            R.layout.ad_template_smallest
        )
    }


    private fun showAdNativeWithSize(
        activity: Activity,
        nativeAdContainer: ViewGroup,
        nativeHolder: NativeHolderAdmob,
        googleENative: GoogleENative,
        layout: Int,
    ) {

        if (isTestDevice) {
            nativeAdContainer.visibility = View.GONE
            return
        }
        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            nativeAdContainer.visibility = View.GONE
            return
        }
        AdmobUtils.showNativeAdsWithLayout(
            activity,
            nativeHolder,
            nativeAdContainer,
            layout,
            googleENative, object : AdmobUtils.AdsNativeCallBackAdmod {
                override fun NativeFailed(massage: String) {
                    nativeAdContainer.visibility = View.GONE
                }

                override fun NativeLoaded() {

                }

                override fun onPaid(adValue: AdValue?, adUnitAds: String?) {


                }
            }
        )
    }

    fun showAdBanner(activity: Activity, adsEnum: String, view: ViewGroup, line: View) {

        if (isTestDevice) {
            view.visibility = View.GONE
            line.visibility = View.GONE
            return
        }
        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            view.visibility = View.GONE
            line.visibility = View.GONE
            return
        }
        AdmobUtils.loadAdBanner(activity, adsEnum, view, object :
            AdmobUtils.BannerCallBack {
            override fun onClickAds() {

            }

            override fun onFailed(message: String) {
                view.visibility = View.GONE
                line.visibility = View.GONE
            }

            override fun onLoad() {

            }


            override fun onPaid(adValue: AdValue?, mAdView: AdView?) {
                adValue?.let { postRevenueAdjust(it, activity) }
            }

        })
    }

    fun showAdBannerSplash(
        activity: Activity,
        adsEnum: String,
        view: ViewGroup,
        line: View,
        callback: AdListenerNew
    ) {

        if (isTestDevice) {
            view.visibility = View.GONE
            line.visibility = View.GONE
            callback.onAdClosedOrFailed()
            return
        }
        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            view.visibility = View.GONE
            line.visibility = View.GONE
            callback.onAdClosedOrFailed()
            return
        }
        AdmobUtils.loadAdBanner(activity, adsEnum, view, object :
            AdmobUtils.BannerCallBack {
            override fun onClickAds() {

            }

            override fun onFailed(message: String) {
                view.visibility = View.GONE
                line.visibility = View.GONE
                callback.onAdClosedOrFailed()
            }

            override fun onLoad() {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    callback.onAdClosedOrFailed()
                }
            }


            override fun onPaid(adValue: AdValue?, mAdView: AdView?) {
                adValue?.let { postRevenueAdjust(it, activity) }
            }

        })
    }

    fun showAdBannerCollapsible(
        activity: Activity,
        adsEnum: BannerHolderAdmob,
        view: ViewGroup,
        line: View,
    ) {
        if (isTestDevice) {
            view.visibility = View.GONE
            line.visibility = View.GONE
            return
        }
        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            view.visibility = View.GONE
            line.visibility = View.GONE
            return
        }
        AdmobUtils.loadAdBannerCollapsibleReload(activity,
            adsEnum,
            CollapsibleBanner.BOTTOM,
            view,
            object : AdmobUtils.BannerCollapsibleAdCallback {
                override fun onBannerAdLoaded(adSize: AdSize) {
                    val params: ViewGroup.LayoutParams = view.layoutParams
                    params.height = adSize.getHeightInPixels(activity)
                    view.layoutParams = params
                }

                override fun onClickAds() {

                }

                override fun onAdFail(message: String) {
                    view.visibility = View.GONE
                    line.visibility = View.GONE
                }

                override fun onAdPaid(adValue: AdValue, mAdView: AdView) {
                    postRevenueAdjust(adValue, activity)
                }
            })

    }


    interface AdListenerNew {
        fun onAdClosedOrFailed()
    }

    interface AdListenerWithNative {
        fun onAdClosedOrFailed()
        fun onAdClosedOrFailedWithNative()
    }


    fun postRevenueAdjust(ad: AdValue, context: Context) {
        adImpressionFacebookSDK(context, ad)
    }

    fun adImpressionFacebookSDK(context: Context, ad: AdValue) {
        val logger = AppEventsLogger.newLogger(context)
        val params = Bundle()
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, ad.currencyCode)
        logger.logEvent(
            AppEventsConstants.EVENT_NAME_AD_IMPRESSION,
            ad.valueMicros / 1000000.0,
            params
        )
    }

    fun showAdBannerReloadAfter30s(
        activity: Activity, bannerHolder: String, view: ViewGroup, line: View
    ) {
        if (isTestDevice) {
            view.visibility = View.GONE
            line.visibility = View.GONE
            return
        }
        if (!AdmobUtils.isNetworkConnected(activity) || RemoteConfig.enable_ads == "0") {
            view.visibility = View.GONE
            line.visibility = View.GONE
            return
        }
        val time = 30

        if (AdmobUtils.isNetworkConnected(activity)) {
            AdmobUtils.loadAndShowBannerWithConfig(
                activity,
                bannerHolder,
                time, 0,
                view, GoogleEBanner.UNIFIED_BOTTOM.name,
                object : AdmobUtils.BannerCollapsibleAdCallback {
                    override fun onBannerAdLoaded(adSize: AdSize) {
                        view.visibility = View.VISIBLE
                        line.visibility = View.VISIBLE
                        val params: ViewGroup.LayoutParams = view.layoutParams
                        params.height = adSize.getHeightInPixels(activity)
                        view.layoutParams = params
                    }

                    override fun onClickAds() {

                    }

                    override fun onAdFail(message: String) {
                        view.visibility = View.GONE
                        line.visibility = View.GONE
                    }

                    override fun onAdPaid(adValue: AdValue, mAdView: AdView) {
                        adImpressionFacebookSDK(activity, adValue)
                    }
                })
        } else {
            view.visibility = View.GONE
            line.visibility = View.GONE
        }
    }
}