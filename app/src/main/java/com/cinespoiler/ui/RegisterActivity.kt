package com.cinespoiler.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
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
import java.util.Date
import java.util.Locale

class RegisterActivity: AppCompatActivity() {
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Aquí accedemos a la base de datos desde UserApplication
        userDao = UserApplication.database.userDao()

        val spinnerGender: Spinner = findViewById(R.id.spinner_Gender)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Gender.values())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = adapter

        val nameEditText: EditText = findViewById(R.id.et_NameRegister)
        val birthdateEditText: EditText = findViewById(R.id.et_DateRegister)
        val emailEditText: EditText = findViewById(R.id.et_EmailRegister)
        val passwordEditText: EditText = findViewById(R.id.et_PasswordRegister)
        val registerButton: Button = findViewById(R.id.btn_register)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val gender = spinnerGender.selectedItem as Gender
            val birthdate = parseDate(birthdateEditText.text.toString())
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val user = User(0, name, gender, birthdate, email, password)

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    userDao.insert(user)
                    withContext(Dispatchers.Main) {
                        // Mostrar un Toast en el hilo principal para indicar éxito
                        Toast.makeText(this@RegisterActivity, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                        // Limpiar campos o realizar otras acciones en la UI si es necesario
                        clearFields(nameEditText, birthdateEditText, emailEditText, passwordEditText)
                        navigateLogin()
                    }
                    // Imprimir todos los usuarios en la terminal
                    printAllUsers()

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        // Mostrar un Toast en el hilo principal para indicar error
                        Toast.makeText(this@RegisterActivity, "Error al registrar el usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
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