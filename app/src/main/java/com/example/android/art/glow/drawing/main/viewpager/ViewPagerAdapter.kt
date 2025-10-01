package com.example.android.art.glow.drawing.main.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.art.glow.drawing.main.fragment.CustomPictureFragment
import com.example.android.art.glow.drawing.main.fragment.SampleFragment

class ViewPagerAdapter(private val items: ArrayList<Fragment>, activity: FragmentActivity)
    :FragmentStateAdapter(activity){
    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        val currentFragment  = items[position]
        println("createFragment: $position and $currentFragment")
        return when(currentFragment) {
            SampleFragment() -> currentFragment.also {
                SampleFragment.newInstance()
            }
            CustomPictureFragment() -> currentFragment.also {
                CustomPictureFragment.newInstance()
            }
            else -> currentFragment.also {
                SampleFragment.newInstance()
            }
        }
    }
}