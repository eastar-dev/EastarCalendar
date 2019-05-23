package dev.eastar.calendar.tools

import android.graphics.*
import dev.eastar.calendar.TEXT_SIZE
import dev.eastar.calendar.WeekDrawer
import dev.eastar.calendar.dayOfWeekColors
import dev.eastar.calendar.dayOfWeekTexts

open class WeekDrawerImpl : WeekDrawer {

    private val dayOfWeekTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            textSize = TEXT_SIZE.toFloat()
            typeface = Typeface.MONOSPACE
            textAlign = Paint.Align.CENTER
        }
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
        dayOfWeekTextPaint.color = dayOfWeekColors[dayOfWeek] ?: error("")
        val text = dayOfWeekTexts[dayOfWeek] ?: error("")
        canvas.drawText(text, (rect.width() / 2).toFloat(), dayOfWeekTextPaint.textSize + dayOfWeekTextPaint.textSize / 3, dayOfWeekTextPaint)
    }
}
