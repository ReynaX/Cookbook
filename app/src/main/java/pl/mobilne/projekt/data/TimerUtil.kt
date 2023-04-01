package pl.mobilne.projekt.data

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class TimerUtil {
    companion object Instance {
        var vibrationsCount = 5
        var vibrationsLength = 1000L
        fun vibrate(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrationEffect = VibrationEffect.createWaveform(
                    LongArray(vibrationsCount) { vibrationsLength * it },
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
                vibratorManager.defaultVibrator.vibrate(vibrationEffect)
            } else {
                @Suppress("DEPRECATION")
                val vib = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                @Suppress("DEPRECATION")
                vib.vibrate(5000)
            }
        }


    }
}