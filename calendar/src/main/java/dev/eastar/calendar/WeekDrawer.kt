package dev.eastar.calendar

import android.graphics.Canvas
import android.graphics.Rect

interface WeekDrawer {
    fun draw(canvas: Canvas, rect: Rect)
    fun draw(canvas: Canvas, rect: Rect, dayOfWeek: Int)
}
