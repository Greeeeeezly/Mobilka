package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentOnboardBinding
import com.example.lab3.databinding.FragmentSigninBinding

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding

    private lateinit var registeredUsers: MutableList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инициализация View Binding
        binding = FragmentSigninBinding.inflate(inflater, container, false)

        // Получение списка зарегистрированных пользователей через Safe Args
        val args = SignInFragmentArgs.fromBundle(requireArguments())
        registeredUsers = args.registeredUsers.toMutableList() ?: mutableListOf()

        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(activity, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = registeredUsers.find { it.email.equals(email, ignoreCase = true) && it.password == password }
            if (user != null) {
                Toast.makeText(activity, "Добро пожаловать, ${user.name}!", Toast.LENGTH_SHORT).show()

                // Переход на HomeFragment с использованием NavController
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            } else {
                Toast.makeText(activity, "Неверные данные", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUpButton?.setOnClickListener {
            // Переход на SignUpFragment с использованием NavController и передачей текущего списка пользователей
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment(
                registeredUsers.toTypedArray()
            )
            findNavController().navigate(action)
        }

        return binding?.root // Возврат корневого представления из View Binding
    }
}
