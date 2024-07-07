package com.cinespoiler.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinespoiler.EntradasFragment
import com.cinespoiler.R
import com.cinespoiler.adapter.MovieAdapter
import com.cinespoiler.model.Movie
import com.google.firebase.firestore.FirebaseFirestore

class MovieDetailsFragment : Fragment() {
    private val listMovie = mutableListOf<Movie>()
    private lateinit var movieImg: ImageView
    private lateinit var movieName: TextView
    private lateinit var movieDescription: TextView
    private lateinit var moviePrice: TextView
    private lateinit var regresar: ImageView
    private lateinit var btn_entradas: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_movie_details, container, false)

         movieImg = view.findViewById(R.id.imgDetailCartelera)
         movieName = view.findViewById(R.id.tvDetailmovieName)
         movieDescription= view.findViewById(R.id.tvDetailDescription)
         moviePrice = view.findViewById(R.id.tvDetailPrice)
         regresar = view.findViewById(R.id.btnRegresar)
         regresar.setOnClickListener { parentFragmentManager.popBackStack()}
        btn_entradas = view.findViewById(R.id.btnEntrada)
        btn_entradas.setOnClickListener {
            val movieId = arguments?.getString("movieId") ?: return@setOnClickListener
            val fragment = EntradasFragment.newInstance(movieId)
            parentFragmentManager.beginTransaction()
                .replace(R.id.hostFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = arguments?.getString("movieId")?: return
        loadMovieDetails(movieId)
    }

    private fun loadMovieDetails(movieId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("movies").document(movieId).get()
            .addOnSuccessListener {
                document ->
                if(document != null ){
                    val img = document.getString("movieImg")
                    val name = document.getString("movieName")
                    val description = document.getString("movieDescription")
                    val price = document.getDouble("moviePrice").toString()

                    movieName.text = name
                    movieDescription.text = description
                    moviePrice.text = price
                    Glide.with(
                        this,
                    ).load(img).into(movieImg)
                }
            }.addOnFailureListener{
                Log.e("Cargar detalle pelicula", "Ha ocurrido un error al obterner los datoos")
            }
    }
    companion object{
            fun newInstance(movieId: String): MovieDetailsFragment {
                val fragment = MovieDetailsFragment()
                val args = Bundle()
                args.putString("movieId", movieId)
                fragment.arguments = args
                return fragment
            }


    }
}