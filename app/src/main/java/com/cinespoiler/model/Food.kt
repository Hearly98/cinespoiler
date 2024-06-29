package com.cinespoiler.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Food (
    val id : Int = 0,
    var name : String,
    var price: Int = 0,
    val description: String = "",
    val imageUrl: String = "",
    var isFavorite: Boolean = false,
    var quantity: Int = 0
)



