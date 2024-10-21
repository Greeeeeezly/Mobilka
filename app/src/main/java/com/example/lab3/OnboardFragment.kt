package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentOnboardBinding

class OnboardFragment : Fragment() {
    private lateinit var binding: FragmentOnboardBinding
    private lateinit var registeredUsers: MutableList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registeredUsers = mutableListOf()

        // Инициализация View Binding
        binding = FragmentOnboardBinding.inflate(inflater, container, false)
        val view = binding.root // Получаем корневое представление из View Binding

        val nextButton: Button = binding.nextButton // Используем View Binding для доступа к кнопке
        nextButton.setOnClickListener {
            // Используем Safe Args для навигации
            val action = OnboardFragmentDirections.actionOnboardFragmentToSignInFragment(registeredUsers.toTypedArray())
            findNavController().navigate(action)
        }

        return view
    }
}
