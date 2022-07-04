package com.example.cloudmusic.utils

/**
 * @author:SunShibo
 * @date:2022-07-04 17:20
 * @feature:
 */
object TimeUtil {
    fun intervalToString(tick: Long): String {
        var tick = tick
        val timeString: String
        if (tick < 0) {
            tick = 0L
        }
        val seconds = tick / 1000
        val millsec = tick % 1000 / 10
        val mins = seconds / 60
        timeString = String.format(
            "%02d:%02d:%02d",
            mins, seconds % 60, millsec
        )
        return timeString
    }
}