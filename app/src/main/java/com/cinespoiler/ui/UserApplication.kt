package com.cinespoiler.ui

import android.app.Application
import androidx.room.Room
// import com.cinespoiler.dao.UserDb

class UserApplication : Application() {

    companion object {
      //  lateinit var database: UserDb
    }

    override fun onCreate() {
        super.onCreate()
       /* database = Room.databaseBuilder(
            applicationContext,
            UserDb::class.java,
            "cinespoiler"
        ).build()*/
    }
}
