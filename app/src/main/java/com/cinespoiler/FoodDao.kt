package com.cinespoiler
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
@Dao
interface FoodDao {
    @Query("SELECT * FROM Food")
    suspend fun getAllFoods(): List<Food>

    @Query("SELECT * FROM Food WHERE id = :id")
    suspend fun getById(id: Int): Food?

    @Insert
    suspend fun insert(food: Food)

    @Update
    suspend fun update(food: Food)
    @Delete
    suspend fun delete(food: Food)
}