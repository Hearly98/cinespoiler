package com.cinespoiler

interface OnClickListener {


    fun onClick(food: Food)
    fun onFavoriteFood(food: Food)
    fun onDeleteStore(food: Food)
}