package com.cinespoiler.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinespoiler.R
import com.cinespoiler.adapter.MovieAdapter
import com.cinespoiler.databinding.ActivityMainBinding
import com.cinespoiler.model.Movie
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

class MoviesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: MovieAdapter
    private lateinit var mGridLayout: GridLayoutManager
    private val listMovie = mutableListOf<Movie>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movies, container, false)
        recyclerView = view?.findViewById(R.id.recyclerViewMovie)!!
        mGridLayout = GridLayoutManager(context, 2)
        mAdapter = MovieAdapter(listMovie)
        recyclerView.apply{
            adapter = mAdapter
            layoutManager = mGridLayout
        }

        getMovie()
        return view
    }
     private fun getMovie(){
          val DB = FirebaseFirestore.getInstance()
         DB.collection("movies")
              .get().addOnSuccessListener { documents ->
             listMovie.clear()
                 listMovie.addAll(documents.toObjects(Movie::class.java))
                 mAdapter.notifyDataSetChanged()

          }.addOnFailureListener {
                  error -> Log.e("MovieFragment", "Error al obtener los datos: ", error)
          }
      }

}