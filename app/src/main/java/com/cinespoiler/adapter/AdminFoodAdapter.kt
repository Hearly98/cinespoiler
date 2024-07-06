package com.cinespoiler.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinespoiler.R
import com.cinespoiler.model.Food

class AdminFoodAdapter(
    private val foodList: List<Food>,
    private val onEditClick: (Food) -> Unit,
    private val onDeleteClick: (Food) -> Unit
) : RecyclerView.Adapter<AdminFoodAdapter.AdminFoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminFoodViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_food_admin, parent, false)
        return AdminFoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminFoodViewHolder, position: Int) {
        val currentItem = foodList[position]
        holder.tvName.text = currentItem.name
        holder.tvDescription.text = currentItem.description
        holder.tvPrice.text = currentItem.price
        if (currentItem.img.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(currentItem.img)
                .into(holder.ivImage)
        }

        holder.btnEdit.setOnClickListener { onEditClick(currentItem) }
        holder.btnDelete.setOnClickListener { onDeleteClick(currentItem) }
    }

    override fun getItemCount() = foodList.size

    class AdminFoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvNameFood)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescriptionFood)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPriceFood)
        val ivImage: ImageView = itemView.findViewById(R.id.imgPhotoFood)
        val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        val btnDelete: Button = itemView.findViewById(R.id.btn_delete)
    }
}
