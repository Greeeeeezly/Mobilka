package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab3.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Инициализация View Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Настройка RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = ChatAdapter(getChatItems())

        return binding.root
    }

    private fun getChatItems(): List<ChatItem> {
        return listOf(
            ChatItem("Иван", "Привет!", "12:00"),
            ChatItem("Мария", "НЕ ПИШИ МНЕ БОЛЬШЕ!!!", "12:05"),
            ChatItem("Алексей", "?", "15:10"),
            ChatItem("Василий", "Когда за пивом?", "17:10")
        )
    }

}
