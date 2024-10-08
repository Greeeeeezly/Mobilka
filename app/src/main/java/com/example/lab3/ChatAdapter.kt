package com.example.lab3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class ChatItem(val name: String, val lastMessage: String, val time: String)

class ChatAdapter(private val chatItems: List<ChatItem>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatItem = chatItems[position]
        holder.bind(chatItem)
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessage)
        private val timeTextView: TextView = itemView.findViewById(R.id.time)

        fun bind(chatItem: ChatItem) {
            nameTextView.text = chatItem.name
            lastMessageTextView.text = chatItem.lastMessage
            timeTextView.text = chatItem.time
        }
    }
}
