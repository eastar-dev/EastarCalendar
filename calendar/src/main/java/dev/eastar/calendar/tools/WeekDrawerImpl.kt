package dev.eastar.calendar.tools

import android.graphics.*
import dev.eastar.calendar.TEXT_SIZE
import dev.eastar.calendar.WeekDrawer
import dev.eastar.calendar.dayOfWeekColors
import java.util.*

class WeekDrawerImpl : WeekDrawer {

    val dayOfWeekTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            textSize = TEXT_SIZE.toFloat()
            typeface = Typeface.MONOSPACE
            textAlign = Paint.Align.CENTER
        }
    }

    private val dayOfWeekTexts by lazy {
        val texts = mutableMapOf<Int, String>()
        val cal = Calendar.getInstance()
        for (i in Calendar.SUNDAY..Calendar.SATURDAY) {
            cal.set(Calendar.DAY_OF_WEEK, i)
            texts[i] = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
        }
        texts
    }

    private val bg: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.parseColor("#aaaaaa")
        }
    }

    override fun draw(canvas: Canvas, rect: Rect) {
        canvas.drawRect(rect, bg)
    }

    override fun draw(canvas: Canvas, rect: Rect, dayOfWeek: Int) {
        val text = dayOfWeekTexts[dayOfWeek]!!
        dayOfWeekTextPaint.color = dayOfWeekColors[dayOfWeek] ?: error("")
        canvas.drawText(text, (rect.width() / 2).toFloat(), dayOfWeekTextPaint.textSize + dayOfWeekTextPaint.textSize / 3, dayOfWeekTextPaint)
    }
}
