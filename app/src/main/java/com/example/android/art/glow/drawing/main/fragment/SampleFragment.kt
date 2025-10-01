package com.example.android.art.glow.drawing.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.art.glow.drawing.ads.AdsManager
import com.example.android.art.glow.drawing.ads.RemoteConfig
import com.example.android.art.glow.drawing.base.BaseFragment
import com.example.android.art.glow.drawing.custom.DrawActivity
import com.example.android.art.glow.drawing.databinding.FragmentSampleBinding
import com.example.android.art.glow.drawing.inspiration.InspirationActivity
import com.example.android.art.glow.drawing.inspiration.InspirationActivity.Companion.inspirationIndex
import com.example.android.art.glow.drawing.main.adapter.SamplePictureAdapter
import com.example.android.art.glow.drawing.splash.NativeFullActivity
import com.example.android.art.glow.drawing.splash.NativeFullActivity.Companion.inspirationScreen
import com.example.android.art.glow.drawing.utils.Constant

class SampleFragment : BaseFragment<FragmentSampleBinding>(FragmentSampleBinding::inflate) {
    private val adapter = SamplePictureAdapter({}) { position ->
        inspirationIndex = position
        openNextScreen(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newDrawingBtn.setOnClickListener {
            openNextScreen(true)
        }

        val ctx = context ?: return
        binding.allSamples.layoutManager = GridLayoutManager(ctx, 2)
        binding.allSamples.adapter = adapter
        adapter.submitList(listOf(), initializeList())
    }

    private fun openNextScreen(isDraw: Boolean = false) {
        println("openNextScreen: $isDraw")
        withSafeContext { ctx ->
            if (RemoteConfig.inter_home != "0") {
                AdsManager.loadAndShowInterSP(
                    requireActivity(),
                    AdsManager.INTER_HOME,
                    "INTER_HOME",
                    object : AdsManager.AdListenerWithNative {
                        override fun onAdClosedOrFailed() {
                            println("openNextScreen 1: $isDraw")
                            if (isDraw) {
                                startActivity(Intent(ctx, DrawActivity::class.java))
                            } else {
                                startActivity(Intent(ctx, InspirationActivity::class.java))
                            }
                        }

                        override fun onAdClosedOrFailedWithNative() {
                            println("openNextScreen 2: $isDraw and ${RemoteConfig.native_full_home}")
                            if (RemoteConfig.native_full_home != "0") {
                                nextFullScreen(isDraw)
                            } else {
                                startActivity(Intent(ctx, DrawActivity::class.java))
                            }
                        }
                    },
                )
            } else {
                startActivity(Intent(ctx, DrawActivity::class.java))
            }
        }
    }

    private fun initializeList(): List<Int> {
        return Constant.allSamples
    }

    private fun nextFullScreen(isDraw: Boolean = false) {
        val ctx = context ?: return
        val intent = Intent(ctx, NativeFullActivity::class.java)
        intent.putExtra("toDrawPicture", isDraw)
        inspirationScreen = 0
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SampleFragment().apply { }
    }
}