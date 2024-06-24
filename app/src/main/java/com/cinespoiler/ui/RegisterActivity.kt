package com.cinespoiler.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cinespoiler.Gender
import com.cinespoiler.MainActivity
import com.cinespoiler.R

import com.cinespoiler.dao.UserDao
import com.cinespoiler.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userDao = UserApplication.database.userDao()

        setupGenderSpinner()
        setupRegisterButton()
    }

    private fun setupGenderSpinner() {
        val spinnerGender: Spinner = findViewById(R.id.spinner_Gender)

        val genderList = mutableListOf("Seleccione su género")
        genderList.addAll(Gender.values().map { it.name })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = adapter
    }

    private fun setupRegisterButton() {
        val nameEditText: EditText = findViewById(R.id.et_NameRegister)
        val birthdateEditText: EditText = findViewById(R.id.et_DateRegister)
        val emailEditText: EditText = findViewById(R.id.et_EmailRegister)
        val passwordEditText: EditText = findViewById(R.id.et_PasswordRegister)
        val registerButton: Button = findViewById(R.id.btn_register)

        val nameErrorTextView: TextView = findViewById(R.id.tv_error_name)
        val genderErrorTextView: TextView = findViewById(R.id.tv_error_gender)
        val birthdateErrorTextView: TextView = findViewById(R.id.tv_error_birthdate)
        val emailErrorTextView: TextView = findViewById(R.id.tv_error_email)
        val passwordErrorTextView: TextView = findViewById(R.id.tv_error_password)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val gender = findViewById<Spinner>(R.id.spinner_Gender).selectedItem.toString()
            val birthdate = birthdateEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            var isValid = validateInput(
                name, gender, birthdate, email, password,
                nameErrorTextView, genderErrorTextView, birthdateErrorTextView, emailErrorTextView, passwordErrorTextView
            )

            if (isValid) {
                registerUser(name, gender, birthdate, email, password, nameEditText, birthdateEditText, emailEditText, passwordEditText)
            }
        }
    }

    private fun validateInput(
        name: String, gender: String, birthdate: String, email: String, password: String,
        nameErrorTextView: TextView, genderErrorTextView: TextView, birthdateErrorTextView: TextView,
        emailErrorTextView: TextView, passwordErrorTextView: TextView
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            nameErrorTextView.visibility = View.VISIBLE
            isValid = false
        } else {
            nameErrorTextView.visibility = View.GONE
        }

        if (gender == "Seleccione su género") {
            genderErrorTextView.visibility = View.VISIBLE
            isValid = false
        } else {
            genderErrorTextView.visibility = View.GONE
        }

        if (birthdate.isEmpty()) {
            birthdateErrorTextView.visibility = View.VISIBLE
            isValid = false
        } else {
            birthdateErrorTextView.visibility = View.GONE
        }

        if (email.isEmpty()) {
            emailErrorTextView.visibility = View.VISIBLE
            isValid = false
        } else {
            emailErrorTextView.visibility = View.GONE
        }

        if (password.isEmpty()) {
            passwordErrorTextView.visibility = View.VISIBLE
            isValid = false
        } else {
            passwordErrorTextView.visibility = View.GONE
        }

        return isValid
    }

    private fun registerUser(
        name: String, gender: String, birthdate: String, email: String, password: String,
        nameEditText: EditText, birthdateEditText: EditText, emailEditText: EditText, passwordEditText: EditText
    ) {
        val user = User(0, name, Gender.valueOf(gender), parseDate(birthdate), email, password)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                userDao.insert(user)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                    clearFields(nameEditText, birthdateEditText, emailEditText, passwordEditText)
                    navigateLogin()
                }
                printAllUsers()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error al registrar el usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun parseDate(dateString: String): Date {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.parse(dateString) ?: Date()
    }

    private fun clearFields(vararg editTexts: EditText) {
        for (editText in editTexts) {
            editText.text.clear()
        }
    }

    private suspend fun printAllUsers() {
        val users = userDao.getAllUsers()
        withContext(Dispatchers.IO) {
            users.forEach { user ->
                println("User(id=${user.id}, name=${user.name}, gender=${user.gender}, birthdate=${user.birthdate}, email=${user.email})")
            }
        }
    }
}
