package com.example.android.art.glow.drawing.custom.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.custom.DrawActivity
import com.example.android.art.glow.drawing.databinding.ItemCustomPickBinding
import com.example.android.art.glow.drawing.databinding.ItemDefaultPickBinding
import com.example.android.art.glow.drawing.dialog.CustomColorPickerDialog

class PickupBackgroundAdapter( private val onClickListener : (Int, Boolean) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val allBackground : MutableList<Int> = mutableListOf()

    private var selectedIndex = -1

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if(viewType == ITEM_HEAD) {
            val binding = ItemCustomPickBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomBackgroundViewHolder(binding)
        }
        val binding = ItemDefaultPickBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DefaultBackgroundViewHolder(binding)

    }

    override fun getItemCount(): Int = allBackground.size + 1

    override fun getItemViewType(position: Int): Int {
        return if(position == 0)  ITEM_HEAD else ITEM_BODY
    }

    fun submitList(input: List<Int>, index : Int = -1) {
        allBackground.clear()
        allBackground.addAll(input)

        selectedIndex = index
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CustomBackgroundViewHolder) {
            holder.bind()
        } else if(holder is DefaultBackgroundViewHolder){
            holder.bind(position)
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


    inner class DefaultBackgroundViewHolder(private val binding: ItemDefaultPickBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val newPos = position - 1
            val item = allBackground[newPos]
            binding.songAvatar.setBackgroundColor(context.resources.getColor(item))
            if(position == selectedIndex) {
                binding.container.setBackgroundResource(R.drawable.background_corner_6_selected)
                binding.container.setPadding(2,2,2,2)
            } else {
                binding.container.setBackgroundResource(R.drawable.background_corner_6)
                binding.container.setPadding(0,0,0,0)
            }

            binding.root.setOnClickListener {
                selectedIndex = position
                DrawActivity.backgroundIndex = position
                onClickListener(item, false)
                notifyDataSetChanged()
            }
        }

    }

    companion object {
        const val ITEM_HEAD = 0
        const val ITEM_BODY = 1
    }
}