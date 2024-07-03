package com.cinespoiler.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinespoiler.R
import com.cinespoiler.model.Food
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase

class AdminAddFood : Fragment() {
    private lateinit var foodAdapter: FoodAdapter
    private val listFood = mutableListOf<Food>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       //Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_admin_add_food, container, false)
        foodAdapter = FoodAdapter(listFood)
        val btnAddFood = view.findViewById<Button>(R.id.btn_AddFood)
       /* btnAddFood.setOnClickListener {
            addFood()
        }*/

        return view
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
*/
    }




