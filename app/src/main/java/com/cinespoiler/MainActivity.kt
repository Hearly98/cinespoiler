package com.cinespoiler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cinespoiler.ui.HomeActivity
import com.cinespoiler.ui.RegisterActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val linkRegister = findViewById<TextView>(R.id.registerTextView)
        linkRegister.setOnClickListener {
                        navigateToRegister()
                    }
        val btnLogin = findViewById<Button>(R.id.btn_Login)
        btnLogin.setOnClickListener{
                loginValidation()
        }
        }


    private fun navigateToRegister(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun loginValidation(){
        //validacion del login


        //termina validacion
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    }



