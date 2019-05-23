[![Release](https://jitpack.io/v/djrain/EastarCalendar.svg)](https://jitpack.io/#djrain/EastarCalendar)

## Eastar Calendar?

* 어느날 갑자기 만들고 싶어진 라이브러리
* canvas base로 직접 캘린더를 직접 그림(속도가 빠름)
* 좀더 빠르도록 개선의 여지(double buffering, invalidate rect)가 있지만 현재 상태에서 불편함이 없어서 그냥 사용
* Kotlin으로 작성되었습니다.
* support pack을 사용합니다.

## What different Eastar Calendar?

* 아랍권의 calendar 형태를 지원합니다.
* canvas에서 날짜 표시부를 표현 하도록 했기때문에 빠릅니다.
* 구현부에서 drawer를 재공 받아서 표현 하기때문에 거의 모든 형태의 표현이 가능합니다.

## Screenshot

![Screenshot](https://github.com/djrain/EastarCalendar/blob/art/release/%EC%95%84%EB%9E%8D.png?raw=true)
![Screenshot](https://github.com/djrain/EastarCalendar/blob/art/release/%EC%9D%BC%EB%B0%98.png?raw=true)

           

sample RESULTDLG
1. Request permission
2. Show message dialog for setting when denied permission



## What's new?

2019.5
처음 배포함 

## How...

### Gradle with jitpack

#### Add it in your root build.gradle at the end of repositories:
```javascript
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
#### Add the dependency
```javascript
dependencies {
        implementation 'com.github.djrain:EastarCalendar:1.0.1'
}
```

## How to use


### 1. Make PermissionListener
We will use PermissionListener for Permission Result.
You will get result to `onPermissionGranted()`, `onPermissionDenied()`

```javascript
private fun setEasrarCalendar() {
    val calendarPager: CalendarPagerFragment = 
        Fragment.instantiate(this, CalendarPagerFragment::class.java.name, null) as CalendarPagerFragment
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
```

<br/>

##Customize
You can customize something ...<br />

CalendarSetting.kt
```javascript

val WEEK_HEIGHT: Int = 25.dp
val TEXT_SIZE = 14.dp
```

<br/><br/>





## Thanks 

## License 
 ```code
Copyright 2019 eastar Jeong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
