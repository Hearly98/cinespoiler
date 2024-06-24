package com.cinespoiler.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cinespoiler.dao.FoodDao
import com.cinespoiler.model.Food

@Database(entities = arrayOf(Food::class), version = 1)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}