import android.log.Log
import dev.eastar.calendar.log
import java.util.*

object CalendarObservable : Observable() {
    fun notifySelectedDay(selectDay: Long) {
        selectDay.log()
        setChanged()
        super.notifyObservers(selectDay)
    }

    fun notifySelectedWeek(dayOfWeek: Int) {
        Log.e(dayOfWeek)
        setChanged()
        super.notifyObservers(dayOfWeek)
    }
}
