package pl.mobilne.projekt.data

import android.content.Context
import android.os.*
import android.util.Log
import java.text.DecimalFormat


class ObservableTimer {
    constructor(context: Context, value: String, progress: Int) {
        this.context = context
        this.value = value
        this.progress = progress
    }

    constructor(context: Context) {
        this.context = context
        this.value = "00:00:00"
        this.progress = 0
    }

    private val observers = mutableListOf<(String) -> Unit>()

    enum class TimerState {
        RUNNING, STOPPED, PAUSED
    }

    private var context: Context
    private var value: String;
    private var progress: Int
    private var state = TimerState.STOPPED
    private var start = 0L
    private var name = "Minutnik"
    private var secondsPassed = 0L
    private lateinit var countDownTimer: CountDownTimer

    fun startTimer(start: Long) {
        state = TimerState.RUNNING
        this.start = start
        this.secondsPassed = 0L

        countDownTimer = object : CountDownTimer(start, 1000) {
            var secondsFull = start / 1000

            override fun onTick(millisUntilFinished: Long) {

                val seconds: Int = ((secondsFull - secondsPassed) % 60).toInt()
                val minutes: Int = (((secondsFull - secondsPassed) / 60) % 60).toInt()
                val hours: Int = (((secondsFull - secondsPassed) / (60 * 60)) % 24).toInt()
                val format = DecimalFormat("00")

                progress = (start - (secondsPassed * 1000)).toInt()

                value =
                    ("${format.format(hours)}:${format.format(minutes)}:${format.format(seconds)}")
                secondsPassed++
                notifyObservers()
            }

            override fun onFinish() {
                state = TimerState.STOPPED
                value = "00:00:00"
                secondsPassed = 0L
                progress = 0
                notifyObservers()
                TimerUtil.vibrate(context)
            }
        }
        countDownTimer.start()
    }

    fun playTimer() {
        state = TimerState.RUNNING
        countDownTimer.start()
    }

    fun pauseTimer() {
        state = TimerState.PAUSED
        countDownTimer.cancel()
    }

    fun stopTimer() {
        state = TimerState.STOPPED
        value = "00:00:00"
        countDownTimer.cancel()
    }

    fun isTimerRunning(): Boolean {
        return state == TimerState.RUNNING
    }

    fun isTimerStopped(): Boolean {
        return state == TimerState.STOPPED
    }

    fun getValue(): String = value

    fun setValue(value: String) {
        this.value = value
        notifyObservers()
    }

    fun getProgress(): Int = progress

    fun setProgress(progress: Int) {
        this.progress = progress;
        notifyObservers()
    }

    fun set(value: String, progress: Int) {
        this.value = value
        this.progress = progress
    }

    fun setTimerName(timerName: String) {
        this.name = timerName
    }

    fun addObserver(observer: (String) -> Unit) {
        observers.add(observer)
        Log.d("Add", "Add invoked")
    }

    fun removeObserver(observer: (String) -> Unit) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        observers.forEach { it.invoke(value) }
        Log.d("Notify", "Notify invoked")
    }
}