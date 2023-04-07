package pl.mobilne.projekt.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pl.mobilne.projekt.R
import pl.mobilne.projekt.adapters.MealBoxContentAdapter
import pl.mobilne.projekt.data.Meal
import pl.mobilne.projekt.listeners.MealDetailsListener

class MealListFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var tabLayout: TabLayout? = null
    private var adapter: MealBoxContentAdapter? = null
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_meal_list, container, false)
        initMealList(view)
        tabLayout = view.findViewById(R.id.meal_tl_tab)
        tabLayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab) {
                // That's terrible!!!
                if (tab.text == "Wszystkie")
                    adapter?.filterByCuisine("")
                else if (tab.text == "Polskie")
                    adapter?.filterByCuisine("polish")
                else if (tab.text == "Zagraniczne")
                    adapter?.filterByCuisine("foreign")

            }

            override fun onTabUnselected(tab: Tab) {}
            override fun onTabReselected(tab: Tab) {}
        })
        return view
    }

    private fun initMealList(view: View) {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gson = Gson()
                val vals = mutableListOf<HashMap<String, *>>()
                for(i in 1 until dataSnapshot.childrenCount)
                    vals.add(dataSnapshot.child(i.toString()).value as HashMap<String, *>)

                val json = gson.toJsonTree(vals)

                val itemsType = object : TypeToken<List<Meal>>() {}.type
                val items: List<Meal> = gson.fromJson(json, itemsType)

                recyclerView = view.findViewById(R.id.meal_fr_meals)
                adapter = MealBoxContentAdapter(items, MealDetailsListener(requireContext()))

                if (recyclerView != null) {
                    recyclerView!!.layoutManager = GridLayoutManager(requireContext(), 1)
                    recyclerView!!.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })


    }

    /**
     * Notify adapter that data has changed so method <code>onBindViewHolder</code> from adapter gets called.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun onOrientationChanged() {
        adapter?.notifyDataSetChanged()
    }

    fun filter(filter: String) {
        adapter?.filter(filter)
    }

}