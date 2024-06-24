package com.cinespoiler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cinespoiler.ui.HomeActivity
import com.cinespoiler.ui.RegisterActivity
import com.cinespoiler.dao.UserDao
import com.cinespoiler.ui.UserApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userDao = UserApplication.database.userDao()

        val linkRegister = findViewById<TextView>(R.id.registerTextView)
        linkRegister.setOnClickListener {
            navigateToRegister()
        }

        val btnLogin = findViewById<Button>(R.id.btn_Login)
        btnLogin.setOnClickListener {
            loginValidation()
        }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginValidation() {
        val emailEditText: EditText = findViewById(R.id.et_EmailLogin)
        val passwordEditText: EditText = findViewById(R.id.et_PasswordLogin)
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "El campo de correo electrónico está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "El campo de contraseña está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val user = userDao.login(email, password)

            withContext(Dispatchers.Main) {
                if (user != null) {
                    Toast.makeText(this@MainActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    Toast.makeText(this@MainActivity, "Correo o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
