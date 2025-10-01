package com.example.android.art.glow.drawing.custom.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.custom.DrawActivity
import com.example.android.art.glow.drawing.databinding.ItemCustomIconBinding

class PickupIconAdapter(private val onClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val allLines: MutableList<Int> = mutableListOf()

    private var selectedIndex = -1
    private var isSymmetricArt = false

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val binding =
            ItemCustomIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomIconViewHolder(binding)

    }

    override fun getItemCount(): Int = allLines.size


    fun submitList(input: List<Int>, index: Int = 0, isSymmetric: Boolean = false) {
        allLines.clear()
        allLines.addAll(input)
        selectedIndex = index
        isSymmetricArt = isSymmetric
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CustomIconViewHolder) {
            holder.bind(position)
        }
    }

    inner class CustomIconViewHolder(private val binding: ItemCustomIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = allLines[position]
            binding.icon.setImageResource(item)

            if (selectedIndex == position) {
                binding.container.setBackgroundResource(R.drawable.background_corner_6_selected)
            } else {
                binding.container.setBackgroundResource(R.drawable.background_corner_6)
            }

            val result = when (position) {
                0 -> 1
                1 -> 2
                2 -> 4
                3 -> 5
                4 -> 6
                5 -> 8
                6 -> 10
                7 -> 12
                else -> 1
            }

            binding.root.setOnClickListener {
                selectedIndex = position

                if (isSymmetricArt) {
                    DrawActivity.symmetricArtIndex = position
                } else {
                    DrawActivity.stickerIndex = position
                }
                onClickListener(if (isSymmetricArt) result else position)
                notifyDataSetChanged()
            }
        }
    }


}