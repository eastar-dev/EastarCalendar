/*
 * Copyright 2019 copyright eastar Jeong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
