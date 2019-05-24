package dev.eastar.demo.eastarcalendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import dev.eastar.calendar.CalendarPagerFragment
import dev.eastar.calendar.day
import dev.eastar.calendar.getDisplayShortName
import dev.eastar.calendar.month
import dev.eastar.calendar.tools.CalendarDrawerImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }



        setEasrarCalendar()
    }

    private fun setEasrarCalendar() {
        val calendarPager: CalendarPagerFragment = Fragment.instantiate(this, CalendarPagerFragment::class.java.name, null) as CalendarPagerFragment
        setEastarCalendarEvent(calendarPager)
        setEastarCalendarDrawer(calendarPager)
        supportFragmentManager.beginTransaction().replace(R.id.container, calendarPager).commit()
    }

    private fun setEastarCalendarEvent(calendarPager: CalendarPagerFragment) {
        prev.setOnClickListener { calendarPager.prev() }
        next.setOnClickListener { calendarPager.next() }
        month.setOnClickListener { calendarPager.move() }

        calendarPager.setOnChangeMonthListener { month.text = it.month }
        calendarPager.setOnChangeSelectedDayListener { toast(it.day) }
        calendarPager.setOnWeekClickListener { toast(getDisplayShortName(it)) }
    }

    private fun setEastarCalendarDrawer(calendarPager: CalendarPagerFragment) {
        calendarPager.setCalendarDrawer(object : CalendarDrawerImpl() {
            //month bg
            override fun drawMonthBg(canvas: Canvas, rect: Rect, firstDay: Long, col: Int, row: Int) {
                super.drawMonthBg(canvas, rect, firstDay, col, row)
            }

            //week
            override fun drawWeekBG(canvas: Canvas, rect: Rect) {
                super.drawWeekBG(canvas, rect)
            }

            override fun drawWeekItem(canvas: Canvas, rect: Rect, dayOfWeek: Int) {
                super.drawWeekItem(canvas, rect, dayOfWeek)
            }

            override fun drawDay(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long) {
                super.drawDay(canvas, rc, day, displayMonth, selectedDay, pressedDay)
            }

            override fun drawDayItem(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long) {
                super.drawDayItem(canvas, rc, day, displayMonth, selectedDay)
            }

            override fun drawDayState(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long) {
                super.drawDayState(canvas, rc, day, displayMonth, selectedDay)
            }

            override fun drawDaySelected(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long) {
                super.drawDaySelected(canvas, rc, day, displayMonth, selectedDay, pressedDay)
            }

            override fun drawDayPressed(canvas: Canvas, rc: Rect, day: Long, displayMonth: Long, selectedDay: Long, pressedDay: Long) {
                super.drawDayPressed(canvas, rc, day, displayMonth, selectedDay, pressedDay)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

fun Context.toast(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()