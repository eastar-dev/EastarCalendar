package dev.eastar.calendar

import android.graphics.Canvas
import android.graphics.Rect

interface MonthDrawer {
    fun draw(canvas: Canvas, rect: Rect, dayFirst: Long, col: Int, row: Int)
}
