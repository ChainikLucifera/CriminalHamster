package com.example.criminalhamster.data

import android.content.Context
import android.util.Log
import com.example.criminalhamster.model.Crime
import java.util.UUID

class CrimeLab(private val appContext: Context) {

    private val crimes = arrayListOf<Crime>()

    init {

    }

    fun getCrimes() = crimes

    fun getCrime(id: UUID): Crime? {
        for (crime in crimes) {
            if (crime.getId() == id)
                return crime
        }
        return null
    }

    fun addCrime(crime: Crime){
        crimes.add(crime)
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