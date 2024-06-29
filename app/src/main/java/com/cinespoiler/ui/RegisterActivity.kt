                                    package com.cinespoiler.ui

                                    import android.content.Intent
                                    import android.os.Bundle
                                    import android.view.View
                                    import android.widget.*
                                    import androidx.appcompat.app.AlertDialog
                                    import androidx.appcompat.app.AppCompatActivity
                                    import androidx.lifecycle.lifecycleScope
                                    import com.cinespoiler.Gender
                                    import com.cinespoiler.MainActivity
                                    import com.cinespoiler.R

                                    //import com.cinespoiler.dao.UserDao
                                    import com.cinespoiler.model.User
                                    import com.cinespoiler.ui.client.HomeActivity
                                    import com.cinespoiler.ui.client.ProviderType
                                    import com.google.firebase.auth.FirebaseAuth
                                    import com.google.firebase.auth.UserProfileChangeRequest
                                    import com.google.firebase.firestore.FirebaseFirestore
                                    import kotlinx.coroutines.Dispatchers
                                    import kotlinx.coroutines.launch
                                    import kotlinx.coroutines.withContext
                                    import java.text.SimpleDateFormat
                                    import java.util.*

                                    class RegisterActivity : AppCompatActivity() {
                                        //private lateinit var userDao: UserDao

                                        override fun onCreate(savedInstanceState: Bundle?) {
                                            super.onCreate(savedInstanceState)
                                            setContentView(R.layout.activity_register)

                                           // userDao = UserApplication.database.userDao()

                                            setupGenderSpinner()
                                            setUpRegister()
                                        }

                                        private fun setupGenderSpinner() {
                                            val spinnerGender: Spinner = findViewById(R.id.spinner_Gender)

                                            val genderList = mutableListOf("Seleccione su género")
                                            genderList.addAll(Gender.values().map { it.name })

                                            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList)
                                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                            spinnerGender.adapter = adapter
                                        }
                                        private fun setUpRegister() {
                                            title = "Autenticacion"
                                            //Se declaran todos los editText para el registro
                                            //Se declaran todos los textView para los mensajes de error
                                            val nameEditText: EditText = findViewById(R.id.et_NameRegister)
                                            val gender: Spinner = findViewById(R.id.spinner_Gender)
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
                                                val email = emailEditText.text.toString()
                                                val password = passwordEditText.text.toString()

                                                if (validateInput(
                                                        nameEditText.text.toString(),
                                                        gender.selectedItem.toString(),
                                                        birthdateEditText.text.toString(),
                                                        email,
                                                        password,
                                                        nameErrorTextView,
                                                        genderErrorTextView,
                                                        birthdateErrorTextView,
                                                        emailErrorTextView,
                                                        passwordErrorTextView
                                                    )
                                                ) {
                                                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                                        .addOnCompleteListener { task ->
                                                            if (task.isSuccessful) {
                                                                val user = FirebaseAuth.getInstance().currentUser
                                                                val profileUpdates = UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(nameEditText.text.toString())
                                                                    .build()
                                                                user?.updateProfile(profileUpdates)
                                                                    ?.addOnCompleteListener { profileUpdateTask ->
                                                                        if (profileUpdateTask.isSuccessful) {
                                                                            // Guardar datos adicionales en Firestore
                                                                            val db = FirebaseFirestore.getInstance()
                                                                            val userData = User(
                                                                                nameEditText.text.toString(),
                                                                                gender.selectedItem.toString(),
                                                                                birthdateEditText.text.toString(),
                                                                                emailEditText.text.toString(),
                                                                                passwordEditText.text.toString())
                                                                            db.collection("users")
                                                                                .document(user.uid)
                                                                                .set(userData)
                                                                                .addOnSuccessListener {
                                                                                    navigateToLogin(email, ProviderType.BASIC)
                                                                                }
                                                                                .addOnFailureListener { e ->
                                                                                    showAlert()
                                                                                }
                                                                        } else {
                                                                            showAlert()
                                                                        }
                                                                    }
                                                            } else {
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

                                        private fun navigateToLogin(email: String, provider: ProviderType){
                                            val homeIntent = Intent(this, MainActivity::class.java).apply {
                                                putExtra("email", email)
                                                putExtra("provider", provider)
                                            }
                                            startActivity(homeIntent)
                                            finish()
                                        }
                                    }
