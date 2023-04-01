package pl.mobilne.projekt.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.mobilne.projekt.R
import pl.mobilne.projekt.adapters.TimersAdapter

class TimersFragment : Fragment() {
    private lateinit var addButton: FloatingActionButton
    private lateinit var noTimersTextView: TextView
    private lateinit var timers: RecyclerView
    private lateinit var timersAdapter: TimersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer_manager, container, false)
        addButton = view.findViewById(R.id.timer_fr_fab_add)
        noTimersTextView = view.findViewById(R.id.timer_fr_tv_no_timers)
        timers = view.findViewById(R.id.timer_fr_recycler_view)

        timersAdapter = TimersAdapter(mutableListOf(), view.context)
        timers.adapter = timersAdapter
        timers.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        setupOnClickListeners()
        return view;
    }

    private fun setupOnClickListeners() {
        addButton.setOnClickListener {
            timersAdapter.addTimer()
        }
    }
}
