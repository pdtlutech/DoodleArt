package com.example.android.art.glow.drawing.main.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.ads.AdsManager
import com.example.android.art.glow.drawing.ads.RemoteConfig
import com.example.android.art.glow.drawing.base.BaseFragment
import com.example.android.art.glow.drawing.custom.DrawActivity
import com.example.android.art.glow.drawing.databinding.FragmentCustomPictureBinding
import com.example.android.art.glow.drawing.inspiration.InspirationActivity
import com.example.android.art.glow.drawing.main.adapter.SamplePictureAdapter
import com.example.android.art.glow.drawing.splash.NativeFullActivity
import com.example.android.art.glow.drawing.splash.NativeFullActivity.Companion.inspirationScreen
import com.example.android.art.glow.drawing.utils.FileUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomPictureFragment : BaseFragment<FragmentCustomPictureBinding>(FragmentCustomPictureBinding::inflate) {

    private val allImages = mutableListOf<String>()
    private val allSelectedImages = mutableListOf<String>()
    private val adapter = SamplePictureAdapter({ file ->
        if(allSelectedImages.contains(file)) {
            allSelectedImages.remove(file)
        } else {
            allSelectedImages.add(file)
        }

        isSelectAll = allImages.size == allSelectedImages.size
        reloadData(false)
    }) {

    }
    private var isSelectAll = false

    override fun onResume() {
        super.onResume()

        allImages.clear()
        allSelectedImages.clear()
        isSelectAll = false

        binding.emptyIcon.visibility = View.GONE
        binding.emptyDescription.visibility = View.GONE
        binding.newDrawingBtn.visibility = View.GONE

        selectPictures()

        binding.apply {
            newDrawingBtn.setOnClickListener {
                openNextScreen(true)

            }

            iconShare.setOnClickListener {
                withSafeContext { ctx ->
                    if(allSelectedImages.size > 0) {
                        FileUtils.shareVideoOrAudio( ctx ,"Share Images",allSelectedImages )
                    }
                }

            }

            iconDelete.setOnClickListener {
                if(allSelectedImages.size > 0) {
                    allSelectedImages.onEach { filePath ->
                        FileUtils.deleteFileByPath(filePath)
                    }
                    allImages.removeAll(allSelectedImages)
                    allSelectedImages.clear()
                    reloadData(false)
                }
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = context ?: return
        binding.allMyPictures.layoutManager = GridLayoutManager(ctx, 2)
        binding.allMyPictures.adapter = adapter
        reloadData(true)
    }

    private fun selectPictures() {
        binding.apply {
            selectAll.setOnClickListener {
                allSelectedImages.clear()
                allSelectedImages.addAll(allImages)
                isSelectAll = !isSelectAll
                reloadData(false)
                updateAllItems()
            }

            cancelBtn.setOnClickListener {
                withSafeContext { ctx ->
                    allSelectedImages.clear()
                    selectAll.setImageResource(R.drawable.icon_unselect_all)
                    selectTitle.text = ctx.resources.getString(R.string.selected)
                    adapter.submitList(allSelectedImages, allImages, true)
                }

            }
        }
    }

    override fun reloadData(isDisplayLoading: Boolean) {
        super.reloadData(isDisplayLoading)
        CoroutineScope(Dispatchers.IO).launch {

            val ctx = context ?: return@launch
            val x = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                FileUtils.getAllImages(ctx)
            } else {
                FileUtils.getAllImagesOlderVersion()
            }
            println("reloadData: ${x.size}")
            allImages.clear()
            allImages.addAll(x)

            withContext(Dispatchers.Main) {
                if(allImages.isEmpty()) {
                    binding.emptyIcon.visibility = View.VISIBLE
                    binding.emptyDescription.visibility = View.VISIBLE
                    binding.newDrawingBtn.visibility = View.VISIBLE

                    binding.topController.visibility = View.GONE
                    binding.allMyPictures.visibility = View.GONE
                } else {
                    binding.emptyIcon.visibility = View.GONE
                    binding.emptyDescription.visibility = View.GONE
                    binding.newDrawingBtn.visibility = View.GONE

                    binding.allMyPictures.visibility =  View.VISIBLE
                    binding.topController.visibility =  View.VISIBLE

                    adapter.submitList(allSelectedImages ,allImages, true)
                }

                withSafeContext {
                    updateAllItems()
                }
            }
        }

    }


    private fun updateAllItems() {
        binding.apply {
            if(isSelectAll) {
                selectAll.setImageResource(R.drawable.icon_select_all)
                selectTitle.text = resources.getString(R.string.selected_all)
                allSelectedImages.addAll(allImages)
            } else if(allSelectedImages.size > 0 && allSelectedImages.size < allImages.size) {
                selectAll.setImageResource(R.drawable.icon_unselect_all)
                selectTitle.text = resources.getString(R.string.selected).plus(" ${allSelectedImages.size}")
            }  else {
                allSelectedImages.clear()
                selectAll.setImageResource(R.drawable.icon_unselect_all)
                selectTitle.text = resources.getString(R.string.selected)
            }

            if(allSelectedImages.size > 0) {
                bottomController.visibility = View.VISIBLE
            } else {
                bottomController.visibility = View.GONE
            }
        }
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

    private fun nextFullScreen(isDraw: Boolean = false) {
        val ctx = context ?: return
        val intent = Intent(ctx, NativeFullActivity::class.java)
        intent.putExtra("toDrawPicture", isDraw)
        inspirationScreen = 0
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CustomPictureFragment().apply {  }
    }
}