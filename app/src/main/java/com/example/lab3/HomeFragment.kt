package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab3.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Настройка RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = ChatAdapter(getChatItems())

        // Переход в CharacterFragment по клику на кнопку
        binding.openCharacterFragmentButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_characterFragment)
        }

        // Переход в SettingsFragment по клику на кнопку
        binding.openSettingsFragmentButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

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
