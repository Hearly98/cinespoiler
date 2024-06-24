package com.cinespoiler.dao
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.cinespoiler.model.Food

@Dao
interface FoodDao {
    @Query("SELECT * FROM Food")
     fun getAllFoods(): MutableList<Food>

    @Update
    fun updateFood(food: Food)

}