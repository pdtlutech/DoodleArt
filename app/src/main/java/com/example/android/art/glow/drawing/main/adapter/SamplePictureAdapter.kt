package com.example.android.art.glow.drawing.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.databinding.ItemSamplePictureBinding
import java.io.File

class SamplePictureAdapter(
    private val onSelectListener: (String) -> Unit,
    private val onOpenSample: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val sampleList = mutableListOf<Any>()

    private val selectList = mutableListOf<String>()

    private var isCustomFile = false

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
       val binding = ItemSamplePictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SamplePictureViewHolder(binding)
    }

    override fun getItemCount(): Int = sampleList.size

    fun submitList(selectedList: List<String>,list: List<Any>, isCustom : Boolean = false) {
        sampleList.clear()
        selectList.clear()

        sampleList.addAll(list)
        selectList.addAll(selectedList)
        isCustomFile = isCustom
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if(holder is SamplePictureViewHolder) {
           holder.bind(position)
       }
    }

    inner class SamplePictureViewHolder(private val binding: ItemSamplePictureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (position: Int) {
            val item = sampleList[position]
            println("reloadData 1: $selectList")
            if(isCustomFile) {
                val file = File(item as String)
                Glide.with(context).load(file).placeholder(R.drawable.background_corner_6).error(R.drawable.background_corner_6).into(binding.songAvatar)

                if(item in selectList) {
                    binding.selectedItem.visibility = View.VISIBLE
                } else {
                    binding.selectedItem.visibility = View.GONE
                }
            } else {
                binding.songAvatar.setImageResource(item as Int)
            }


            binding.root.setOnClickListener {
                println("SamplePictureViewHolder 0: $isCustomFile")
                if (isCustomFile) {
                    if (item in selectList) {
                        selectList.remove(item)
                    } else {
                        selectList.add(item as String)
                    }
                    // Notify that only this item has changed
                    onSelectListener(item as String)
                    notifyItemChanged(position)
                } else {
                    onOpenSample(position)
                }

            }
        }
    }
}