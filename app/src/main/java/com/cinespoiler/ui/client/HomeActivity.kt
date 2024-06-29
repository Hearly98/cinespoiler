package com.cinespoiler.ui.client

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cinespoiler.ui.fragments.FoodFragment
import com.cinespoiler.ui.fragments.HomeFragment
import com.cinespoiler.ui.fragments.MoviesFragment
import com.cinespoiler.ui.fragments.ProfileFragment
import com.cinespoiler.R
import com.cinespoiler.databinding.ActivityMainBinding

class HomeActivity: AppCompatActivity() {

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
        val homeFragment = HomeFragment()
        val foodFragment = FoodFragment()
        val moviesFragment = MoviesFragment()
        val profileFragment = ProfileFragment()
        mActiveFragment = homeFragment

        mFragmentManager.beginTransaction()
            .add(R.id.hostFragment,
                profileFragment, ProfileFragment::class.java.name)
            .hide(profileFragment).commit()
        mFragmentManager.beginTransaction()
            .add(R.id.hostFragment,
                foodFragment, FoodFragment::class.java.name)
            .hide(foodFragment).commit()
        mFragmentManager.beginTransaction()
            .add(R.id.hostFragment,
                moviesFragment, MoviesFragment::class.java.name)
            .hide(moviesFragment).commit()
        mFragmentManager.beginTransaction()
            .add(R.id.hostFragment,
                homeFragment, HomeFragment::class.java.name)
            .commit()


        mBinding.btnv.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.i_home -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(homeFragment).commit()
                    mActiveFragment = homeFragment
                    true
                }
                R.id.i_movie -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(moviesFragment).commit()
                    mActiveFragment = moviesFragment
                    true
                }
                R.id.i_dulceria -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(foodFragment).commit()
                    mActiveFragment = foodFragment
                    true
                }
                R.id.i_profile -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(profileFragment).commit()
                    mActiveFragment = profileFragment
                    true
                }
                else -> false
            }

        }
    }
    }

