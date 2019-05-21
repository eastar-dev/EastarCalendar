package android.os

import android.log.Log
import java.text.SimpleDateFormat
import java.util.*

fun Long.log() = Log.e(SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(Date(this)))
