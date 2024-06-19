package com.cinespoiler

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Food")
data class Food (
    @PrimaryKey(autoGenerate = true )
    val id : Int = 0,
    val name : String,
    val description : String,
    val price: Int = 0,
    var isFavorite: Boolean = false
)



