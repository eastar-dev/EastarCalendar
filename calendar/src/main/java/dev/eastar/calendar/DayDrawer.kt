package dev.eastar.calendar

import android.graphics.Canvas
import android.graphics.Rect

interface DayDrawer {
    fun draw(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long)
}
