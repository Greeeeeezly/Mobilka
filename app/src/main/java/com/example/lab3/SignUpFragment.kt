package com.example.lab3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class SignUpFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registeredUsers: MutableList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        registeredUsers = arguments?.getSerializable("registeredUsers") as? MutableList<User> ?: mutableListOf()

        nameEditText = view.findViewById(R.id.nameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)

        val registerButton = view.findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val newUser = User(name, email, password)
            registeredUsers.add(newUser)

            Toast.makeText(activity, "Пользователь $name зарегистрирован!", Toast.LENGTH_SHORT).show()

            (activity as MainActivity).replaceFragment(SignInFragment().apply {
                arguments = Bundle().apply {
                    putString("registeredName", name)
                    putSerializable("registeredUsers", ArrayList(registeredUsers))
                }
            })
        }

        return view
    }
}

