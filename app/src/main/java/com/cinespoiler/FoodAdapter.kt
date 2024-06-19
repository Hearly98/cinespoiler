package com.cinespoiler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(private var foods: MutableList<Food>,
                  private var listener: OnClickListener) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_food, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods.get(position)

        with(holder) {
            setListener(food)
            binding.tvName.text = food.name
            binding.tvName.text = food.price
            binding.cbFavorite.isChecked = food.isFavorite
        }
    }

    override fun getItemCount(): Int = foods.size
    fun add(storeEntity: Food) {
        foods.add(storeEntity)
        notifyDataSetChanged()
    }

    fun setStores(stores: MutableList<Food>) {
        this.foods = stores
        notifyDataSetChanged()
    }

    fun update(food: Food) {
        val index = foods.indexOf(food)
        if (index != -1) {
            foods.set(index, food)
            notifyItemChanged(index)
        }
    }

    fun delete(food: Food) {
        val index = foods.indexOf(food)
        if (index != -1) {
            foods.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    // Clase interna inner class
    // Heredando de recycler
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Se debe sincronizar en graddle sino no aparece
        // Creamos la variable para que reciba de la vista
        val binding = ItemTiendaBinding.bind(view)

        fun setListener(food: Food) {
            with(binding.root) {
                setOnClickListener { listener.onClick(storeEntity) }
                setOnLongClickListener {
                    listener.onDeleteStore(storeEntity)
                    true
                }
            }

            binding.cbFavorite.setOnClickListener {
                listener.onFavoriteStore(storeEntity)
            }
        }

    }


}