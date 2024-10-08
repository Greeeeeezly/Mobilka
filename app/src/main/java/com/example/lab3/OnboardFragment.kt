package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class OnboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboard, container, false)
        val descriptionTextView = view.findViewById<TextView>(R.id.descriptionTextView)
        descriptionTextView.text = "Это Можнограм, можно всё, что не нельзя!"
        val nextButton = view.findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            (activity as MainActivity).replaceFragment(SignInFragment())
        }

        return view
    }
}

