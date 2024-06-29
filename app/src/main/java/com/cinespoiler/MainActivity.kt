package com.cinespoiler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cinespoiler.ui.client.HomeActivity
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

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        if (userId != -1) {
            navigateToHome()
        } else {
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

                    val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("user_id", user.id)
                    editor.putString("user_name", user.name)
                    editor.putString("user_email", user.email)
                    editor.putString("user_gender", user.gender.name)
                    editor.putString("user_birthdate", user.birthdate.toString())
                    editor.putString("user_password", user.password)
                    editor.apply()

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

    private fun logout() {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
