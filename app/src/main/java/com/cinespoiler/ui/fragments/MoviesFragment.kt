package com.cinespoiler.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.cinespoiler.R
import com.cinespoiler.adapter.MovieAdapter
import com.cinespoiler.databinding.ActivityMainBinding

class MoviesFragment : Fragment() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: MovieAdapter
    private lateinit var mGridLayout: GridLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

}