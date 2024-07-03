package com.cinespoiler

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cinespoiler.ui.RegisterActivity
import com.cinespoiler.ui.admin.AdminActivity
import com.cinespoiler.ui.client.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
//Aqui se manejará la logica de inicio de Sesión
class MainActivity : AppCompatActivity() {

    //SharedPreferences nos permitirá gestionar las sesiones de los usuarios dependiendo su rol
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Al crear se mostrará la vista activity_login
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)
        val rol= sharedPreferences.getString("user_rol", null)

        //Si el cliente no está registrado presionará en el texto registrar
        //El cual activará la funcion navigateToRegister()
        val linkRegister = findViewById<TextView>(R.id.registerTextView)
        linkRegister.setOnClickListener {
            navigateToRegister()
        }
        //Si tiene un usario valido será redirigido a la vista principal dependiendo de su rol
        //Cinespoiler para cliente
        //Cinespoiler para administrador
        if (userId != null && rol != null) {
            navigateBasedOnRole(rol)
        } else {
            setupLogin()
        }
    }

    private fun navigateBasedOnRole(rol: String) {
        when (rol) {
            "admin" -> navigateToAdminHome()
            "cliente" -> navigateToHome()
            else -> setupLogin()
        }
    }

    private fun navigateToAdminHome() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupLogin() {
        title = "Inicio de sesión"

        //Obteniendo id de los EditText y Button de la vista activity_login
        val emailEditText: EditText = findViewById(R.id.et_EmailLogin)
        val passwordEditText: EditText = findViewById(R.id.et_PasswordLogin)
        val btnLogin: Button = findViewById(R.id.btn_Login)

        //Al dar click se iniciará un evento OnClickListener
        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            //Si no está vacío se validarán los datos con la funcion loginUser(parametro Email, parametro Password)
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                //Si está vacío enviará el mensaje de Error
                Toast.makeText(this@MainActivity, "Por favor ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Función que valida los datos del usuario en activity_login con Firebase Authentication
    private fun loginUser(email: String, password: String) {
        //Obtenemos la instantica de FirebaseAuth y pasamos el email y password en .signInWithEmailAndPassword
        //.signInWithEmailAndPassword -> validará que exista un usuario y contraseña en la lista de usuarios de Firebase Authentication
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                //Si los datos colocados son correctos guardamos el user.uid para obtener los datos del usuario que inició sesion
                //Y lo enviamos a la vista navigateToHome()
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        saveUserDetails(sharedPreferences, user.uid) {
                            val rol = sharedPreferences.getString("user_rol", null)
                            if (rol != null) {
                                navigateBasedOnRole(rol)
                            } else {
                                showAlert("Error", "No se pudo determinar el rol del usuario")
                            }
                        }
                    } else {
                        showAlert("Error", "Usuario no encontrado")
                    }
                } else {
                    showAlert("Error", "Inicio de sesión fallido")
                }
            }
    }
    //Metodo que guarda los datos del usuario logeado
    //Esto nos permitirá hacer una comparación con FireStore(base de datos) y poder obtener los datos adicionales del usuario
    //Al obtener los datos del usuario lo colocaremos en la vista Profile (ProfileFragment)
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
                            putString("user_password", document.getString("password"))
                            putString("user_rol", document.getString("rol"))
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
