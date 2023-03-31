package pl.mobilne.projekt.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import pl.mobilne.projekt.R
import pl.mobilne.projekt.fragments.MealDetailsFragment
import pl.mobilne.projekt.fragments.MealListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = MealListFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_cl_layout, fragment).commit()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val fragment = supportFragmentManager.findFragmentById(R.id.main_cl_layout) as MealListFragment
        fragment.onOrientationChanged()
    }
}