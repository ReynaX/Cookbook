package pl.mobilne.projekt.adapters

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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

    /**
     * Listener responsible for removing timer from items when remove button
     * from ViewHolder has been clicked.
     */
    private var listener: OnItemClickListener = object : OnItemClickListener {
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
            // Set timer layout values before first tick
            setObservableValues(observableTimer)
            observableTimer.addObserver {
                setObservableValues(observableTimer)
            }

            playButton.setOnClickListener {
                if (!observableTimer.isTimerRunning()) {
                    val millis = convertToMillis()
                    if (millis != 0) {
                        if (observableTimer.isTimerStopped())
                            observableTimer.setMaxProgress(millis)
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
                    resetObservableValues()
                }
            }

            timerValueView.setOnClickListener {
                if (!observableTimer.isTimerStopped())
                    return@setOnClickListener
                timerValueListener()
            }

            timerNameView.setOnClickListener {
                timerNameListener(observableTimer)
            }

            removeButton.setOnClickListener {
                onItemClickListener.onItemClick(adapterPosition)
            }

            setProgressBarSize()
        }

        /**
         * Convert value of timerValueView text to milliseconds.
         */
        private fun convertToMillis(): Int {
            val s = timerValueView.text.split(":")
            if (s.size != 3)
                return 0
            return s[0].toInt() * 1000 * 3600 + s[1].toInt() * 1000 * 60 + s[2].toInt() * 1000
        }

        private fun setObservableValues(observableTimer: ObservableTimer) {
            timerValueView.text = observableTimer.getValue()
            timerProgressView.progress = observableTimer.getProgress()
            timerProgressView.max = observableTimer.getMaxProgress()
        }

        private fun resetObservableValues() {
            timerValueView.text = context.resources.getString(R.string.timer_default_value)
            timerProgressView.progress = 0
            timerProgressView.max = 100
        }

        /**
         * Listener to TextView of timer value that shows dialog with 3 number pickers.
         * Observable timer doesn't get updated on value change but only when
         * play button has been clicked.
         */
        private fun timerValueListener() {

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

        /**
         * Listener to TextView of timer name that show EditView.
         * Name of observable timer gets updated on dialog closed.
         */
        private fun timerNameListener(observableTimer: ObservableTimer){
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.layout_timer_name_picker, null)
            val nameView = view.findViewById<EditText>(R.id.timer_tv_name_picker)
            nameView.setText(timerNameView.text)

            val dialog = AlertDialog.Builder(context).setTitle("Wybierz nazwę").setView(view)
                .setPositiveButton("Wybierz nazwę") { _, _ ->
                    nameView.clearComposingText()
                    timerNameView.text = nameView.text
                    observableTimer.setTimerName(timerNameView.text.toString())
                }.setNegativeButton("Anuluj", null).create()
            dialog.show()
        }
        @Suppress("DEPRECATION")
        private fun setProgressBarSize(){
            val displayMetrics = DisplayMetrics()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.display?.getRealMetrics(displayMetrics)
            } else {
                val display =
                    (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?)!!.defaultDisplay
                display.getMetrics(displayMetrics)
            }

            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels
            val params = timerProgressView.layoutParams
            params.height = height - timerNameView.lineHeight - pauseButton.height
            if (displayMetrics.widthPixels > displayMetrics.heightPixels)
                params.width = width / 3
            else params.width = width
            timerProgressView.layoutParams = params
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

    fun addTimer() {
        items.add(ObservableTimer(this.context))
        notifyItemInserted(itemCount - 1)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}