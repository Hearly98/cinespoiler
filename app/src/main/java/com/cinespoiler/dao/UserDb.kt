package com.cinespoiler.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cinespoiler.Converters
import com.cinespoiler.model.User
/*
@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class UserDb : RoomDatabase() {
        abstract fun userDao(): UserDao
}*/