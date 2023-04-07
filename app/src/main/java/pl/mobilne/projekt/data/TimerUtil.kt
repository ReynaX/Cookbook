package pl.mobilne.projekt.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import pl.mobilne.projekt.R

class TimerUtil {
    companion object Instance {
        private var vibrationsCount = 5
        private var vibrationsLength = 1000L
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

        fun sendNotification(context: Context, title: String, text: String){
            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel("TIMER NOTIFICATION",
                "Timer Notifications",
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
            mNotificationManager.createNotificationChannel(channel)
            val mBuilder = NotificationCompat.Builder(context, "TIMER NOTIFICATION")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
            mNotificationManager.notify(0, mBuilder.build())
        }
    }
}