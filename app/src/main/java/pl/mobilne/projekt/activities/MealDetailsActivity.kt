package pl.mobilne.projekt.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import pl.mobilne.projekt.R
import pl.mobilne.projekt.fragments.MealDetailsFragment
import pl.mobilne.projekt.fragments.TimersFragment


class MealDetailsActivity : AppCompatActivity() {
    enum class FragmentLoaded {
        DETAILS, TIMER
    }

    private val detailsFragment : MealDetailsFragment = MealDetailsFragment()
    private val timerFragment : TimersFragment = TimersFragment()
    private var fragmentLoaded : FragmentLoaded = FragmentLoaded.DETAILS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_details)
        val display = supportActionBar
        display?.title = getString(R.string.app_name)
        display?.setDisplayHomeAsUpEnabled(true)

        detailsFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction().add(R.id.details_cl_main_layout, detailsFragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.timer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.details_menu_change_fr) {
            if(fragmentLoaded == FragmentLoaded.DETAILS) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.details_cl_main_layout, timerFragment).addToBackStack(null)
                    .commit()
                fragmentLoaded = FragmentLoaded.TIMER
                item.setIcon(R.drawable.baseline_details)

            }else if(fragmentLoaded == FragmentLoaded.TIMER){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.details_cl_main_layout, detailsFragment).addToBackStack(null)
                    .commit()
                fragmentLoaded = FragmentLoaded.DETAILS
                item.setIcon(R.drawable.baseline_timer)
            }
        }else if(id == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}