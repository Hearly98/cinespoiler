package com.cinespoiler

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true ) val id : Int,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "gender") val gender : Gender,
    @ColumnInfo(name= "birthdate") val birthdate : Date,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name="password") val password : String
)
