package com.cinespoiler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.cinespoiler.databinding.ActivityFoodBinding
import java.util.concurrent.LinkedBlockingQueue

class FoodActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding: ActivityFoodBinding

    private lateinit var mAdapter: FoodAdapter

    private lateinit var mGridLayout: GridLayoutManager



    private fun getFoods() {
        val queue = LinkedBlockingQueue<MutableList<Food>>()
        Thread {
            val foods = FoodApplication.database.foodDao().getAllFoods()
            queue.add(foods)
        }.start()

        // mAdapter.setStores(stores)
        mAdapter.setFoods(queue.take())
    }
    override fun onClick(food: Food) {

    }
    override fun onFavoriteFood(food: Food) {
        food.isFavorite = !food.isFavorite
        val queue = LinkedBlockingQueue<Food>()
        Thread {
            FoodApplication.database.foodDao().updateFood(food)
            queue.add(food)
        }.start()
        mAdapter.update(queue.take())
    }

    override fun onDeleteStore(food: Food) {
        TODO("Not yet implemented")
    }


}