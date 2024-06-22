package com.cinespoiler

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = arrayOf(Food::class), version = 1)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}