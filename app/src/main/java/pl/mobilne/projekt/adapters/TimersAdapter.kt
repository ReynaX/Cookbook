package pl.mobilne.projekt.adapters

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.mobilne.projekt.R
import pl.mobilne.projekt.data.Timer

class TimersAdapter(private var items: List<Timer>, private val context: Context) : RecyclerView.Adapter<TimersAdapter.ViewHolder>(){

    class ViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        private val addButton : FloatingActionButton
        private val pauseButton : FloatingActionButton
        private val stopButton : FloatingActionButton
        private val context : Context

        init {
            addButton = view.findViewById(R.id.timer_layout_fab_play)
            pauseButton = view.findViewById(R.id.timer_layout_fab_pause)
            stopButton = view.findViewById(R.id.timer_layout_fab_stop)
            this.context = context
        }

        fun bind(timer: Timer){
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_timer_element, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addTimer(timer : Timer){
        items += timer
        notifyItemInserted(itemCount - 1)
    }

    fun addTimer(){
        items += Timer()
        notifyItemInserted(itemCount - 1)
    }
}