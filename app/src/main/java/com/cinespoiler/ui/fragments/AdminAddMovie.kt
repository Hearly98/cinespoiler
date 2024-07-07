package com.cinespoiler.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinespoiler.R
import com.cinespoiler.adapter.AdminMovieAdapter
import com.cinespoiler.adapter.MovieAdapter
import com.cinespoiler.model.Food
import com.cinespoiler.model.Movie
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminAddMovie : Fragment() {

    private lateinit var movieNameEditText: EditText
    private lateinit var moviePriceEditText: EditText
    private lateinit var movieDescriptionEditText: EditText
    private lateinit var movieImageLinkEditText: EditText
    private lateinit var btnAddMovie: Button
    private lateinit var movieAdapter: AdminMovieAdapter
    private val listMovie = mutableListOf<Movie>()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_add_movie, container, false)



        movieNameEditText = view.findViewById(R.id.tv_movieName)
        moviePriceEditText = view.findViewById(R.id.tv_moviePrice)
        movieDescriptionEditText = view.findViewById(R.id.tv_movieDescription)
        movieImageLinkEditText = view.findViewById(R.id.tv_movieImageLinkAdd)
        btnAddMovie = view.findViewById(R.id.btn_AddMovie)
        movieAdapter = AdminMovieAdapter(listMovie, ::onEditClick, ::onDeleteClick)


        btnAddMovie.setOnClickListener {
            addMovie()
        }

        getMovie()
        return view
    }


    private fun addMovie() {
        val movieName = movieNameEditText.text.toString().trim()
        val moviePrice = moviePriceEditText.text.toString().trim()
        val movieDescription = movieDescriptionEditText.text.toString().trim()
        val movieImageLink = movieImageLinkEditText.text.toString().trim()

        if (movieName.isNotEmpty() && moviePrice.isNotEmpty() && movieDescription.isNotEmpty() && movieImageLink.isNotEmpty()) {
            val movie = Movie(
                movieName = movieName,
                moviePrice =  moviePrice,
                movieDescription =  movieDescription,
                movieImg =  movieImageLink
            )

            db.collection("movies")
                .add(movie)
                .addOnSuccessListener {
                    movieNameEditText.text.clear()
                    moviePriceEditText.text.clear()
                    movieDescriptionEditText.text.clear()
                    movieImageLinkEditText.text.clear()
                    getMovie()
                    Toast.makeText(context, "Pelicula añadida correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al añadir Pelicula: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getMovie() {
        db.collection("movies").get()
            .addOnSuccessListener { documents ->
                listMovie.clear()
                for (document in documents) {
                    val movie = document.toObject<Movie>().apply { movieId = document.id }
                    listMovie.add(movie)
                }
                movieAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { error ->
                Log.e("AdminAddMovie", "Error al obtener los datos: ", error)
            }
    }

    private fun onEditClick(movie: Movie) {
        movieNameEditText.setText(movie.movieName)
        moviePriceEditText.setText(movie.moviePrice)
        movieDescriptionEditText.setText(movie.movieDescription)
        movieImageLinkEditText.setText(movie.movieImg)

        btnAddMovie.setOnClickListener {
            val updatedMovie = Movie(

                movieName = movieNameEditText.text.toString(),
                moviePrice = moviePriceEditText.text.toString(),
                movieDescription = movieDescriptionEditText.text.toString(),
                movieImg = movieImageLinkEditText.text.toString(),
            )
            db.collection("movies").document(movie.movieId).set(updatedMovie)
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Pelicula actualizada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    getMovie()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Error al actualizar pelicula: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

        private fun onDeleteClick(movie: Movie) {
            db.collection("movies").document(movie.movieId).delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Pelicula eliminada correctamente", Toast.LENGTH_SHORT).show()
                    getMovie()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al eliminar Pelicula: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }






