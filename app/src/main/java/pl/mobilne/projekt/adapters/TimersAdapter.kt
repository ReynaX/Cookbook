package pl.mobilne.projekt.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.*
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.mobilne.projekt.R
import pl.mobilne.projekt.data.ObservableTimer


class TimersAdapter(private var items: MutableList<ObservableTimer>, private val context: Context) :
    RecyclerView.Adapter<TimersAdapter.ViewHolder>() {

    var listener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            if (position >= 0 && position < items.size) {
                items.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    class ViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        private val playButton: FloatingActionButton
        private val pauseButton: FloatingActionButton
        private val stopButton: FloatingActionButton
        private val removeButton: FloatingActionButton
        private val timerNameView: TextView
        private val timerValueView: TextView
        private val timerProgressView: ProgressBar
        private val context: Context

        init {
            playButton = view.findViewById(R.id.timer_layout_fab_play)
            pauseButton = view.findViewById(R.id.timer_layout_fab_pause)
            stopButton = view.findViewById(R.id.timer_layout_fab_stop)
            removeButton = view.findViewById(R.id.timer_layout_fab_remove)
            timerNameView = view.findViewById(R.id.timer_layout_tv_title)
            timerValueView = view.findViewById(R.id.timer_layout_tv_value)
            timerProgressView = view.findViewById(R.id.timer_layout_pb_value)

            this.context = context
        }

        fun bind(observableTimer: ObservableTimer, onItemClickListener: OnItemClickListener) {
            observableTimer.addObserver {
                timerValueView.text = observableTimer.getValue()
                timerProgressView.progress = observableTimer.getProgress()
            }

            playButton.setOnClickListener {
                if (!observableTimer.isTimerRunning()) {
                    val millis = convertToMillis()
                    if (millis != 0) {
                        if(observableTimer.isTimerStopped())
                            timerProgressView.max = millis
                        observableTimer.startTimer(millis.toLong())
                    }
                } else observableTimer.playTimer()
            }

            pauseButton.setOnClickListener {
                if (observableTimer.isTimerRunning()) {
                    observableTimer.pauseTimer()
                }
            }

            stopButton.setOnClickListener {
                if (observableTimer.isTimerRunning()) {
                    observableTimer.stopTimer()
                }
            }

            // Show dialog with 3 number picker(hour, minutes, seconds) to change
            // the countdown timer's starting value
            timerValueView.setOnClickListener {
                if (!observableTimer.isTimerStopped())
                    return@setOnClickListener
                val inflater = LayoutInflater.from(context)
                val dialogView = inflater.inflate(R.layout.dialog_timer_time_picker, null)
                val hoursPicker = dialogView.findViewById<NumberPicker>(R.id.timer_np_hours_picker)
                val minutesPicker =
                    dialogView.findViewById<NumberPicker>(R.id.timer_np_minutes_picker)
                val secondsPicker =
                    dialogView.findViewById<NumberPicker>(R.id.timer_np_seconds_picker)

                hoursPicker.minValue = 0
                hoursPicker.maxValue = 100

                minutesPicker.minValue = 0
                minutesPicker.maxValue = 59

                secondsPicker.minValue = 0
                secondsPicker.maxValue = 59

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Wybierz czas")
                    .setView(dialogView)
                    .setPositiveButton("Wybierz czas") { _, _ ->
                        val hoursPicked = hoursPicker.value
                        val minutesPicked = minutesPicker.value
                        val secondsPicked = secondsPicker.value
                        val selectedTime = String.format(
                            "%02d:%02d:%02d",
                            hoursPicked,
                            minutesPicked,
                            secondsPicked
                        )
                        timerValueView.text = selectedTime
                    }
                    .setNegativeButton("Anuluj", null)
                    .create()

                dialog.show()
            }

            // Show dialog with edittext view to change name of timer
            timerNameView.setOnClickListener {
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.layout_timer_name_picker, null)
                val nameView = view.findViewById<EditText>(R.id.timer_tv_name_picker)

                nameView.setText(timerNameView.text)

                val dialog = AlertDialog.Builder(context).setTitle("Wybierz nazwę").setView(view)
                    .setPositiveButton("Wybierz nazwę") { _, _ ->
                        timerNameView.text = nameView.text
                    }.setNegativeButton("Anuluj", null).create()
                dialog.show()
            }

            removeButton.setOnClickListener {
                onItemClickListener.onItemClick(adapterPosition)
            }
        }

        private fun convertToMillis(): Int {
            val s = timerValueView.text.split(":")
            if (s.size != 3)
                return 0
            return s[0].toInt() * 1000 * 3600 + s[1].toInt() * 1000 * 60 + s[2].toInt() * 1000
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_timer_element, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    fun addTimer(observableTimer: ObservableTimer) {
        items.add(observableTimer)
        notifyItemInserted(itemCount - 1)
    }

    fun addTimer() {
        items.add(ObservableTimer(this.context))
        notifyItemInserted(itemCount - 1)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}