/*
 * Copyright 2019 copyright eastar Jeong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.eastar.calendar

import android.content.res.Resources
//import android.log.Log
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.*

//달력요일부분
class CalendarUtil {
    companion object {
        @JvmStatic
        fun stripTime(milliseconds: Long) = ((milliseconds + TimeZone.getDefault().rawOffset) / DAY1 * DAY1) - TimeZone.getDefault().rawOffset

        @JvmStatic
        fun stripDay(milliseconds: Long) = Calendar.getInstance().apply {
            timeInMillis = milliseconds
            set(Calendar.DATE, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        fun equalMonth(l: Long, r: Long) = stripDay(l) == stripDay(r)
        fun equalDay(l: Long, r: Long) = stripTime(l) == stripTime(r)
    }
}

//fun Long.log() = Log.e(SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(Date(this)))

val Long.month: String get() = SimpleDateFormat("yyyy.M", Locale.getDefault()).format(Date(this))
val Long.day: String get() = SimpleDateFormat("d", Locale.getDefault()).format(Date(this))
val Long.week: String get() = SimpleDateFormat("E", Locale.getDefault()).format(Date(this))
val Number.dp: Int get() = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), Resources.getSystem().displayMetrics))