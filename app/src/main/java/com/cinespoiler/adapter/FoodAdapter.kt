package com.cinespoiler.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinespoiler.R
import com.cinespoiler.model.Food

class FoodAdapter(private val foodList: List<Food>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodList[position]
        holder.tvName.text = currentItem.name
        holder.tvDescription.text = currentItem.description
        holder.tvPrice.text = currentItem.price
        if (currentItem.img.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(currentItem.img)
                .into(holder.ivImage)
        }
    }

    override fun getItemCount() = foodList.size

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvNameFood)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescriptionFood)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPriceFood)
        val ivImage: ImageView = itemView.findViewById(R.id.imgPhotoFood)
    }
}
