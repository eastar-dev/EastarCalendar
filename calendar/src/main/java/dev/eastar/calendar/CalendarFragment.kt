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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.eastar.calendar.tools.CalendarDrawerImpl

//달력부분
class CalendarFragment : android.support.v4.app.Fragment() {

    //표시되는 월의 1일 0시
    private var displayMonth = 0L
    private var calendarDrawer: CalendarDrawer = CalendarDrawerImpl()

    companion object {
        const val DISPLAY_MONTH = "DISPLAY_MONTH"
        fun newInstance(displayMonth: Long): CalendarFragment {
            val f = CalendarFragment()
            val args = Bundle()
            args.putLong(DISPLAY_MONTH, displayMonth)
            f.arguments = args
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayMonth = arguments?.getLong(DISPLAY_MONTH) ?: 0L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CalendarMonthView(inflater.context).apply {
            setDisplayMonth(displayMonth)
            this.setCalendarDrawer(calendarDrawer)
        }
    }

    fun setCalendarDrawer(calendarDrawer: CalendarDrawer) {
        this.calendarDrawer = calendarDrawer
    }
}
