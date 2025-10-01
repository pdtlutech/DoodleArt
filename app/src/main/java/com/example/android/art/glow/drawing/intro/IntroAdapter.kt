package com.example.android.art.glow.drawing.intro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroAdapter(
    fragmentActivity: FragmentActivity, private val image: List<Int>,
    private val title: List<String>,
    val content: List<String>, private val dots: List<Int>
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return IntroFragment.newInstance(
            image[position],
            title[position],
            content[position],
            position, image.size, dots[position]
        )
    }

    override fun getItemCount(): Int {
        return image.size
    }
}
