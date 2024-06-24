package com.cinespoiler.ui

import android.app.Application
import androidx.room.Room
import com.cinespoiler.dao.FoodDatabase

class FoodApplication : Application() {
    companion object{
        lateinit var database: FoodDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this,
            FoodDatabase::class.java,
            "Food")
            .build()
    }
}