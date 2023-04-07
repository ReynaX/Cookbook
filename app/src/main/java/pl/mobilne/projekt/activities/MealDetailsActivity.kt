package pl.mobilne.projekt.activities

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import pl.mobilne.projekt.R
import pl.mobilne.projekt.adapters.TimersAdapter
import pl.mobilne.projekt.fragments.MealDetailsFragment
import pl.mobilne.projekt.fragments.TimersFragment
import kotlin.math.abs


class MealDetailsActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    enum class FragmentLoaded {
        DETAILS, TIMER
    }

    private val detailsFragment : MealDetailsFragment = MealDetailsFragment()
    private val timerFragment : TimersFragment = TimersFragment()
    private var fragmentLoaded : FragmentLoaded = FragmentLoaded.DETAILS
    private lateinit var menu: Menu

    private lateinit var timersAdapter: TimersAdapter
    private lateinit var gestureDetector: GestureDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_details)
        val display = supportActionBar
        display?.title = getString(R.string.app_name)
        display?.setDisplayHomeAsUpEnabled(true)

        detailsFragment.arguments = intent.extras

        gestureDetector = GestureDetector(applicationContext, this)
        timersAdapter = TimersAdapter(mutableListOf(), this.applicationContext)
        supportFragmentManager.beginTransaction().add(R.id.details_cl_main_layout, detailsFragment).commit()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        timersAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.timer_menu, menu)
        this.menu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.details_menu_change_fr) {
            if(fragmentLoaded == FragmentLoaded.DETAILS) {
                timerFragment.setAdapter(timersAdapter)
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

    override fun dispatchTouchEvent(e: MotionEvent?): Boolean {
        super.dispatchTouchEvent(e)
        return gestureDetector.onTouchEvent(e!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        return gestureDetector.onTouchEvent(event!!)
    }
    override fun onDown(p0: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent) {
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent) {
    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val swipeThreshold = 125
        val swipeVelocityThreshold = 400
        val diffX: Float = p1.x - p0.x
        if (diffX > swipeThreshold && abs(velocityX) > swipeVelocityThreshold)
            onOptionsItemSelected(menu.findItem(R.id.details_menu_change_fr))
        else if (-diffX > swipeThreshold && abs(velocityX) > swipeVelocityThreshold)
            finish()
        return true
    }
}