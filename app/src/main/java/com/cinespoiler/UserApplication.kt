package com.cinespoiler

import android.app.Application
import androidx.room.Room

class UserApplication : Application() {
    companion object {
        lateinit var database: UserDb
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            UserDb::class.java,
            "Cinespoiler"
        ).build()
    }
}
