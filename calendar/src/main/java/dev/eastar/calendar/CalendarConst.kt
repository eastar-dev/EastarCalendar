package dev.eastar.calendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import java.util.*

//달력요일부분
class CalendarConst {
    @SuppressLint("ConstantLocale")
    companion object {
        val WEEK_HEIGHT: Int = 25.dp
        val TEXT_SIZE = 14.dp
        const val WEEK_COUNT = 6
        const val DAY1 = 86400000L

        const val SUNDAY_COLORS = Color.RED
        const val MONDAY_COLORS = Color.DKGRAY
        const val SATURDAY_COLORS = Color.BLUE

        val dayOfWeekTextPaint by lazy {
            Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
                textSize = TEXT_SIZE.toFloat()
                typeface = Typeface.MONOSPACE
                textAlign = Paint.Align.CENTER
            }
        }

        val firstDayOfWeek by lazy { Calendar.getInstance().firstDayOfWeek }

        val dayOfWeekTexts by lazy {
            val texts = mutableMapOf<Int, String>()
            val cal = Calendar.getInstance()
            for (i in Calendar.SUNDAY..Calendar.SATURDAY) {
                cal.set(Calendar.DAY_OF_WEEK, i)
                texts[i] = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
            }
            texts
        }

        val dayOfWeekColors by lazy {
            val colors = mutableMapOf<Int, Int>()
            colors[Calendar.SUNDAY] = SUNDAY_COLORS
            colors[Calendar.MONDAY] = MONDAY_COLORS
            colors[Calendar.TUESDAY] = MONDAY_COLORS
            colors[Calendar.WEDNESDAY] = MONDAY_COLORS
            colors[Calendar.THURSDAY] = MONDAY_COLORS
            colors[Calendar.FRIDAY] = MONDAY_COLORS
            colors[Calendar.SATURDAY] = SATURDAY_COLORS
            colors
        }

    }
}
