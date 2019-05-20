package dev.eastar.calendar.tools

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import dev.eastar.calendar.CalendarConst
import dev.eastar.calendar.CalendarConst.Companion.dayOfWeekTexts
import dev.eastar.calendar.WeekDrawer
import dev.eastar.calendar.CalendarConst.Companion.dayOfWeekTextPaint as dayOfWeekTextPaint1

class WeekDrawerImpl : WeekDrawer {
    private val bg: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.parseColor("#aaaaaa")
        }
    }

    override fun draw(canvas: Canvas, rect: Rect) {
        canvas.drawRect(rect, bg)
    }

    override fun draw(canvas: Canvas, rect: Rect, dayOfWeek: Int) {
        val text = dayOfWeekTexts[dayOfWeek]
        dayOfWeekTextPaint1.color = CalendarConst.dayOfWeekColors[dayOfWeek]!!
        canvas.drawText(text, (rect.width() / 2).toFloat(), dayOfWeekTextPaint1.textSize + dayOfWeekTextPaint1.textSize / 3, dayOfWeekTextPaint1)
    }
}
