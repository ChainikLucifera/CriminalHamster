package com.example.criminalhamster

import android.content.Context
import android.util.Log
import java.util.UUID

class CrimeLab(private val appContext: Context) {

    private val crimes = arrayListOf<Crime>()

    init {
        for (i in 1..100) {
            val crime = Crime()
            crime.setTitle("Crime #$i")
            crime.setSolved(i % 2 == 0)
            crimes.add(crime)
            Log.d("TEST", crime.getId().toString())
        }
    }

    fun getCrimes() = crimes

    fun getCrime(id: UUID): Crime? {
        for (crime in crimes) {
            if (crime.getId() == id)
                return crime
        }
        return null
    }

    companion object {
        private var crimeLab: CrimeLab? = null
        fun getInstance(appContext: Context): CrimeLab {
            if (crimeLab == null) {
                crimeLab = CrimeLab(appContext.applicationContext)
            }
            return crimeLab as CrimeLab
        }
    }
}