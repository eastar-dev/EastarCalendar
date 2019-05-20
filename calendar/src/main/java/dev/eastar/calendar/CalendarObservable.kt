package dev.eastar.calendar

import java.util.*

object CalendarObservable : Observable() {
    fun notifySelectedDay(daySelected: Long) {
        daySelected.log()
        setChanged()
        super.notifyObservers(daySelected)
    }
}
