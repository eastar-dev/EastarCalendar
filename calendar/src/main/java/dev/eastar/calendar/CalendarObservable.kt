
import android.log.Log
import android.os.log
import java.util.*

object CalendarObservable : Observable() {
    fun notifySelectedDay(selectDay: Long) {
        selectDay.log()
        setChanged()
        super.notifyObservers(selectDay)
    }

    fun notifySelectedWeek(dayOfWeek: Int) {
        Log.e(dayOfWeek)
    }
}
