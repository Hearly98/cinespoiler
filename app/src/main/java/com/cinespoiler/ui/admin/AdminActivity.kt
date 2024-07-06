package com.cinespoiler.ui.admin


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cinespoiler.R
import com.cinespoiler.databinding.ActivityAdminBinding
import com.cinespoiler.ui.fragments.*

class AdminActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityAdminBinding
    private lateinit var mActiveFragment: Fragment
    private lateinit var mFragment: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        mBinding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpNavMenu()
    }

    private fun setUpNavMenu(){
        var mFragmentManager = supportFragmentManager
        val addMovieFragment = AdminAddMovie()
        val addFoodFragment = AdminAddFood()
        val profileFragment = ProfileFragment()
        mActiveFragment = addMovieFragment
        mFragmentManager.beginTransaction()
            .add(
                R.id.hostFragmentAdmin,
                profileFragment, ProfileFragment::class.java.name)
            .hide(profileFragment).commit()
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
                R.id.item_addFood-> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(addFoodFragment).commit()
                    mActiveFragment = addFoodFragment
                    true
                }
                R.id.item_addMovie -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(addMovieFragment).commit()
                    mActiveFragment = addMovieFragment
                    true
                }
                R.id.item_Profile ->{
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