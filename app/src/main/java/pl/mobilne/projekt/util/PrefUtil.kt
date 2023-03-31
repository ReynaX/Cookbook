//package pl.mobilne.projekt.util
//
//import android.content.Context
//import android.preference.PreferenceManager
//
//class PrefUtil {
//    companion object {
//        fun getTimerLength(context: Context) : Int{
//            return 1
//        }
//
//        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "pl.mobilne.timer.previous_timer_length"
//
//        fun getPreviousTimerLengthSeconds(context: Context) : Long{
//            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
//        }
//
//        fun setPreviousTImerLengthSeconds(seconds: Long, context: Context){
//            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
//            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
//            editor.apply()
//        }
//
//        private const val TIMER_STATE_ID = "pl.mobilne.projekt.timer_state"
//
//        fun getTimerState(context: Context) : Timer.TimerState{
//            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
//            return Timer.TimerState.values()[ordinal]
//        }
//
//        fun setTimerState(state: Timer.TimerState, context: Context){
//            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
//            val ordinal = state.ordinal
//            editor.putInt(TIMER_STATE_ID, ordinal)
//            editor.apply()
//        }
//
//        private const val SECONDS_REMAINING_ID = "pl.mobilne.timer.seconds_remaining"
//
//        fun getSecondsRemaining(context: Context) : Long{
//            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//            return preferences.getLong(SECONDS_REMAINING_ID, 0)
//        }
//
//        fun setSecondsRemaining(seconds: Long, context: Context){
//            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
//            editor.putLong(SECONDS_REMAINING_ID, seconds)
//            editor.apply()
//        }
//    }
//}