package com.cinespoiler

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :id")
     fun getById(id: Int): User

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
     fun login(email: String, password: String): User

    @Insert
     fun insert(user: User)

    @Update
     fun update(user: User)

    @Delete
     fun delete(user: User)
}
