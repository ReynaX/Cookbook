package pl.mobilne.projekt.data

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("name")
    val name: String,
    @SerializedName("servings")
    val servings: String,
    @SerializedName("prepare_time")
    val prepareTime: String,
    @SerializedName("list_image_path")
    val listImagePath: String,
    @SerializedName("details_image_path")
    val detailsImagePath: String,
    @SerializedName("categories")
    val categories: List<String>?,
    @SerializedName("description")
    val description : String,
    @SerializedName("calories")
    val calories : String,
    @SerializedName("diet")
    val diet : List<String>?,
    @SerializedName("cook_time")
    val cookTime : String?,
    @SerializedName("bake_time")
    val bakeTime : String?,
    @SerializedName("ingredients")
    val ingredients : List<String>,
    @SerializedName("recipe")
    val recipe : String
) {
}