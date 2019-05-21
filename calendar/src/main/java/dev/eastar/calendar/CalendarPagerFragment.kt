package dev.eastar.calendar

import CalendarObservable
import android.app.DatePickerDialog
import android.log.Log
import android.os.Bundle
import android.os.log
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
    companion object {
        const val pagerCount = 12 * 100 //100년
    }

    private var onPageChange: ((Long) -> Unit)? = null
    private var onSelectedDayChange: ((Long) -> Unit)? = null
    //월이 변경 될때 날짜도 자동으로 같이 변경될지
    private var smartSelectedDay: Boolean = true
    private lateinit var pager: ViewPager
    private var selectedDay = System.currentTimeMillis()
    private var centerMonth = System.currentTimeMillis()
    private val startDay
        get() = Calendar.getInstance().apply {
            timeInMillis = centerMonth
            set(Calendar.DATE, 1)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.MONTH, -pagerCount / 2)
        }.timeInMillis
                .also { it.log() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("hello calendar pager")
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
                onSelectedDayChange?.invoke(obj)
                pager.setCurrentItem(toPosition(selectedDay), false)
//                pager.currentItem = toPosition(obj)
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
                Log.e("toDisplayMonth")
                onPageChange?.invoke(toDisplayMonth(position))
                if (smartSelectedDay) {
                    val displayMoth = toDisplayMonth(position)
                    selectedDay = getSmartSelectedDay(displayMoth, selectedDay)
                    CalendarObservable.notifySelectedDay(selectedDay)
                }
            }
        })
        pager.setCurrentItem(toPosition(selectedDay), false)
    }

    //월이동시 적당한(?) 선택일을 가져온다
    fun getSmartSelectedDay(dayStandard: Long, daySelected: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = daySelected
        val day = cal.get(Calendar.DATE)

        cal.timeInMillis = dayStandard
        cal.set(Calendar.DATE, Math.min(day, cal.getActualMaximum(Calendar.DAY_OF_MONTH)))
        val result = cal.timeInMillis
        result.log()
        return result
    }

    //-----------------------------------------------------------------------------------------
    private inner class DAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return CalendarFragment.newInstance(toDisplayMonth(position))
        }

        override fun getCount() = pagerCount
    }

    private fun toPosition(timeInMillis: Long): Int {
        return distance(startDay, timeInMillis, Calendar.MONTH)
    }

    fun toDisplayMonth(position: Int): Long = Calendar.getInstance().run {
        timeInMillis = startDay
        add(Calendar.MONTH, position)
        set(Calendar.DATE, getActualMinimum(Calendar.DATE))
        return timeInMillis
    }

    //util
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

    fun distance(milliseconds_first: Long, milliseconds_end: Long, field: Int): Int {
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
    fun setOnChangeMonth(callback: (month: Long) -> Unit) {
        this.onPageChange = callback
    }

    @Suppress("unused")
    fun setOnChangeSelectedDay(callback: (selectedDay: Long) -> Unit) {
        this.onPageChange = callback
    }

}
