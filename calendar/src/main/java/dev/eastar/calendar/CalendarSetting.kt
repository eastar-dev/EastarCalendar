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

import android.graphics.Color
import java.util.*

val WEEK_HEIGHT: Int = 25.dp
val TEXT_SIZE = 14.dp

const val WEEK_COUNT = 6

const val DAY1 = 86400000L

val firstDayOfWeek = Calendar.getInstance().firstDayOfWeek
//시작요일은 붉은색, 시작요일에서 일주일에서 하루모자른 날은 파란색
val dayOfWeekColors by lazy {
    mapOf(Calendar.SUNDAY to Color.DKGRAY
            , Calendar.MONDAY to Color.DKGRAY
            , Calendar.TUESDAY to Color.DKGRAY
            , Calendar.WEDNESDAY to Color.DKGRAY
            , Calendar.THURSDAY to Color.DKGRAY
            , Calendar.FRIDAY to Color.DKGRAY
            , Calendar.SATURDAY to Color.DKGRAY
            , firstDayOfWeek to Color.RED
            , ((firstDayOfWeek + (Calendar.DAY_OF_WEEK - 1)) - Calendar.SUNDAY) % Calendar.DAY_OF_WEEK + Calendar.SUNDAY to Color.BLUE
    )
}

fun getDisplayShortName(dayOfWeek: Int) = Calendar.getInstance().run {
    set(Calendar.DAY_OF_WEEK, dayOfWeek)
    getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
}

val dayOfWeekTexts by lazy {
    mapOf(Calendar.SUNDAY to getDisplayShortName(Calendar.SUNDAY),
            Calendar.MONDAY to getDisplayShortName(Calendar.MONDAY),
            Calendar.TUESDAY to getDisplayShortName(Calendar.TUESDAY),
            Calendar.WEDNESDAY to getDisplayShortName(Calendar.WEDNESDAY),
            Calendar.THURSDAY to getDisplayShortName(Calendar.THURSDAY),
            Calendar.FRIDAY to getDisplayShortName(Calendar.FRIDAY),
            Calendar.SATURDAY to getDisplayShortName(Calendar.SATURDAY)
    )
}



