package com.cinespoiler
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
@Dao
interface FoodDao {
    @Query("SELECT * FROM Food")
     fun getAllFoods(): MutableList<Food>

    @Update
    fun updateFood(food: Food)

}