package dev.eastar.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.log.Log
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import dev.eastar.calendar.tools.DayDrawerImpl
import dev.eastar.calendar.tools.MonthDrawerImpl
import dev.eastar.calendar.tools.WeekDrawerImpl
import java.util.*

//달력부분
class CalendarMonthView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    companion object {
        private var RECT = Rect()
    }

    private var selectedDay: Long = 0//달력에서 선택한날
    private var dayFirst: Long = 0//보여지는 시작일
    private var displayMonth: Long = 0//보여지고있는월

    fun setDisplayMonth(displayMonth: Long) {
        this.displayMonth = displayMonth
        dayFirst = CalendarUtil.getFirstWeek(displayMonth)
    }

//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        Log.e(isRtl)
//    }

    private var isRtl: Int = resources.configuration.layoutDirection
    private var monthWidth: Int = 0
    private var monthHeight: Int = 0
    private var weekWidth: Int = 0
    private var weekHeight: Int = 0
    private var dayWidth: Int = 0
    private var dayHeight: Int = 0
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        monthWidth = w
        monthHeight = h
        weekWidth = Math.round((w / Calendar.DAY_OF_WEEK).toDouble()).toInt()
        weekHeight = WEEK_HEIGHT
        dayWidth = Math.round((w / Calendar.DAY_OF_WEEK).toDouble()).toInt()
        dayHeight = Math.round((h / WEEK_COUNT).toDouble()).toInt()
    }

    private var monthDrawer: MonthDrawer = MonthDrawerImpl()
    public fun setMonthDrawer(monthDrawer: MonthDrawer) {
        this.monthDrawer = monthDrawer
    }

    private var weekDrawer: WeekDrawer? = WeekDrawerImpl()
    public fun setWeekDrawer(weekDrawer: WeekDrawer?) {
        this.weekDrawer = weekDrawer
    }

    private var dayDrawer: DayDrawer = DayDrawerImpl()
    public fun setDayDrawer(dayDrawer: DayDrawer) {
        this.dayDrawer = dayDrawer
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        CalendarObservable.addObserver(observer)
        Log.e(isRtl)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        CalendarObservable.deleteObserver(observer)
    }

    private val observer = Observer { _: Observable, o: Any ->
        if (o is Long) {
            selectedDay = CalendarUtil.getSmartSelectedDay(displayMonth, o)
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val col = Calendar.DAY_OF_WEEK
        val row = WEEK_COUNT
        val dayCount = col * row

        //month
        RECT.set(0, 0, monthWidth, monthHeight)
        monthDrawer.draw(canvas, RECT, dayFirst, row, col)

        RECT.set(0, 0, monthWidth, weekHeight)
        weekDrawer?.draw(canvas, RECT)

        //week
        for (i in 0 until col) {
            val x = if (resources.configuration.layoutDirection == LAYOUT_DIRECTION_LTR)
                weekWidth * i
            else
                weekWidth * (Calendar.DAY_OF_WEEK - (i + 1))
            canvas.save()
            canvas.translate(x.toFloat(), 0f)

            val dayOfWeek = (firstDayOfWeek + i - Calendar.SUNDAY) % Calendar.DAY_OF_WEEK + Calendar.SUNDAY
            RECT.set(0, 0, weekWidth, weekHeight)
            weekDrawer?.draw(canvas, RECT, dayOfWeek)
            canvas.restore()
        }

        canvas.translate(0f, weekHeight.toFloat())

        //day
        for (i in 0 until dayCount) {
            val day = dayFirst + i * DAY1
            val x = if (resources.configuration.layoutDirection == LAYOUT_DIRECTION_LTR)
                dayWidth * (i % col)
            else
                weekWidth * (Calendar.DAY_OF_WEEK - (i % col + 1))
            val y = Math.round((dayHeight * (i / col)).toDouble()).toInt()
            canvas.save()

            canvas.translate(x.toFloat(), y.toFloat())
            RECT.set(0, 0, dayWidth, dayHeight)
            dayDrawer.draw(canvas, RECT, day, displayMonth, selectedDay)

            canvas.restore()
        }
    }

    private fun hitTest(e: MotionEvent) {
        if (e.y < weekHeight) {
            CalendarObservable.notifySelectedWeek(firstDayOfWeek + (e.x / dayWidth).toInt())
            return
        }
        val xAxle = (e.x / dayWidth).toInt()
        val yAxle = ((e.y - weekHeight) / dayHeight).toInt()
        val index = xAxle + yAxle * Calendar.DAY_OF_WEEK
        CalendarObservable.notifySelectedDay(dayFirst + DAY1 * index.toLong())
    }

    //------------------------------------------------------------------------------------------
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detector.onTouchEvent(event)
    }

    private val detector = GestureDetector(getContext(), object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            hitTest(e)
            return false
        }
    })
}
