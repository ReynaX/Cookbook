package pl.mobilne.projekt.listeners

import pl.mobilne.projekt.data.Meal

interface OnItemClickListener {
    fun onItemClick(meal : Meal)
}