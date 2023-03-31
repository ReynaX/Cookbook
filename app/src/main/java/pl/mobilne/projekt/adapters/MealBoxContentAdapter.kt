package pl.mobilne.projekt.adapters

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.mobilne.projekt.R
import pl.mobilne.projekt.data.Meal
import pl.mobilne.projekt.listeners.OnItemClickListener


class MealBoxContentAdapter(private val items: List<Meal>, var listener: OnItemClickListener) :
    RecyclerView.Adapter<MealBoxContentAdapter.ViewHolder>() {

    class ViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        private val title: TextView
        private val categories: TextView
        private val servings: TextView
        private val prepareTime: TextView
        private val image: ImageView
        private val description: TextView
        private val context: Context

        init {
            this.title = view.findViewById(R.id.main_meal_tv_name)
            this.categories = view.findViewById(R.id.main_meal_tv_categories)
            this.servings = view.findViewById(R.id.main_meal_tv_servings)
            this.prepareTime = view.findViewById(R.id.main_meal_tv_prepare_time)
            this.image = view.findViewById(R.id.main_meal_iv_image)
            this.context = context;
            this.description = view.findViewById(R.id.main_meal_tv_description)
        }

        fun bind(meal: Meal, listener: OnItemClickListener) {
            val resources = context.resources;
            this.title.text = meal.name
            this.categories.text = meal.categories?.joinToString { str -> str }
            this.servings.text = String.format(
                resources.getString(R.string.servings_list_message),
                meal.servings
            )
            this.prepareTime.text = String.format(
                resources.getString(R.string.prepare_time_list_message),
                meal.prepareTime
            )
            this.description.text = meal.description
            Glide.with(context).load(meal.listImagePath).centerCrop().into(this.image)
            itemView.setOnClickListener { listener.onItemClick(meal); }
        }

        @Suppress("DEPRECATION")
        fun setImageSize() {
            val displayMetrics = DisplayMetrics()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.display?.getRealMetrics(displayMetrics)
            } else {
                val display =
                    (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?)!!.defaultDisplay
                display.getMetrics(displayMetrics)
            }
            val thirdScreenWidth = displayMetrics.widthPixels / 3

            this.image.layoutParams = ViewGroup.LayoutParams(thirdScreenWidth, thirdScreenWidth)
            this.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, thirdScreenWidth)

            // Assign listener to description text view so that when orientation changes, listener
            // will execute until the proper measurements(width, height) get calculated
            this.description.viewTreeObserver
                .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (description.lineCount > 1) {
                            description.viewTreeObserver.removeOnGlobalLayoutListener(this)

                            val remainingHeight = thirdScreenWidth - getOtherViewsHeight()
                            if (remainingHeight > 0) {
                                val lineHeight = description.lineHeight
                                val maxLines = remainingHeight / lineHeight
                                description.maxLines = maxLines - 1
                            }
                        }
                    }
                })
        }

        private fun getOtherViewsHeight(): Int {
            return servings.lineHeight * servings.lineCount +
                    categories.lineHeight * categories.lineCount +
                    title.lineHeight * title.lineCount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_main_meal, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, listener);
        holder.setImageSize()
    }
}