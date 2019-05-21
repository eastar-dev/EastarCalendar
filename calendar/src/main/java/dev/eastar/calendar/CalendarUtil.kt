package dev.eastar.calendar

import android.content.res.Resources
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.*

//달력요일부분
class CalendarUtil {
    companion object {


        @JvmStatic
        fun stripTime(milliseconds: Long): Long {
            //        return ((milliseconds + TimeZone.getDefault().getRawOffset()) / DAY1 * DAY1) - TimeZone.getDefault().getRawOffset();
            val c = Calendar.getInstance()
            c.timeInMillis = milliseconds
            c.set(Calendar.HOUR_OF_DAY, 0)
            c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)
            return c.timeInMillis
        }

        @JvmStatic
        fun stripDay(milliseconds: Long): Long {
            val c = Calendar.getInstance()
            c.timeInMillis = milliseconds
            c.set(Calendar.DATE, 1)
            c.set(Calendar.HOUR_OF_DAY, 0)
            c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)
            return c.timeInMillis
        }

        fun equalMonth(l: Long, r: Long): Boolean {
            return stripDay(l) == stripDay(r)
        }

        fun equalDay(l: Long, r: Long): Boolean {
            return stripTime(l) == stripTime(r)
        }

    }
}

val Long.month: String get() = SimpleDateFormat("yyyy.M", Locale.getDefault()).format(Date(this))
val Long.day: String get() = SimpleDateFormat("d", Locale.getDefault()).format(Date(this))
val Long.week: String get() = SimpleDateFormat("E", Locale.getDefault()).format(Date(this))
val Number.dp: Int get() = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), Resources.getSystem().displayMetrics))