package com.cinespoiler

import com.cinespoiler.model.Food

interface OnClickListener {


    fun onClick(food: Food)
    fun onFavoriteFood(food: Food)
    fun onDeleteStore(food: Food)
}