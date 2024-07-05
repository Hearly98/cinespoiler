package com.cinespoiler.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinespoiler.R
import com.cinespoiler.model.Food
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase

class AdminAddFood  : Fragment()  {
    private lateinit var foodNameEditText: EditText
    private lateinit var foodPriceEditText: EditText
    private lateinit var foodDescriptionEditText: EditText
    private lateinit var foodImageLinkEditText: EditText
    private lateinit var btnAddFood: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_add_food, container, false)

        foodNameEditText = view.findViewById(R.id.tv_foodNameAdd)
        foodPriceEditText = view.findViewById(R.id.tv_foodPriceAdd)
        foodDescriptionEditText = view.findViewById(R.id.tv_foodDescriptionAdd)
        foodImageLinkEditText = view.findViewById(R.id.tv_foodImageLinkAdd)
        btnAddFood = view.findViewById(R.id.btn_AddFood)

        btnAddFood.setOnClickListener {
            addFood()
        }

        return view
    }

    private fun addFood() {
        val foodName = foodNameEditText.text.toString().trim()
        val foodPrice = foodPriceEditText.text.toString().trim()
        val foodDescription = foodDescriptionEditText.text.toString().trim()
        val foodImageLink = foodImageLinkEditText.text.toString().trim()

        if (foodName.isNotEmpty() && foodPrice.isNotEmpty() && foodDescription.isNotEmpty() && foodImageLink.isNotEmpty()) {
            val food = Food(
                name = foodName,
                price = foodPrice,
                description = foodDescription,
                isFavorite = false, // O cambiar según tu lógica
                img = foodImageLink
            )

            db.collection("food")
                .add(food)
                .addOnSuccessListener {
                    // Limpiar los campos de texto después de agregar
                    foodNameEditText.text.clear()
                    foodPriceEditText.text.clear()
                    foodDescriptionEditText.text.clear()
                    foodImageLinkEditText.text.clear()
                    // Mostrar mensaje de éxito
                }
                .addOnFailureListener { e ->
                    // Mostrar mensaje de error
                }
        } else {
            // Mostrar mensaje de que todos los campos son obligatorios
        }
    }
}

    /*private fun addFood() {
        val DB = FirebaseFirestore.getInstance()
        val etNameFood = view?.findViewById<EditText>(R.id.tv_foodNameAdd)
        val etPriceFood = view?.findViewById<EditText>(R.id.tv_foodPriceAdd)
        val etDescriptionFood = view?.findViewById<EditText>(R.id.tv_foodDescriptionAdd)
        DB.collection("food").add(listFood).addOnCompleteListener {
                for(document in documents){
                    etNameFood.("food_name")
                }
        }

    }*/




