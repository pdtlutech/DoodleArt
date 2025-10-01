package com.example.android.art.glow.drawing.language.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.art.glow.drawing.R
import com.example.android.art.glow.drawing.databinding.RcvLanguageBinding
import com.example.android.art.glow.drawing.language.model.Language

class LanguageAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    private var selected = -1
    private var languageList: ArrayList<Language> = ArrayList()


    class ViewHolder(val binding: RcvLanguageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RcvLanguageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    interface OnClickListener {
        fun onClickListener(position: Int, name: String, img: Int)
    }

    fun updateData(data: ArrayList<Language>) {
        languageList.clear()
        languageList.addAll(data)
        notifyDataSetChanged()
    }

    fun updatePosition(position: Int) {
        selected = position
        notifyDataSetChanged()
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language = languageList[position]
        if (selected == position) {
            holder.binding.language.setBackgroundResource(R.drawable.btn_choselanguage)
            holder.binding.languageName.setTextColor(Color.parseColor("#008D7D"))
            holder.binding.checkbox.setImageResource(R.drawable.checkbox_select)
        } else {
            holder.binding.language.setBackgroundResource(R.drawable.btn_language_no_select)
            holder.binding.languageName.setTextColor(Color.parseColor("#5A5A5A"))
            holder.binding.checkbox.setImageResource(R.drawable.checkbox_un_select)
        }

        holder.binding.languageIcon.setImageResource(language.img)
        holder.binding.languageName.text = language.name
        holder.binding.languageName.isSelected = true

        holder.itemView.setOnClickListener {
            onClickListener.onClickListener(position, language.key, language.img)
        }
    }

    override fun getItemCount(): Int {
        return languageList.size
    }
}