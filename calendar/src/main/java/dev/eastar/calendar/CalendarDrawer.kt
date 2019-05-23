package dev.eastar.calendar

import android.graphics.Canvas
import android.graphics.Rect

interface CalendarDrawer {
    fun drawMonthBg(canvas: Canvas, rect: Rect, firstDay: Long, col: Int, row: Int)

    fun drawWeekBG(canvas: Canvas, rect: Rect)
    fun drawWeekItem(canvas: Canvas, rect: Rect, dayOfWeek: Int)

    fun drawDay(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long)
    fun drawDayItem(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long)
    fun drawDayState(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long)
    fun drawDaySelected(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long)
    fun drawDayPressed(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long)
}
