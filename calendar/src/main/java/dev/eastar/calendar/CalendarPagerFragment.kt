package dev.eastar.calendar

import CalendarObservable
import android.app.DatePickerDialog
//import android.log.Log
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import dev.eastar.calendar.tools.CalendarDrawerImpl
import java.util.*

//pager 달력
class CalendarPagerFragment : android.support.v4.app.Fragment() {
    companion object {
        const val pagerCount = 12 * 100 //100년
    }

    private var calendarDrawer: CalendarDrawer = CalendarDrawerImpl()
    public fun setCalendarDrawer(calendarDrawer: CalendarDrawer) {
        this.calendarDrawer = calendarDrawer
    }

    private var onChangeMonth: ((Long) -> Unit)? = null
    private var onChangeSelectedDay: ((Long) -> Unit)? = null
    private var onWeekClickListener: ((Int) -> Unit)? = null
    //월이 변경 될때 날짜도 자동으로 같이 변경될지
    private var smartSelectedDay: Boolean = true
    private lateinit var pager: ViewPager
    private var selectedDay = System.currentTimeMillis()
    private var centerMonth = System.currentTimeMillis()
    private var startDay = getStartDay(centerMonth)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        Log.e("hello calendar pager")
        return ViewPager(inflater.context).apply {
            id = android.R.id.list
        }.also {
            pager = it
        }
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
                onChangeSelectedDay?.invoke(obj)
                pager.setCurrentItem(toPosition(selectedDay), false)
            }
            is Int -> {
                onWeekClickListener?.invoke(obj)
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
//                Log.e("toDisplayMonth")
                onChangeMonth?.invoke(toDisplayMonth(position))
                if (smartSelectedDay) {
                    val displayMoth = toDisplayMonth(position)
                    selectedDay = getSmartSelectedDay(displayMoth, selectedDay)
                    CalendarObservable.notifySelectedDay(selectedDay)
                }
            }
        })
        pager.setCurrentItem(toPosition(selectedDay), false)
    }

    //-----------------------------------------------------------------------------------------
    private inner class DAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return CalendarFragment.newInstance(toDisplayMonth(position)).apply {
                this.setCalendarDrawer(calendarDrawer)
            }
        }

        override fun getCount() = pagerCount
    }
    //-----------------------------------------------------------------------------------------

    private fun getStartDay(centerMonth: Long) = Calendar.getInstance().apply {
        timeInMillis = centerMonth
        set(Calendar.DATE, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        add(Calendar.MONTH, -pagerCount / 2)
    }.timeInMillis
//            .also { it.log() }

    private fun getSmartSelectedDay(displayMonth: Long, selectDay: Long) = Calendar.getInstance().apply {
        timeInMillis = selectDay
        val day = get(Calendar.DATE)

        timeInMillis = displayMonth
        set(Calendar.DATE, Math.min(day, getActualMaximum(Calendar.DAY_OF_MONTH)))
    }.timeInMillis
//            .also { it.log() }

    private fun toPosition(timeInMillis: Long): Int {
        return distance(startDay, timeInMillis, Calendar.MONTH)
    }

    fun toDisplayMonth(position: Int) = Calendar.getInstance().apply {
        timeInMillis = startDay
        add(Calendar.MONTH, position)
        set(Calendar.DATE, getActualMinimum(Calendar.DATE))
    }.timeInMillis

    //util
    private fun showDatePickerDialog(current: Long = System.currentTimeMillis(), maximim: Long? = null, minimum: Long? = null, onDateSetListener: (DatePicker, Int, Int, Int) -> Unit) {
        var cur: Long = current
        var min: Long? = minimum
        var max: Long? = maximim

        if (min != null && max != null && min > max) max = min.also { min = max }
        if (max != null && cur > max) cur = max
        if (min != null && cur < min!!) cur = min!!

        val cal = Calendar.getInstance().apply { timeInMillis = cur }
        val dpd = DatePickerDialog(context!!, onDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE))
        max?.let { dpd.datePicker.maxDate = it }
        min?.let { dpd.datePicker.minDate = it }

        dpd.show()
    }

    private fun distance(milliseconds_first: Long, milliseconds_end: Long, field: Int): Int {
        val sd = CalendarUtil.stripTime(milliseconds_first)
        val ed = CalendarUtil.stripTime(milliseconds_end)

        if (ed < sd)
            throw IllegalArgumentException("must milliseconds_first < milliseconds_end $sd,$ed")

        if (!(field == Calendar.YEAR || field == Calendar.MONTH || field == Calendar.DATE || field == Calendar.DAY_OF_WEEK))
            throw IllegalArgumentException("filed must in Calendar.YEAR, Calendar.MONTH or Calendar.DATE")

        val firstDayOfWeek = Calendar.getInstance().firstDayOfWeek
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        start.timeInMillis = sd
        end.timeInMillis = ed
        val sy = start.get(Calendar.YEAR)
        val ey = end.get(Calendar.YEAR)
        val sm = start.get(Calendar.MONTH)
        val em = end.get(Calendar.MONTH)

        start.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        val sw = start.timeInMillis
        end.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        val ew = end.timeInMillis

        when (field) {
            Calendar.YEAR -> return ey - sy
            Calendar.MONTH -> return (ey - sy) * 12 + (em - sm)
            Calendar.DAY_OF_WEEK -> return ((ew - sw) / (DAY1 * Calendar.DAY_OF_WEEK)).toInt()
            Calendar.DATE -> return ((ed - sd) / DAY1).toInt()
        }
        return -1
    }

    //interface
    @Suppress("unused")
    public fun prev() {
        pager.currentItem = pager.currentItem - 1
    }

    @Suppress("unused")
    public fun next() {
        pager.currentItem = pager.currentItem + 1
    }

    @Suppress("unused")
    public fun move(selectedDay: Long) {
        centerMonth = selectedDay
        startDay = centerMonth
        CalendarObservable.notifySelectedDay(selectedDay)
    }

    @Suppress("unused")
    public fun move() {
        showDatePickerDialog(selectedDay) { _: DatePicker, year: Int, month: Int, day: Int ->
            val cal = Calendar.getInstance()
            cal.set(year, month, day)
            val selectedDay = cal.timeInMillis
            move(selectedDay)
        }
    }

    @Suppress("unused")
    fun getCurrentMonth() = toDisplayMonth(pager.currentItem)

    @Suppress("unused")
    fun setOnChangeMonthListener(callback: (month: Long) -> Unit) {
        this.onChangeMonth = callback
    }

    @Suppress("unused")
    fun setOnChangeSelectedDayListener(callback: (selectedDay: Long) -> Unit) {
        this.onChangeSelectedDay = callback
    }

    @Suppress("unused")
    fun setOnWeekClickListener(callback: (dayOfWeek: Int) -> Unit) {
        this.onWeekClickListener = callback
    }
}
