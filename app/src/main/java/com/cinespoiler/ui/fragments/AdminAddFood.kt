package com.cinespoiler.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinespoiler.R
import com.cinespoiler.model.Food
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminAddFood : Fragment() {
    private lateinit var foodNameEditText: EditText
    private lateinit var foodPriceEditText: EditText
    private lateinit var foodDescriptionEditText: EditText
    private lateinit var foodImageLinkEditText: EditText
    private lateinit var btnAddFood: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodAdapter: AdminFoodAdapter
    private val listFood = mutableListOf<Food>()
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
        recyclerView = view.findViewById(R.id.recyclerViewFoodAdmin)

        recyclerView.layoutManager = LinearLayoutManager(context)
        foodAdapter = AdminFoodAdapter(listFood, ::onEditClick, ::onDeleteClick)
        recyclerView.adapter = foodAdapter

        btnAddFood.setOnClickListener {
            addFood()
        }

        getFood()
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
                    foodNameEditText.text.clear()
                    foodPriceEditText.text.clear()
                    foodDescriptionEditText.text.clear()
                    foodImageLinkEditText.text.clear()
                    getFood()
                    Toast.makeText(context, "Comida añadida correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al añadir comida: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFood() {
        db.collection("food").get()
            .addOnSuccessListener { documents ->
                listFood.clear()
                for (document in documents) {
                    val food = document.toObject<Food>().apply { id = document.id }
                    listFood.add(food)
                }
                foodAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { error ->
                Log.e("AdminAddFood", "Error al obtener los datos: ", error)
            }
    }

    private fun onEditClick(food: Food) {
        foodNameEditText.setText(food.name)
        foodPriceEditText.setText(food.price)
        foodDescriptionEditText.setText(food.description)
        foodImageLinkEditText.setText(food.img)

        btnAddFood.setOnClickListener {
            val updatedFood = Food(
                id = food.id,
                name = foodNameEditText.text.toString(),
                price = foodPriceEditText.text.toString(),
                description = foodDescriptionEditText.text.toString(),
                isFavorite = false,
                img = foodImageLinkEditText.text.toString()
            )
            db.collection("food").document(food.id).set(updatedFood)
                .addOnSuccessListener {
                    Toast.makeText(context, "Comida actualizada correctamente", Toast.LENGTH_SHORT).show()
                    getFood()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al actualizar comida: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun onDeleteClick(food: Food) {
        db.collection("food").document(food.id).delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Comida eliminada correctamente", Toast.LENGTH_SHORT).show()
                getFood()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al eliminar comida: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
