package com.cinespoiler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinespoiler.R
import com.cinespoiler.model.Movie

class AdminMovieAdapter(
    private val movieList: List<Movie>,
    private val onEditClick: (Movie) -> Unit,
    private val onDeleteClick: (Movie) -> Unit
) : RecyclerView.Adapter<AdminMovieAdapter.AdminMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminMovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_admin_add_movie, parent, false)
        return AdminMovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdminMovieViewHolder, position: Int) {
        val currentItem = movieList[position]
        holder.tvName.text = currentItem.movieName
        holder.tvDescription.text = currentItem.movieDescription
        holder.tvPrice.text = currentItem.moviePrice
        if (currentItem.movieImg.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(currentItem.movieImg)
                .into(holder.ivImage)
        }

        holder.btnEdit.setOnClickListener { onEditClick(currentItem) }
        holder.btnDelete.setOnClickListener { onDeleteClick(currentItem) }
    }

    override fun getItemCount() = movieList.size

    class AdminMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_movieName)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_movieDescription)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_moviePrice)
        val ivImage: ImageView = itemView.findViewById(R.id.imageView)
        val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        val btnDelete: Button = itemView.findViewById(R.id.btn_delete)
    }
}
