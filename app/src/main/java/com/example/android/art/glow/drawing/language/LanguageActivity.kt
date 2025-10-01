package com.example.android.art.glow.drawing.language

import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.admob.max.dktlibrary.utils.Utils
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.ads.AdsManager
import com.example.android.art.glow.drawing.ads.RemoteConfig
import com.example.android.art.glow.drawing.base.BaseActivity2
import com.example.android.art.glow.drawing.databinding.ActivityLanguageBinding
import com.example.android.art.glow.drawing.intro.IntroActivity
import com.example.android.art.glow.drawing.language.adapter.LanguageAdapter
import com.example.android.art.glow.drawing.language.model.Language
import com.example.android.art.glow.drawing.main.MainActivity
import com.example.android.art.glow.drawing.utils.Common
import com.example.android.art.glow.drawing.utils.Common.gone
import com.example.android.art.glow.drawing.utils.Common.inVisible
import com.example.android.art.glow.drawing.utils.Common.visible

class LanguageActivity : BaseActivity2<ActivityLanguageBinding>(ActivityLanguageBinding::inflate) {
    private var isBackground = false

    companion object {
        var languageKey = ""
    }

    lateinit var language: String
    var flag = R.drawable.ic_english
    var pos = -1
    var adapter: LanguageAdapter? = null
    private var start = false
    override fun initView() {
        getBack()
        getLanguage()
        changeLanguageDone()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        setContentView(binding.root)
        binding.tv.isSelected = true
        start = intent.getBooleanExtra("isSplash", false)

        if (start) {
            binding.btnBack.inVisible()
            if (RemoteConfig.native_language == "1") {
                AdsManager.showNativeLanguage(
                    this, binding.frNative, AdsManager.NATIVE_LANGUAGE_1
                )
            }
            if (RemoteConfig.native_intro_1 != "0") {
                AdsManager.loadNative(this, AdsManager.NATIVE_INTRO_1)
            }

            if (RemoteConfig.native_full_intro_2 == "1") {
                AdsManager.loadNativeFullScreen(
                    this,
                    AdsManager.NATIVE_FULLSCREEN_2,
                    "NATIVE_FULLSCREEN_2"
                )
            } else {
                AdsManager.isFailNativeFullScreen = true
            }
            if (RemoteConfig.native_full_intro_3 == "1") {
                AdsManager.loadNativeFullScreen(
                    this,
                    AdsManager.NATIVE_FULLSCREEN_3,
                    "NATIVE_FULLSCREEN_3"
                )
            } else {
                AdsManager.isFailNativeFullScreen2 = true
            }

        } else {
            binding.btnBack.visible()
            pos = -1
            binding.cvSelect.inVisible()
            val languageList = ArrayList<Language>()
            setLanguageList(languageList)
            adapter = LanguageAdapter(object : LanguageAdapter.OnClickListener {
                override fun onClickListener(position: Int, name: String, img: Int) {
                    if (pos == -1) {
                        if (RemoteConfig.native_language == "1") {
                            AdsManager.showNative(
                                this@LanguageActivity,
                                AdsManager.NATIVE_LANGUAGE_2,
                                binding.frNative
                            )
                        }
                    }
                    pos = position
                    language = name
                    flag = img
                    adapter?.updatePosition(pos)
                    binding.cvSelect.visible()
                }
            })
            adapter?.updateData(languageList)

            binding.rcvLanguage.apply {
                layoutManager = LinearLayoutManager(context)
            }
            binding.rcvLanguage.adapter = adapter
            if (RemoteConfig.native_language == "1") {
                AdsManager.loadAndShowNative(this, binding.frNative, AdsManager.NATIVE_LANGUAGE_1)
            }
        }
        if (RemoteConfig.native_language_small == "1"){
            AdsManager.showNativeSmallest(this, AdsManager.NATIVE_SMALL_LANGUAGE, binding.frNativeTop)
        }else{
            binding.frNativeTop.gone()
        }
    }


    override fun onPause() {
        super.onPause()
        isBackground = true
    }

    private fun changeLanguageDone() {
        binding.cvSelect.setOnClickListener {
            if (pos == -1) {
                Toast.makeText(this, "Please select languages before apply!", Toast.LENGTH_SHORT)
                    .show()

            } else {
                Common.setPreLanguage(this@LanguageActivity, language)
                Common.setPreLanguageflag(this@LanguageActivity, flag)
                Common.setPosition(this@LanguageActivity, pos)
                languageKey = language
                if (start) {
                    Utils.getInstance()
                        .replaceActivity(this@LanguageActivity, IntroActivity::class.java)
                } else {
                    Utils.getInstance()
                        .replaceActivity(this@LanguageActivity, MainActivity::class.java)
                }
            }
        }
    }
    private fun getBack() {
        binding.btnBack.setOnClickListener {
            if (!start) {
                finish()
            } else {
                Utils.getInstance().replaceActivity(this@LanguageActivity, MainActivity::class.java)
            }
        }
    }


    private fun getLanguage() {
        language = Common.getPreLanguage(this)
        flag = Common.getPreLanguageflag(this)
        languageKey = language
        val languageList = ArrayList<Language>()
        setLanguageList(languageList)
        pos = Common.getPosition(this)
        adapter = LanguageAdapter(object : LanguageAdapter.OnClickListener {
            override fun onClickListener(position: Int, name: String, img: Int) {
                if (pos == -1) {
                    Handler().postDelayed({
                        binding.cvSelect.visibility = View.VISIBLE
                    },1000)
                    if (RemoteConfig.native_language == "1") {
                        AdsManager.showNative(
                            this@LanguageActivity, AdsManager.NATIVE_LANGUAGE_2, binding.frNative
                        )
                    }
                }
                pos = position
                language = name
                flag = img
                adapter?.updatePosition(pos)
            }
        })
        adapter?.updateData(languageList)
        adapter?.updatePosition(pos)

        binding.rcvLanguage.apply {
            layoutManager = LinearLayoutManager(context)
        }
        binding.rcvLanguage.adapter = adapter
    }

    private fun setLanguageList(languageList: ArrayList<Language>) {
        languageList.add(Language(R.drawable.ic_english, getString(R.string.english), "en"))
        languageList.add(Language(R.drawable.ic_hindi, getString(R.string.hindi), "hi"))
        languageList.add(Language(R.drawable.ic_spanish, getString(R.string.spanish), "es"))
        languageList.add(Language(R.drawable.ic_french, getString(R.string.french), "fr"))
        languageList.add(Language(R.drawable.ic_arabic, getString(R.string.arabic), "ar"))
        languageList.add(Language(R.drawable.ic_bengali, getString(R.string.bengali), "bn"))
        languageList.add(Language(R.drawable.ic_russian, getString(R.string.russian), "ru"))
        languageList.add(Language(R.drawable.ic_portuguese, getString(R.string.portuguese), "pt"))
        languageList.add(Language(R.drawable.ic_indonesian, getString(R.string.indonesian), "in"))
        languageList.add(Language(R.drawable.ic_german, getString(R.string.german), "de"))
        languageList.add(Language(R.drawable.ic_italian, getString(R.string.italian), "it"))
        languageList.add(Language(R.drawable.ic_korean, getString(R.string.korean), "ko"))
    }
    override fun onStop() {
        super.onStop()
        isBackground = true
    }
}
