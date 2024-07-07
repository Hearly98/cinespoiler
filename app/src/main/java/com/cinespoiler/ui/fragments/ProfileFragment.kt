package com.cinespoiler.ui.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cinespoiler.MainActivity
import com.cinespoiler.R
import com.cinespoiler.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.io.File
import java.io.FileInputStream
import android.widget.Toast


class ProfileFragment : Fragment() {
    companion object{
        private const val REQUEST_IMAGE = 100
    }
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var storage: FirebaseStorage

    //Agregado para actualizar datos del usuario
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null && data.data != null) {
                val selectedImgUri: Uri = data.data!!
                uploadImage(selectedImgUri)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        storage = Firebase.storage
        sharedPreferences = requireActivity().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        // llamando a auth y db
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        //
        val userName = sharedPreferences.getString("user_name", "")
        val userEmail = sharedPreferences.getString("user_email", "")
        val userGender = sharedPreferences.getString("user_gender", "")
        val birthdate = sharedPreferences.getString("user_birthdate", "")
        val password = sharedPreferences.getString("user_password", "")
        val imgUri = sharedPreferences.getString("user_img", "")
        val nameEditText = view.findViewById<EditText>(R.id.etNombreProfile)
        val emailEditText = view.findViewById<EditText>(R.id.etEmailProfile)
        val genderSpinner = view.findViewById<Spinner>(R.id.spinnerGeneroProfile)
        val btnCerrarSesion = view.findViewById<Button>(R.id.btnCerrarSesion)
        val birthDateEditText = view.findViewById<EditText>(R.id.etFechNacProfile)
        val passwordEditText = view.findViewById<EditText>(R.id.etPasswordProfile)
        val imgView = view.findViewById<ImageView>(R.id.imgProfile)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)

        // Deshabilitar la edición del email y contraseña
        emailEditText.isEnabled = false
        passwordEditText.isEnabled = false

        // Guardar cambios en el perfil de usuario
        btnGuardar.setOnClickListener {
            saveUserProfile(nameEditText, genderSpinner, birthDateEditText)
        }

        nameEditText.setText(userName)
        emailEditText.setText(userEmail)
        birthDateEditText.setText(birthdate)
        passwordEditText.setText(password)

        // Cargar imagen del perfil utilizando Glide
        if (imgUri.isNullOrEmpty()) {
            imgView.setImageResource(R.drawable.ic_user)
        } else {
            Glide.with(requireContext())
                .load(imgUri)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(imgView)
        }

        val genders = resources.getStringArray(R.array.genders_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        val genderPosition = genders.indexOf(userGender)
        if (genderPosition >= 0) {
            genderSpinner.setSelection(genderPosition)
        }

        btnCerrarSesion.setOnClickListener {
            logout()
        }
        // Configuración del clic de la imagen para seleccionar una nueva imagen
        imgView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getContent.launch(intent)
        }
        return view
    }

    // Función para guardar el perfil de usuario en Firestore
    private fun saveUserProfile(
        nameEditText: EditText, genderSpinner: Spinner, birthDateEditText: EditText
    ) {
        val userId = auth.currentUser?.uid
        val name = nameEditText.text.toString()
        val gender = genderSpinner.selectedItem.toString()
        val birthdate = birthDateEditText.text.toString()
        val email = auth.currentUser?.email

        // Crear una instancia de User con los datos actualizados
        val updatedUser = User(
            name = name,
            gender = gender,
            birthdate = birthdate,
            email = email ?: "",
            password = "", // La contraseña no se actualiza aquí
            rol = "cliente"
        )

        // Guardar los datos actualizados en Firestore
        userId?.let {
            db.collection("users").document(it)
                .set(updatedUser)
                .addOnSuccessListener {
                    Log.d("ProfileFragment", "User profile updated successfully")
                    // Actualizar las SharedPreferences con los datos actualizados
                    with(sharedPreferences.edit()) {
                        putString("user_name", name)
                        putString("user_gender", gender)
                        putString("user_birthdate", birthdate)
                        apply()
                    }
                    // Mostrar mensaje de confirmación
                    Toast.makeText(requireContext(), "Sus datos han sido actualizados", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileFragment", "Error updating user profile", e)
                }
        }
    }


    private fun logout() {
        Log.d("ProfileFragment", "Logout button pressed")

        with(sharedPreferences.edit()) {
            clear()
            apply()
        }

        FirebaseAuth.getInstance().signOut()

        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun uploadImage(imgUri: Uri) {
        val storageRef = storage.reference
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val profileRef = storageRef.child("profile/$userId/profile.jpg")
        val uploadTask = profileRef.putFile(imgUri)
        val imgView = view?.findViewById<ImageView>(R.id.imgProfile)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            profileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                // Actualizar la URL de la imagen en Firestore
                updateUserProfileImage(downloadUri.toString())
                loadImageIntoImageView(downloadUri.toString(), imgView!!)
            } else {
                Log.d("Upload Img", "Error al subir la imagen")
            }
        }
    }

    private fun updateUserProfileImage(imageUrl: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val imgView = view?.findViewById<ImageView>(R.id.imgProfile)
        user?.let {
            db.collection("users").document(it.uid)
                .update("img", imageUrl)
                .addOnSuccessListener {
                    Log.d("Update img", "La imagen ha sido actualizada exitosamente")
                    with(sharedPreferences.edit()) {
                        putString("user_img", imageUrl)
                        apply()
                    }
                    loadImageIntoImageView(imageUrl, imgView!!)
                }
                .addOnFailureListener { error ->
                    Log.w("Update img", "Error al actualizar la imagen", error)
                }
        }

    }
    private fun loadImageIntoImageView(imageUri: String?, imageView: ImageView) {
        if (imageUri.isNullOrEmpty()) {
            imageView.setImageResource(R.drawable.ic_user)
        } else {
            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Invalida la caché original
                .skipMemoryCache(true) // Salta la caché en memoria para asegurar que la nueva imagen se carga
                .into(imageView)
        }
    }

}
