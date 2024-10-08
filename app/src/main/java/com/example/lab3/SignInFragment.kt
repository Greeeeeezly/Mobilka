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

class SignInFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registeredUsers: MutableList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signin, container, false)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)

        registeredUsers = arguments?.getSerializable("registeredUsers") as? MutableList<User> ?: mutableListOf()

        val signInButton = view.findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val user = registeredUsers.find { it.email == email && it.password == password }
            if (user != null) {
                Toast.makeText(activity, "Добро пожаловать, ${user.name}!", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).replaceFragment(HomeFragment())
            } else {
                Toast.makeText(activity, "Неверные данные", Toast.LENGTH_SHORT).show()
            }
        }

        val signUpButton = view.findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            (activity as MainActivity).replaceFragment(SignUpFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("registeredUsers", ArrayList(registeredUsers))
                }
            })
        }

        return view
    }
}

