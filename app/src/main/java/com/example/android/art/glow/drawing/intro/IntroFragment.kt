package com.example.android.art.glow.drawing.intro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.ads.AdsManager
import com.example.android.art.glow.drawing.ads.RemoteConfig
import com.example.android.art.glow.drawing.databinding.ViewpagerItempageBinding
import com.example.android.art.glow.drawing.utils.Common.gone
import com.example.android.art.glow.drawing.utils.Common.visible

class IntroFragment : Fragment() {
    private var bgId = 0
    private var dot = 0
    private var size = 4
    private var position = 0
    private var title = ""
    private var text = ""
    private val binding by lazy { ViewpagerItempageBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    private var callbackIntro: CallbackIntro? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isAdded && activity == null) {
            return
        }
        activity?.let { activity ->
            if (activity is CallbackIntro) callbackIntro = activity
            bgId = arguments?.getInt("id") ?: R.drawable.first_intro
            println("IntroFragment: $bgId")
            dot = arguments?.getInt("dot") ?: R.drawable.icon_dot
            size = arguments?.getInt("size") ?: 3
            position = arguments?.getInt("position") ?: 0
            title = arguments?.getString("title") ?: getString(R.string.first_intro_title)
            text = arguments?.getString("text") ?: getString(R.string.first_intro_description)
            binding.image.setImageResource(bgId)
            binding.content.text = text
            binding.title.text = title
            binding.slidedot.setImageResource(dot)

            when (position) {
                0 -> {
                    binding.closeAds.gone()
                    when (RemoteConfig.native_intro_1) {
                        "1" -> {
                            AdsManager.showNativeLanguage(
                                activity,
                                binding.frNative,
                                AdsManager.NATIVE_INTRO_1
                            )
                        }

                        else -> {
                            binding.frNative.gone()
                        }
                    }
                }

                1 -> {
                    Log.d("TAG=======", "onViewCreated: 1")
                    if (size > 3 && bgId == 0) {
                        Log.d(
                            "TAG=======",
                            "onViewCreated: 1.1"
                        )
                        binding.closeAds.visible()
                        binding.frNativeFull.visible()
                        binding.rlBot.gone()
                        AdsManager.showNativeFullScreen(
                            activity,
                            AdsManager.NATIVE_FULLSCREEN_2,
                            binding.frNativeFull
                        )
                    } else {
                        binding.frNative.gone()
                    }
                }

                size - 1 -> {
                    binding.closeAds.gone()
                    Log.d(
                        "TAG=======",
                        "onViewCreated: ${if (size > 3) "1.3" else "2.2"}"
                    )
                    if (RemoteConfig.native_intro_4 == "1") {
                        AdsManager.showNativeLanguage(
                            activity,
                            binding.frNative,
                            AdsManager.NATIVE_INTRO_4
                        )
                    } else {
                        binding.frNative.gone()
                    }
                }

                size - 2 -> {
                    Log.d("TAG=======", "onViewCreated: 1.4")
                    if (size > 3 && bgId == 0) {
                        binding.frNativeFull.visible()
                        binding.rlBot.gone()
                        binding.closeAds.visible()
                        AdsManager.showNativeFullScreen(
                            activity,
                            AdsManager.NATIVE_FULLSCREEN_3,
                            binding.frNativeFull
                        )
                    } else {
                        binding.frNative.gone()
                    }
                }

                2 -> {
                    binding.closeAds.gone()
                    Log.d(
                        "TAG=======",
                        "onViewCreated: ${if (size > 3) "1.5" else "2.1"}"
                    )
                    binding.frNative.gone()
                }

                else -> {
                    binding.closeAds.gone()
                    binding.frNative.gone()
                }
            }

            binding.next.setOnClickListener {
                callbackIntro?.onNext(position + 1)
            }
            binding.closeAds.setOnClickListener {
                callbackIntro?.onNext(position + 1)
            }

        }
    }


    companion object {
        @JvmStatic
        fun newInstance(
            bgId: Int, title: String, text: String, position: Int, size: Int, dot: Int,
        ): IntroFragment {
            val args = Bundle()
            args.putInt("id", bgId)
            args.putInt("dot", dot)
            args.putInt("size", size)
            args.putInt("position", position)
            args.putString("title", title)
            args.putString("text", text)
            val f = IntroFragment()
            f.arguments = args
            return f
        }

    }
}