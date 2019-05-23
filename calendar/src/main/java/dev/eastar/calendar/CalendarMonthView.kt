package dev.eastar.calendar

import CalendarObservable
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SoundEffectConstants
import android.view.View
import dev.eastar.calendar.tools.CalendarDrawerImpl
import java.util.*

//달력부분
class CalendarMonthView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    companion object {
        private var RECT = Rect()
    }

    private var calendarDrawer: CalendarDrawer = CalendarDrawerImpl()
    private var pressedDay: Long = -1
    private var selectedDay: Long = 0//달력에서 선택한날
    private var dayFirst: Long = 0//보여지는 시작일
    private var displayMonth: Long = 0//보여지고있는월

    fun setDisplayMonth(displayMonth: Long) {
//        displayMonth.log()
        this.displayMonth = displayMonth

        val cal = Calendar.getInstance()
        cal.timeInMillis = displayMonth
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        dayFirst = cal.timeInMillis
//        dayFirst.log()
    }

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
        dayHeight = Math.round(((h - WEEK_HEIGHT) / WEEK_COUNT).toDouble()).toInt()
    }

//    private var monthDrawer: MonthDrawer? = MonthDrawerImpl()
//    private var weekDrawer: WeekDrawer? = WeekDrawerImpl()
//    private var dayDrawer: DayDrawer? = DayDrawerImpl()
//    public fun setMonthDrawer(monthDrawer: MonthDrawer?) {
//        this.monthDrawer = monthDrawer
//    }
//
//    public fun setWeekDrawer(weekDrawer: WeekDrawer?) {
//        this.weekDrawer = weekDrawer
//    }
//
//    public fun setDayDrawer(dayDrawer: DayDrawer?) {
//        this.dayDrawer = dayDrawer
//    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        CalendarObservable.addObserver(observer)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        CalendarObservable.deleteObserver(observer)
    }

    private val observer = Observer { _: Observable, selectDay: Any ->
        if (selectDay is Long) {
            selectedDay = selectDay
//            selectedDay.log()
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
        calendarDrawer.drawMonthBg(canvas, RECT, dayFirst, row, col)

        RECT.set(0, 0, monthWidth, weekHeight)
        calendarDrawer.drawWeekBG(canvas, RECT)

        //week
        for (i in 0 until col) {
            val x = if (resources.configuration.layoutDirection == LAYOUT_DIRECTION_LTR) weekWidth * i else weekWidth * (Calendar.DAY_OF_WEEK - 1 - i)
            canvas.save()
            canvas.translate(x.toFloat(), 0f)

            val dayOfWeek = (firstDayOfWeek + i - Calendar.SUNDAY) % Calendar.DAY_OF_WEEK + Calendar.SUNDAY
            RECT.set(0, 0, weekWidth, weekHeight)
            calendarDrawer.drawWeekItem(canvas, RECT, dayOfWeek)
            canvas.restore()
        }

        canvas.translate(0f, weekHeight.toFloat())

        //day
        for (i in 0 until dayCount) {
            val day = dayFirst + i * DAY1
            val x = if (resources.configuration.layoutDirection == LAYOUT_DIRECTION_LTR) dayWidth * (i % col) else weekWidth * (Calendar.DAY_OF_WEEK - 1 - i % col)
            val y = Math.round((dayHeight * (i / col)).toDouble()).toInt()
            canvas.save()

            canvas.translate(x.toFloat(), y.toFloat())
            RECT.set(0, 0, dayWidth, dayHeight)
            calendarDrawer.drawDay(canvas, RECT, day, displayMonth, selectedDay, pressedDay)
            canvas.restore()
        }
    }

    fun stateChange(e: MotionEvent?) {
        var pressDay = if (e == null || e.x < 0 || e.y < 0 || monthWidth < e.x || monthHeight < e.y || e.y < weekHeight) {
            -1
        } else {
            val xAxle = if (resources.configuration.layoutDirection == LAYOUT_DIRECTION_LTR) (e.x / dayWidth).toInt() else Calendar.DAY_OF_WEEK - 1 - (e.x / dayWidth).toInt()
            val yAxle = ((e.y - weekHeight) / dayHeight).toInt()
            val index = xAxle + yAxle * Calendar.DAY_OF_WEEK
            (dayFirst + DAY1 * index.toLong())
            //.also { it.log() }
        }
        if (this.pressedDay != pressDay) {
            this.pressedDay = pressDay
            invalidate()
        }
    }

    private fun hitTest(e: MotionEvent): Boolean {
        if (e.x < 0 || e.y < 0 || monthWidth < e.x || monthHeight < e.y)
            return false

        playSoundEffect(SoundEffectConstants.CLICK)
        if (e.y < weekHeight) {
            CalendarObservable.notifySelectedWeek(firstDayOfWeek + (e.x / dayWidth).toInt())
            return true
        }

        val xAxle = if (resources.configuration.layoutDirection == LAYOUT_DIRECTION_LTR) (e.x / dayWidth).toInt() else Calendar.DAY_OF_WEEK - 1 - (e.x / dayWidth).toInt()
        val yAxle = ((e.y - weekHeight) / dayHeight).toInt()
        val index = xAxle + yAxle * Calendar.DAY_OF_WEEK
        CalendarObservable.notifySelectedDay(dayFirst + DAY1 * index.toLong())
        return true
    }

    //------------------------------------------------------------------------------------------
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val consume = detector.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_UP -> onGestureListener.onUp(event)
            MotionEvent.ACTION_CANCEL -> onGestureListener.onUp(event)
        }
        return consume
    }

    fun setCalendarDrawer(calendarDrawer: CalendarDrawer) {
        this.calendarDrawer = calendarDrawer

    }

    private val onGestureListener = object : GestureDetector.OnGestureListener {
        override fun onDown(e: MotionEvent?) = stateChange(e).let { true }
        fun onUp(e: MotionEvent) = stateChange(null)
        override fun onSingleTapUp(e: MotionEvent?) = hitTest(e!!)
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float) = stateChange(e2).let { false }

        override fun onShowPress(e: MotionEvent?) {}
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float) = false
        override fun onLongPress(e: MotionEvent?) {}
    }
    private val detector = GestureDetector(getContext(), onGestureListener).apply { setIsLongpressEnabled(false) }
}
