package dev.eastar.calendar.tools

import android.graphics.*
import dev.eastar.calendar.*
import java.util.*

open class DayDrawerImpl : DayDrawer {
    // 테두리
    private val selectedPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.parseColor("#00d89c")
            style = Paint.Style.STROKE
            strokeWidth = 1f.dp.toFloat()
        }
    }
    //날짜
    private val textPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.parseColor("#000000")
            textSize = 14f.dp.toFloat()
            typeface = Typeface.MONOSPACE
            textAlign = Paint.Align.CENTER
        }
    }
    //오늘
    private val todayPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.parseColor("#00d89c")
        }
    }

    override fun draw(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long) {
        drawDay(canvas, rc, day, displayMonth, selectedDay)
        drawState(canvas, rc, day, displayMonth, selectedDay)
        if (day == pressedDay)
            drawPressed(canvas, rc, day, displayMonth, selectedDay, pressedDay)
    }

    open fun drawDay(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long) {
        val isToday = CalendarUtil.equalDay(day, System.currentTimeMillis())
        val isSameMonth = CalendarUtil.equalMonth(day, displayMonth)
        val isSelectedDay = isSameMonth && CalendarUtil.equalDay(day, selectedDay)

        val cal = Calendar.getInstance()
        cal.timeInMillis = day
        val dow = cal.get(Calendar.DAY_OF_WEEK)

        textPaint.color = dayOfWeekColors[dow] ?: Color.DKGRAY
        todayPaint.color = Color.GREEN
        if (!isSameMonth) {
            textPaint.alpha = 0x66
            todayPaint.alpha = 0x66
        }

        if (isToday) {
            textPaint.color = Color.WHITE
            canvas.drawCircle(rc.centerX().toFloat(), 16f.dp.toFloat(), 10f.dp.toFloat(), todayPaint) // 오늘
        }
        canvas.drawText(day.day, rc.centerX().toFloat(), 20f.dp.toFloat(), textPaint)// 날짜
        if (isSelectedDay)
            canvas.drawRect(rc, selectedPaint)    // 테두리
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    val alarmPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    }
    val transferPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.parseColor("#00d89c")
        }
    }
    val alertPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.parseColor("#925fc0")
        }
    }
    val schedulePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.parseColor("#3498db")
        }
    }

    open fun drawState(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long) {

    }

    open fun drawPressed(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long) {

    }

}