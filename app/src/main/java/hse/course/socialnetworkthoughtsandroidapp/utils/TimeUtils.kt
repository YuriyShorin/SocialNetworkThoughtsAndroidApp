package hse.course.socialnetworkthoughtsandroidapp.utils

import java.sql.Timestamp
import java.time.Month
import java.time.ZoneId

class TimeUtils {

    companion object {

       private enum class RussianMonth(val month: String) {
            JANUARY("Января"),
            FEBRUARY("Февраля"),
            MARCH("Марта"),
            APRIL("Апреля"),
            MAY("Мая"),
            JUNE("Июня"),
            JULY("Июля"),
            AUGUST("Августа"),
            SEPTEMBER("Сентября"),
            OCTOBER("Октября"),
            NOVEMBER("Ноября"),
            DECEMBER("Декабря")
        }

        fun formatTime(timeToFormat: Timestamp): String {
            val time = timeToFormat.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            val formatHour = if (time.hour < 10) "0${time.hour}" else time.hour.toString()
            val formatMinute = if (time.minute < 10) "0${time.minute}" else time.minute.toString()
            return when (time.month) {
                Month.JANUARY -> "${time.dayOfMonth} ${RussianMonth.JANUARY.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.FEBRUARY -> "${time.dayOfMonth} ${RussianMonth.FEBRUARY.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.MARCH -> "${time.dayOfMonth} ${RussianMonth.MARCH.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.APRIL -> "${time.dayOfMonth} ${RussianMonth.APRIL.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.MAY -> "${time.dayOfMonth} ${RussianMonth.MAY.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.JUNE -> "${time.dayOfMonth} ${RussianMonth.JUNE.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.JULY -> "${time.dayOfMonth} ${RussianMonth.JULY.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.AUGUST -> "${time.dayOfMonth} ${RussianMonth.AUGUST.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.SEPTEMBER -> "${time.dayOfMonth} ${RussianMonth.SEPTEMBER.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.OCTOBER -> "${time.dayOfMonth} ${RussianMonth.OCTOBER.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.NOVEMBER -> "${time.dayOfMonth} ${RussianMonth.NOVEMBER.month}, ${time.year} ${formatHour}:${formatMinute}"
                Month.DECEMBER -> "${time.dayOfMonth} ${RussianMonth.DECEMBER.month}, ${time.year} ${formatHour}:${formatMinute}"
                null -> ""
            }
        }
    }
}



