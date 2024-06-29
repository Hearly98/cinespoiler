package com.cinespoiler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cinespoiler.ui.client.HomeActivity
import com.cinespoiler.ui.RegisterActivity
import com.cinespoiler.ui.client.ProviderType
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val linkRegister = findViewById<TextView>(R.id.registerTextView)
        linkRegister.setOnClickListener {
            navigateToRegister()
        }

        val btnLogin = findViewById<Button>(R.id.btn_Login)
        btnLogin.setOnClickListener {
            val bundle = intent.extras
            val email = bundle?.getString("email")
            val provider = bundle?.getString("provider")
            setUp(email?: "", provider ?: "")
        }
    }



    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setUp(email: String, provider: String) {
        title = "Inicio"
        val emailEditText: EditText = findViewById(R.id.et_EmailLogin)
        val passwordEditText: EditText = findViewById(R.id.et_PasswordLogin)
        val btn_login = findViewById<Button>(R.id.btn_Login)

        btn_login.setOnClickListener {
            if(emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            navigateToHome(it.result?.user?.email?: "", ProviderType.BASIC)
                        }else{
                            showAlert()
                        }
                    }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun navigateToHome(email: String, provider: ProviderType){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider)
        }
        startActivity(homeIntent)
        finish()
    }
}
