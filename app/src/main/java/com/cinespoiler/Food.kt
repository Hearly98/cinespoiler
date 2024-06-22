package com.cinespoiler

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Food")
data class Food (
    @PrimaryKey(autoGenerate = true )
    val id : Int = 0,
    var name : String,
    var price: Int = 0,
    var isFavorite: Boolean = false
)



