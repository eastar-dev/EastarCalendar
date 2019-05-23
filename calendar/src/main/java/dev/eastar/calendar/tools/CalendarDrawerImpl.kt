package dev.eastar.calendar.tools

import android.graphics.*
import dev.eastar.calendar.*
import java.util.*

open class CalendarDrawerImpl : CalendarDrawer {
    override fun drawMonthBg(canvas: Canvas, rect: Rect, firstDay: Long, col: Int, row: Int) {

    }

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

    override fun drawWeekBG(canvas: Canvas, rect: Rect) {
        canvas.drawRect(rect, bg)
    }

    override fun drawWeekItem(canvas: Canvas, rect: Rect, dayOfWeek: Int) {
        dayOfWeekTextPaint.color = dayOfWeekColors[dayOfWeek] ?: error("")
        val text = dayOfWeekTexts[dayOfWeek] ?: error("")
        canvas.drawText(text, (rect.width() / 2).toFloat(), dayOfWeekTextPaint.textSize + dayOfWeekTextPaint.textSize / 3, dayOfWeekTextPaint)
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

    override fun drawDay(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long) {
        drawDayItem(canvas, rc, day, displayMonth, selectedDay)
        drawDayState(canvas, rc, day, displayMonth, selectedDay)
        if (day == selectedDay)
            drawDaySelected(canvas, rc, day, displayMonth, selectedDay, pressedDay)
        if (day == pressedDay)
            drawDayPressed(canvas, rc, day, displayMonth, selectedDay, pressedDay)
    }

    override fun drawDayItem(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long) {
        val isToday = CalendarUtil.equalDay(day, System.currentTimeMillis())
        val isSameMonth = CalendarUtil.equalMonth(day, displayMonth)

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

    override fun drawDayState(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long) {
    }

    // 테두리
    private val selectedPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.parseColor("#00d89c")
            style = Paint.Style.STROKE
            strokeWidth = 1f.dp.toFloat()
        }
    }

    override fun drawDaySelected(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long) {
        canvas.drawRect(rc, selectedPaint)    // 테두리
    }

    // 누르고있는동안표시됨
    private val pressedPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.parseColor("#aad89c")
            style = Paint.Style.STROKE
            strokeWidth = 1f.dp.toFloat()
        }
    }

    override fun drawDayPressed(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long) {
        canvas.drawRect(rc, pressedPaint)    // 테두리
    }

}