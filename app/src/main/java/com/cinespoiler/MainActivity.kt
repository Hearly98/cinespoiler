package com.cinespoiler

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cinespoiler.ui.client.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)

        if (userId != null) {
            navigateToHome()
        } else {
            setupLogin()
        }
    }

    private fun setupLogin() {
        title = "Inicio de sesión"

        val emailEditText: EditText = findViewById(R.id.et_EmailLogin)
        val passwordEditText: EditText = findViewById(R.id.et_PasswordLogin)
        val btnLogin: Button = findViewById(R.id.btn_Login)

        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this@MainActivity, "Por favor ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        saveUserDetails(sharedPreferences, user.uid) {
                            navigateToHome()
                        }
                    } else {
                        showAlert("Error", "Usuario no encontrado")
                    }
                } else {
                    showAlert("Error", "Inicio de sesión fallido")
                }
            }
    }

    private fun saveUserDetails(sharedPreferences: SharedPreferences, userId: String, onComplete: () -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        with(sharedPreferences.edit()) {
                            putString("user_id", userId)
                            putString("user_email", document.getString("email"))
                            putString("user_birthdate", document.getString("birthdate"))
                            putString("user_gender", document.getString("gender"))
                            putString("user_name", document.getString("name"))
                            apply()
                        }

                        Log.d("Firestore", "Datos del usuario guardados correctamente en SharedPreferences.")
                    } else {
                        Log.d("Firestore", "No existe tal documento.")
                    }
                } else {
                    Log.d("Firestore", "Error al obtener los documentos: ", task.exception)
                }
                onComplete()
            }
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun navigateToHome() {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
        finish()
    }
}
