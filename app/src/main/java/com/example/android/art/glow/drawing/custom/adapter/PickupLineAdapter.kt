package com.example.android.art.glow.drawing.custom.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.custom.DrawActivity
import com.example.android.art.glow.drawing.custom.adapter.PickupBackgroundAdapter.Companion.ITEM_BODY
import com.example.android.art.glow.drawing.custom.adapter.PickupBackgroundAdapter.Companion.ITEM_HEAD
import com.example.android.art.glow.drawing.databinding.ItemCustomPickBinding
import com.example.android.art.glow.drawing.databinding.ItemDefaultLinePickBinding
import com.example.android.art.glow.drawing.dialog.CustomColorPickerDialog
import com.example.android.art.glow.drawing.utils.Constant

class PickupLineAdapter(private val onClickListener: (Int, Boolean) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val allLines : MutableList<Int> = mutableListOf()

    private lateinit var context: Context

    private var selectedIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if(viewType == ITEM_HEAD) {
            val binding = ItemCustomPickBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomBackgroundViewHolder(binding)
        }
        val binding = ItemDefaultLinePickBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DefaultBackgroundViewHolder(binding)

    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0)  ITEM_HEAD else ITEM_BODY
    }

    override fun getItemCount(): Int = allLines.size + 1


    fun submitList(input: List<Int>, index: Int = 0) {
        allLines.clear()
        allLines.addAll(input)
        selectedIndex = index
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is DefaultBackgroundViewHolder) {
            println("onBindViewHolder: $position")
            holder.bind(position)
        } else if(holder is CustomBackgroundViewHolder) {
            holder.bind()
        }
    }

    inner class CustomBackgroundViewHolder(private val binding: ItemCustomPickBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.icon.setImageResource(R.drawable.icon_custom)
            if(selectedIndex == 0) {
                binding.container.setBackgroundResource(R.drawable.background_corner_6_selected)
            } else {
                binding.container.setBackgroundResource(R.drawable.background_corner_6)
            }

            binding.root.setOnClickListener {
                selectedIndex = 0
                DrawActivity.backgroundIndex = 0
                val dialog = CustomColorPickerDialog(context) { newColor ->
                    onClickListener(newColor, true)
                }
                dialog.show()
                notifyDataSetChanged()
            }

        }
    }

    inner class DefaultBackgroundViewHolder(private val binding: ItemDefaultLinePickBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            println("CustomBackgroundViewHolder $position and $allLines" )
               val newPos = position - 1
                val item = allLines[newPos]
                println("CustomBackgroundViewHolder: $item")
                binding.icon.setImageResource(item)

                if(selectedIndex == position) {
                    binding.container.setBackgroundResource(R.drawable.background_corner_6_selected)
                    binding.container.setPadding(2,2,2,2)
                } else {
                    binding.container.setBackgroundResource(R.drawable.background_corner_6)
                    binding.container.setPadding(0,0,0,0)
                }

                binding.root.setOnClickListener {
                    selectedIndex = position
                    DrawActivity.lineIndex = position
                    onClickListener(Constant.allColors[newPos], false)
                    notifyDataSetChanged()
                }

        }
    }

}