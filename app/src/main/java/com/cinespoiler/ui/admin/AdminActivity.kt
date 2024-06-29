package com.cinespoiler.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cinespoiler.R
import com.cinespoiler.databinding.ActivityMainBinding
import com.cinespoiler.ui.fragments.AdminAddFood
import com.cinespoiler.ui.fragments.AdminAddMovie

class AdminActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mActiveFragment: Fragment
    private lateinit var mFragment: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpNavMenu()
    }

    private fun setUpNavMenu(){
        var mFragmentManager = supportFragmentManager
        val addMovieFragment = AdminAddMovie()
        val addFoodFragment = AdminAddFood()
        mActiveFragment = addMovieFragment
        mFragmentManager.beginTransaction()
            .add(
                R.id.hostFragmentAdmin,
                addFoodFragment, AdminAddFood::class.java.name)
            .hide(addFoodFragment).commit()
        mFragmentManager.beginTransaction()
            .add(
                R.id.hostFragmentAdmin,
                addMovieFragment, AdminAddMovie::class.java.name)
            .commit()


        mBinding.btnv.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.i_home -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(addFoodFragment).commit()
                    mActiveFragment = addFoodFragment
                    true
                }
                R.id.i_movie -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(addMovieFragment).commit()
                    mActiveFragment = addMovieFragment
                    true
                }
                else -> false
            }

        }
    }
}