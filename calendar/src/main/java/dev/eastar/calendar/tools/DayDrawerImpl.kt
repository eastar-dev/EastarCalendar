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

    override fun draw(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long) {
        drawDay(canvas, rc, day, displayMonth, selectedDay)
        drawState(canvas, rc, day, displayMonth, selectedDay)
    }

    override fun drawDay(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long) {
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

    override fun drawState(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long) {
//        val dayData = ViewModelProviders.of(mContext as FragmentActivity).get(CalDataViewModel::class.java).getDayData(day) ?: return
//
//        val h = dayData.HISTORY.size > 0
//        val i = dayData.INFO.size > 0
//        val s = dayData.SCHEDULE.size > 0
//        val a = dayData.SCHEDULE.size > 0
//        val xCenter = rc.centerX().toFloat()
//        if (h)
//            canvas.drawCircle(xCenter, VV.dp2px(31f).toFloat(), VV.dp2px(3f).toFloat(), trP)  // 이체
//        if (i)
//            canvas.drawCircle(xCenter, VV.dp2px(39f).toFloat(), VV.dp2px(3f).toFloat(), alertP)  // 소식
//        if (s)
//            canvas.drawCircle(xCenter, VV.dp2px(47f).toFloat(), VV.dp2px(3f).toFloat(), scheduleP)  // 일정
//        if (a)
//            canvas.drawBitmap(bitmap, xCenter + VV.dp2px(9f), VV.dp2px(10f).toFloat(), alarmPaint)// 알람
    }
}