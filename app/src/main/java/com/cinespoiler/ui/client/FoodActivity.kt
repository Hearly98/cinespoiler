package com.cinespoiler.ui.client

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.cinespoiler.adapter.FoodAdapter
import com.cinespoiler.OnClickListener
import com.cinespoiler.databinding.ActivityFoodBinding
import com.cinespoiler.model.Food
import java.util.concurrent.LinkedBlockingQueue

abstract  class FoodActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding: ActivityFoodBinding

    private lateinit var mAdapter: FoodAdapter

    private lateinit var mGridLayout: GridLayoutManager

/*

    private fun getFoods() {
        val queue = LinkedBlockingQueue<MutableList<Food>>()
        Thread {
           val foods = FoodApplication.database.foodDao().getAllFoods()
            queue.add(foods)
        }.start()
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
*/

}