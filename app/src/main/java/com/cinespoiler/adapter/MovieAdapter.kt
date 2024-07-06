package com.cinespoiler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinespoiler.R
import com.cinespoiler.model.Food
import com.cinespoiler.model.Movie

class MovieAdapter(private val movieList: List<Movie>,
                   private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cartelera, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = movieList[position]
        if (currentItem.movieImg.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(currentItem.movieImg)
                .into(holder.ivMovie)
        }
        holder.itemView.setOnClickListener { onItemClick(currentItem.movieId) }
    }

    override fun getItemCount() = movieList.size

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivMovie: ImageView = itemView.findViewById(R.id.imgPhotoCartelera)
    }


}