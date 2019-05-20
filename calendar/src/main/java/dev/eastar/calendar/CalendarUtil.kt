package dev.eastar.calendar

import android.content.res.Resources
import android.log.Log
import android.util.TypedValue
import dev.eastar.calendar.CalendarPagerFragment.Companion.START_DAY
import java.text.SimpleDateFormat
import java.util.*

//달력요일부분
class CalendarUtil {
    companion object {
        @JvmStatic
        fun getDisplayMoth(position: Int): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = START_DAY
            cal.add(Calendar.MONTH, position)
            cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE))
            //            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            val result = cal.timeInMillis
            result.log()
            return result
        }

        //월단위보기에서 선택일을가져온다
        @JvmStatic
        fun getSmartSelectedDay(dayStandard: Long, daySelected: Long): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = daySelected
            val day = cal.get(Calendar.DATE)

            cal.timeInMillis = dayStandard
            cal.set(Calendar.DATE, Math.min(day, cal.getActualMaximum(Calendar.DAY_OF_MONTH)))
            val result = cal.timeInMillis
            result.log()
            return result
        }

        @JvmStatic
        fun getFirstWeek(dayStandard: Long): Long {
            val cal = Calendar.getInstance()
            cal.timeInMillis = dayStandard
            cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
            val result = cal.timeInMillis
            result.log()
            return result
        }

        @JvmStatic
        fun distance(milliseconds_first: Long, milliseconds_end: Long, field: Int): Int {
            val sd = stripTime(milliseconds_first)
            val ed = stripTime(milliseconds_end)

            if (ed < sd)
                throw IllegalArgumentException("must milliseconds_first < milliseconds_end $sd,$ed")

            if (!(field == Calendar.YEAR || field == Calendar.MONTH || field == Calendar.DATE || field == Calendar.DAY_OF_WEEK))
                throw IllegalArgumentException("filed must in Calendar.YEAR, Calendar.MONTH or Calendar.DATE")

            val firstDayOfWeek = Calendar.getInstance().firstDayOfWeek
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            start.timeInMillis = sd
            end.timeInMillis = ed
            val sy = start.get(Calendar.YEAR)
            val ey = end.get(Calendar.YEAR)
            val sm = start.get(Calendar.MONTH)
            val em = end.get(Calendar.MONTH)

            start.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            val sw = start.timeInMillis
            end.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            val ew = end.timeInMillis

            when (field) {
                Calendar.YEAR -> return ey - sy
                Calendar.MONTH -> return (ey - sy) * 12 + (em - sm)
                Calendar.DAY_OF_WEEK -> return ((ew - sw) / (DAY1 * Calendar.DAY_OF_WEEK)).toInt()
                Calendar.DATE -> return ((ed - sd) / DAY1).toInt()
            }
            return -1
        }

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

fun Long.log() = Log.e(SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(Date(this)))

val Long.day: String get() = SimpleDateFormat("d", Locale.getDefault()).format(Date(this))
val Number.dp: Int get() = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), Resources.getSystem().displayMetrics))