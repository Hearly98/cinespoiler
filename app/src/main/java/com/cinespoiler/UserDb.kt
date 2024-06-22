package com.cinespoiler

import Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class UserDb : RoomDatabase() {
        abstract fun userDao(): UserDao
}