package pl.mobilne.projekt.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pl.mobilne.projekt.R
import pl.mobilne.projekt.adapters.MealBoxContentAdapter
import pl.mobilne.projekt.data.Meal
import pl.mobilne.projekt.listeners.MealDetailsListener
import java.io.IOException

class MealListFragment : Fragment() {

    private var recyclerView: RecyclerView? = null;
    private var adapter: MealBoxContentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view : View = inflater.inflate(R.layout.fragment_meal_list, container, false)
        initMealList(view)
        return view
    }

    private fun initMealList(view : View) {
        val jsonString: String
        try{
            jsonString = view.context.assets.open("data.json").bufferedReader().use { it.readText() }
        }catch(ioException: IOException){
            return
        }
        val itemsType = object : TypeToken<List<Meal>>() {}.type
        val items : List<Meal> = Gson().fromJson(jsonString, itemsType)

        recyclerView = view.findViewById(R.id.meal_fr_meals)
        adapter = MealBoxContentAdapter(items, MealDetailsListener(this.requireContext()))

        if(recyclerView != null) {
            recyclerView!!.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView!!.adapter = adapter
        }
    }

    /**
     * Notify adapter that data has changed so method <code>onBindViewHolder</code> from adapter gets called.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun onOrientationChanged(){
        val adapter : MealBoxContentAdapter =
            (view?.findViewById<RecyclerView>(R.id.meal_fr_meals)?.adapter ?: return) as MealBoxContentAdapter

        adapter.notifyDataSetChanged()
    }

    fun filter(filter: String){
        adapter?.filter(filter)
    }

}