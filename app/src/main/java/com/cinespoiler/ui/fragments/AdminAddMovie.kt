package com.cinespoiler.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cinespoiler.ui.fragments.FoodFragment
import com.cinespoiler.ui.fragments.HomeFragment
import com.cinespoiler.ui.fragments.MoviesFragment
import com.cinespoiler.ProfileFragment
import com.cinespoiler.R

class AdminAddMovie : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_add_movie, container, false)
    }
}