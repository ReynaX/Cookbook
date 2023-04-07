package pl.mobilne.projekt.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import pl.mobilne.projekt.R
import pl.mobilne.projekt.fragments.MealListFragment

class MainActivity : AppCompatActivity() {

    private val fragment = MealListFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.main_cl_layout, fragment).commit()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val fragment = supportFragmentManager.findFragmentById(R.id.main_cl_layout) as MealListFragment
        fragment.onOrientationChanged()
    }

    /**
     * Setup menu and listener to search view to filter data in list of meals based on
     * given query.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val menuItem = menu?.findItem(R.id.search_item)?.actionView as SearchView
        menuItem.queryHint = "Szukaj..."
        menuItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Do something when user submits the search query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null)
                    fragment.filter(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
}