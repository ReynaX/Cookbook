package pl.mobilne.projekt.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.mobilne.projekt.R

class MealDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_meal_details, container, false)
        val titleView : TextView = view.findViewById(R.id.details_fr_tv_title)

        val extras = requireArguments()
        titleView.text = extras.getString("name")

        initDescription(view, extras.getString("description"))
        initPrepareTime(view, extras.getString("prepare_time"))
        initServings(view, extras.getString("servings"))
        initCookTime(view, extras.getString("cook_time"))
        initDiet(view, extras.getStringArrayList("diet"))
        initIngredients(view, extras.getStringArrayList("ingredients"))
        initCalories(view, extras.getString("calories"))
        initBakeTime(view, extras.getString("bake_time"))
        initRecipe(view, extras.getString("recipe"))
        extras.getString("details_image_path")?.let { initImage(view, it) }

        val sendIngredientsButton = view.findViewById<FloatingActionButton>(R.id.details_fr_fab_send_ingredients)
        sendIngredientsButton.setOnClickListener {
            Toast.makeText(this.context, "Składniki wysłano do ...", Toast.LENGTH_LONG).show()
        }
        return view
    }

    private fun initDescription(view : View, description : String?) {
        val descriptionView : TextView = view.findViewById(R.id.details_fr_tv_description)
        if(description != null)
            descriptionView.text = description
        else
            descriptionView.visibility = View.INVISIBLE
    }

    private fun initServings(view : View, servings : String?){
        val servingsView : TextView = view.findViewById(R.id.details_fr_tv_servings)
        if(servings != null) {
            servingsView.text = HtmlCompat.fromHtml(String.format(getString(R.string.servings_details_message),
                servings), HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
        else
            servingsView.visibility = View.INVISIBLE
    }

    private fun initCookTime(view : View, cookTime : String?){
        val cookTimeView : TextView = view.findViewById(R.id.details_fr_tv_cook_time)
        if(cookTime != null) {
            cookTimeView.text = HtmlCompat.fromHtml(String.format(getString(R.string.cook_time_details_message),
                cookTime), HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    }

    private fun initBakeTime(view : View, bakeTime : String?){
        val bakeTimeView : TextView = view.findViewById(R.id.details_fr_tv_cook_time)
        if(bakeTime != null)
            bakeTimeView.text = HtmlCompat.fromHtml(String.format(getString(R.string.bake_time_details_message),
                bakeTime), HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    private fun initPrepareTime(view : View, prepareTime: String?){
        val prepareTimeView : TextView = view.findViewById(R.id.details_fr_tv_prepare_time)
        if(prepareTime != null)
            prepareTimeView.text = HtmlCompat.fromHtml(String.format(getString(R.string.prepare_time_details_message),
                prepareTime), HtmlCompat.FROM_HTML_MODE_COMPACT)
        else
            prepareTimeView.visibility = View.INVISIBLE
    }

    private fun initCalories(view : View, calories: String?){
        val caloriesView : TextView = view.findViewById(R.id.details_fr_tv_calories)
        if(calories != null)
            caloriesView.text = HtmlCompat.fromHtml(String.format(getString(R.string.calories_details_message),
                calories), HtmlCompat.FROM_HTML_MODE_COMPACT)
        else
            caloriesView.visibility = View.INVISIBLE
    }

    private fun initRecipe(view : View, recipe: String?){
        val recipeView : TextView = view.findViewById(R.id.details_fr_tv_recipe)
        if(recipe != null)
            recipeView.text = recipe
        else
            recipeView.visibility = View.INVISIBLE
    }

    private fun initImage(view : View, imagePath : String){
        val imageView : ImageView = view.findViewById(R.id.details_fr_iv_image)
        Glide.with(view.context).load(imagePath).into(imageView)
    }

    private fun initDiet(view : View, diet : ArrayList<String>?){
        val dietView : TextView = view.findViewById(R.id.details_fr_tv_diet)
        if(diet != null)
            dietView.text = HtmlCompat.fromHtml(String.format(getString(R.string.diet_details_message),
                diet.joinToString { str -> str }), HtmlCompat.FROM_HTML_MODE_COMPACT)
        else
            dietView.visibility = View.INVISIBLE
    }

    private fun initIngredients(view : View, ingredients : ArrayList<String>?){
        val ingredientsView : TextView = view.findViewById(R.id.details_fr_tv_ingriedents)
        if(ingredients != null){
            val joined = ingredients.joinToString(
                separator = "<br>",
                transform = { str ->
                    HtmlCompat.fromHtml(
                        String.format(
                            getString(R.string.ingredients_details_element),
                            str
                        ), HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
                }
            )

            ingredientsView.text = HtmlCompat.fromHtml("<b>Składniki:</b> <br>$joined</br>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
        else
            ingredientsView.visibility = View.INVISIBLE
    }
}