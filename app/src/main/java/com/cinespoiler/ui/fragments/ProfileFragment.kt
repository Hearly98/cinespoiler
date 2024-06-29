package com.cinespoiler.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.cinespoiler.MainActivity
import com.cinespoiler.R

class ProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)

        val userName = sharedPreferences.getString("user_name", "")
        val userEmail = sharedPreferences.getString("user_email", "")
        val userGender = sharedPreferences.getString("user_gender", "")
        val birthdate = sharedPreferences.getString("user_birthdate", "")
        val password = sharedPreferences.getString("user_password", "")


        val nameEditText = view.findViewById<EditText>(R.id.etNombreProfile)
        val emailEditText = view.findViewById<EditText>(R.id.etEmailProfile)
        val genderSpinner = view.findViewById<Spinner>(R.id.spinnerGeneroProfile)
        val btnCerrarSesion = view.findViewById<Button>(R.id.btnCerrarSesion)
        val birthDateEditText = view.findViewById<EditText>(R.id.etFechNacProfile)
        val passwordEditText = view.findViewById<EditText>(R.id.etPasswordProfile)

        nameEditText.setText(userName)
        emailEditText.setText(userEmail)
        birthDateEditText.setText(birthdate)
        passwordEditText.setText(password)

        val genders = resources.getStringArray(R.array.genders_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        val genderPosition = genders.indexOf(userGender)
        if (genderPosition >= 0) {
            genderSpinner.setSelection(genderPosition)
        }

        btnCerrarSesion.setOnClickListener {
            logout()
        }

        return view
    }

    private fun logout() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }
}
