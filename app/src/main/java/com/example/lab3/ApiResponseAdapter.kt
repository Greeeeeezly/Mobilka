package com.example.lab3

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.ListItemBinding

class ApiResponseAdapter(private var items: List<Character>) :
    RecyclerView.Adapter<ApiResponseAdapter.ApiResponseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiResponseViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApiResponseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApiResponseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<Character>) {
        this.items = newData
        notifyDataSetChanged()
    }

    class ApiResponseViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val titlesAdapter = SimpleTextAdapter()
        private val aliasesAdapter = SimpleTextAdapter()

        init {
            binding.titlesRecyclerView.adapter = titlesAdapter
            binding.aliasesRecyclerView.adapter = aliasesAdapter

            binding.titlesRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
            binding.aliasesRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        }

        fun bind(apiResponse: Character) {
            with(binding) {
                nameTextView.text = apiResponse.name
                usernameTextView.text = apiResponse.culture
                emailTextView.text = apiResponse.born
                playedByTextView.text = "Played by: ${apiResponse.playedBy.firstOrNull() ?: "-"}"
                titlesAdapter.submitList(apiResponse.titles)
                aliasesAdapter.submitList(apiResponse.aliases)
            }
        }
    }
}