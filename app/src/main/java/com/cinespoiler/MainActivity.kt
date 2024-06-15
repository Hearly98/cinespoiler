package com.cinespoiler

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var db: UserDb
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = Room.databaseBuilder(
            applicationContext,
            UserDb::class.java, "Cinespoiler"
        ).build()
        userDao = db.userDao()

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

            GlobalScope.launch {
                userDao.insert(user)
            }
        }
    }

    private fun parseDate(dateString: String): Date {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.parse(dateString) ?: Date()
    }
}