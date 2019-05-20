package dev.eastar.calendar

import android.log.Log
import java.util.*

object CalendarObservable : Observable() {
    fun notifySelectedDay(daySelected: Long) {
        daySelected.log()
        setChanged()
        super.notifyObservers(daySelected)
    }

    fun notifySelectedWeek(dayOfWeek: Int) {
        Log.e(dayOfWeek)
    }
}
