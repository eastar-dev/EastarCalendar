package dev.eastar.calendar

import android.app.DatePickerDialog
import android.log.Log
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import java.util.*

//pager 달력
class CalendarPagerFragment : android.support.v4.app.Fragment() {
    //월이 변경 될때 날짜도 자동으로 같이 변경될지
    private var smartSelectedDay: Boolean = true
    private lateinit var pager: ViewPager
    private var selectedDay = System.currentTimeMillis()

    companion object {
        const val START_DAY = 0L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("hello calendar pager")
        return ViewPager(inflater.context).also { pager = it }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CalendarObservable.addObserver(observer)
    }

    override fun onDestroy() {
        super.onDestroy()
        CalendarObservable.deleteObserver(observer)
    }

    private var observer = Observer { _: Observable, obj: Any ->
        when (obj) {
            is Long -> {
                selectedDay = obj
                pager.currentItem = toPosition(obj)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadOnce()
    }

    private fun loadOnce() {
        pager.adapter = DAdapter(fragmentManager!!)
        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (smartSelectedDay) {
                    val displayMoth = CalendarUtil.getDisplayMonth(position)
                    selectedDay = CalendarUtil.getSmartSelectedDay(displayMoth, selectedDay)
                    CalendarObservable.notifySelectedDay(selectedDay)
                }
            }
        })
        pager.setCurrentItem(toPosition(selectedDay), false)
    }

    //-----------------------------------------------------------------------------------------
    private inner class DAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            Log.e(position)
            return CalendarFragment.newInstance(CalendarUtil.getDisplayMonth(position))
        }

        override fun getCount(): Int {
            return Integer.MAX_VALUE
        }

        //        @Override
        //        public int getItemPosition(Object object) {
        //            return POSITION_NONE;
        //        }
    }

    private fun toPosition(timeInMillis: Long): Int {
        return CalendarUtil.distance(START_DAY, timeInMillis, Calendar.MONTH)
    }
    //-----------------------------------------------------------------------------------------

    public fun prev() {
        pager.currentItem = pager.currentItem - 1
    }

    public fun next() {
        pager.currentItem = pager.currentItem + 1
    }

    public fun move(selectedDay: Long) {
        CalendarObservable.notifySelectedDay(selectedDay)
    }

    public fun move() {
        showDatePickerDialog(selectedDay) { _: DatePicker, year: Int, month: Int, day: Int ->
            val cal = Calendar.getInstance()
            cal.set(year, month, day)
            val selectedDay = cal.timeInMillis
            move(selectedDay)
        }
    }

    private fun showDatePickerDialog(current: Long = System.currentTimeMillis(), max: Long = 0L, min: Long = 0L, onDateSetListener: (DatePicker, Int, Int, Int) -> Unit) {
        if (max > 0 && min > 0 && min > max)
            throw IllegalArgumentException("min > max")
        if (max > 0 && min > 0 && min > current)
            Log.w("IllegalArgumentException(\"min > current\")")
        if (max > 0 && min > 0 && current > max)
            Log.w("IllegalArgumentException(\"current > max\")")

        val cal = Calendar.getInstance()
        cal.timeInMillis = current
        val dpd = DatePickerDialog(context, onDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE))
        if (max > 0)
            dpd.datePicker.maxDate = max
        if (min > 0)
            dpd.datePicker.minDate = min
        dpd.show()
    }
}
