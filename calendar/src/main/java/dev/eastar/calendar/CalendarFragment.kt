package dev.eastar.calendar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

//달력부분
class CalendarFragment : android.support.v4.app.Fragment() {
    //표시되는 월의 1일 0시
    private var displayMonth = 0L
    private val selectedDay = 0L

    companion object {
        const val DISPLAY_MONTH = "DISPLAY_MONTH"
        fun newInstance(displayMonth: Long): Fragment {
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
        return CalendarMonthView(inflater.context)
    }
}
