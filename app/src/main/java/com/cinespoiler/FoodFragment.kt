package com.cinespoiler

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cinespoiler.databinding.FragmentFoodBinding
import com.cinespoiler.databinding.ItemFoodBinding
import com.cinespoiler.model.Food
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class FoodFragment : Fragment() {

    private lateinit var mBinding: FragmentFoodBinding

    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Food, SnapshotHolder>

    // Hay que configurar nuestro RecyclerView para Firebase
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFoodBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseDatabase.getInstance().reference.child("snapshots")

        val options = FirebaseRecyclerOptions.Builder<Food>()
            .setQuery(query, Food::class.java).build()

        mFirebaseAdapter = object  : FirebaseRecyclerAdapter<Food, SnapshotHolder>(options) {
            private lateinit var mContext: Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapshotHolder {
                mContext = parent.context

                val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_food, parent, false)
                return SnapshotHolder(view)
            }

            override fun onBindViewHolder(holder: SnapshotHolder, position: Int, model: Food) {
                val food = getItem(position)

                with(holder) {
                    setListener(food)

                    binding.tvNameFood.text = food.name
                    binding.tvDescription.text = food.description
                    binding.tvPrice.text = food.price.toString()
                    Glide.with(mContext)
                        .load(food.imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding.imgPhoto)
                }
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show()
            }

        }

        mLayoutManager = LinearLayoutManager(context)

        // Configurando el RecyclerView
        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }

    // Creando el ViewHolder
    inner class SnapshotHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemFoodBinding.bind(view)

        fun setListener(food: Food) {
            // Puedes agregar acciones en esta función si necesitas.
        }
    }
}
