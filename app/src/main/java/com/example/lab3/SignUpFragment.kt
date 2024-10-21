package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentOnboardBinding
import com.example.lab3.databinding.FragmentSignupBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding

    private lateinit var registeredUsers: MutableList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инициализация View Binding
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        // Получение списка зарегистрированных пользователей через Safe Args
        registeredUsers = arguments?.let {
            SignUpFragmentArgs.fromBundle(it).registeredUsers.toMutableList()
        } ?: mutableListOf()

        // Установка обработчика нажатия на кнопку регистрации
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(activity, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Проверка, существует ли уже пользователь с таким email
            if (registeredUsers.any { it.email.equals(email, ignoreCase = true) }) {
                Toast.makeText(
                    activity,
                    "Пользователь с таким email уже существует",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val newUser = User(name, email, password)
            registeredUsers.add(newUser)

            Toast.makeText(activity, "Пользователь $name зарегистрирован!", Toast.LENGTH_SHORT)
                .show()

            // Переход на SignInFragment с передачей обновленного списка пользователей
            val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment(
                registeredUsers.toTypedArray()
            )
            findNavController().navigate(action)
        }

        return binding?.root // Возврат корневого представления из View Binding
    }
}
