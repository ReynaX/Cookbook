package pl.mobilne.projekt.listeners

import android.content.Context
import android.content.Intent
import pl.mobilne.projekt.activities.MealDetailsActivity
import pl.mobilne.projekt.data.Meal

class MealDetailsListener(private val context : Context) : OnItemClickListener {
    override fun onItemClick(meal: Meal) {
        val intent = Intent(context, MealDetailsActivity::class.java)
        // Should have been Serializable(but it works :-)
        intent.putExtra("name", meal.name)
        intent.putExtra("servings", meal.servings)
        intent.putExtra("prepare_time", meal.prepareTime)
        intent.putExtra("list_image_path", meal.listImagePath)
        intent.putExtra("details_image_path", meal.detailsImagePath)
        intent.putExtra("categories", meal.categories?.let { ArrayList<String>(it) })
        intent.putExtra("description", meal.description)
        intent.putExtra("calories", meal.calories)
        intent.putExtra("diet", meal.diet?.let { ArrayList<String>(it) })
        intent.putExtra("cook_time", meal.cookTime)
        intent.putExtra("ingredients", ArrayList<String>(meal.ingredients))
        intent.putExtra("recipe", meal.recipe)

        context.startActivity(intent)
    }
}