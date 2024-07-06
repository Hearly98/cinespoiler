package com.cinespoiler.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinespoiler.R
import com.cinespoiler.model.Food
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class FoodFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var foodAdapter: FoodAdapter
    private val listFood = mutableListOf<Food>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_food, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewFood)
        recyclerView.layoutManager = LinearLayoutManager(context)
        foodAdapter = FoodAdapter(listFood)
        recyclerView.adapter = foodAdapter
        getFood()

        return view
    }

    private fun getFood() {
        // Obteniendo la base de datos
        val db = FirebaseFirestore.getInstance()
        val foodCollection = db.collection("food")

        foodCollection.get()
            .addOnSuccessListener { documents ->
                val foods = documents.toObjects<Food>()
                listFood.clear()
                listFood.addAll(foods)
                foodAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { error ->
                Log.e("FoodFragment", "Error al obtener los datos: ", error)
            }
    }
}
