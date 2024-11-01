package com.example.criminalhamster

import com.example.criminalhamster.model.Crime
import java.util.Calendar
import java.util.Locale

object Utils {
    fun getStringDateOfCrime(crime: Crime): String{
        val year = crime.getDate().get(Calendar.YEAR).toString()
        val month = crime.getDate().get(Calendar.MONTH).toString()
        val day = crime.getDate().get(Calendar.DAY_OF_MONTH).toString()
        return "$year/$month/$day"
    }
     fun getStringTimeOfCrime(crime: Crime): String{
         var hour = crime.getDate().get(Calendar.HOUR_OF_DAY).toString()
         var minute = crime.getDate().get(Calendar.MINUTE).toString()
         if(minute.toInt() < 10) minute = String.format("0%s", minute)
         if(hour.toInt() < 10) hour = String.format("0%s", hour)
         return "$hour:$minute"
     }
    fun getStringFullDate(crime: Crime): String{
        return getStringDateOfCrime(crime) + " - " + getStringTimeOfCrime(crime)
    }
}