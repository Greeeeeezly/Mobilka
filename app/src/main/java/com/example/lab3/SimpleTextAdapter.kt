package com.example.lab3

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SimpleTextAdapter : RecyclerView.Adapter<SimpleTextAdapter.TextViewHolder>() {

    private var items: List<String> = listOf()

    fun submitList(list: List<String>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_title, parent, false) as TextView
        return TextViewHolder(textView)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class TextViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
        fun bind(text: String) {
            textView.text = text
        }
    }
}
