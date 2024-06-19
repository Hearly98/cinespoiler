package com.cinespoiler

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true )
    val id : Int = 0,
    val name : String,
    val gender : Gender,
    val birthdate : Date,
    val email : String,
    val password : String
)
