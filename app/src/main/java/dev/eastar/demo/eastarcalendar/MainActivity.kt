package dev.eastar.demo.eastarcalendar

import android.log.Log
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import dev.eastar.calendar.CalendarPagerFragment
import dev.eastar.calendar.month
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
        val calendarPager: CalendarPagerFragment = Fragment.instantiate(this, CalendarPagerFragment::class.java.name, null) as CalendarPagerFragment
        supportFragmentManager.beginTransaction().replace(R.id.container, calendarPager).commit()
        prev.setOnClickListener { calendarPager.prev() }
        next.setOnClickListener { calendarPager.next() }
        month.setOnClickListener { calendarPager.move() }

        calendarPager.setOnChangeMonth {
            Log.e(it.month)
            month.text = it.month
        }
        calendarPager.setOnChangeSelectedDay {
            Log.e(it.month)
            month.text = it.month
        }
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
