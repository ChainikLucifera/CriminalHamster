package com.example.criminalhamster.data

import android.content.Context
import android.util.Log
import com.example.criminalhamster.model.Crime
import java.util.UUID

class CrimeLab(private val appContext: Context) {

    private val crimes = arrayListOf<Crime>()
    private val serializer = JSONSerializer(appContext, FILE_NAME)
    init {
        try{
            crimes.addAll(serializer.loadCrimes())
        }
        catch (e:Exception){
            Log.e("TEST", e.toString())
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

    fun addCrime(crime: Crime){
        crimes.add(crime)
    }

    fun deleteCrime(crime: Crime){
        crimes.remove(crime)
    }

    fun saveCrimes():Boolean{
        try {
            serializer.saveCrimes(crimes)
            Log.d("TEST", "Saved suck")
            return true
        }
        catch (e : Exception){
            Log.d("TEST", e.toString())
            return false
        }
    }

    companion object {
        private var crimeLab: CrimeLab? = null
        fun getInstance(appContext: Context): CrimeLab {
            if (crimeLab == null) {
                crimeLab = CrimeLab(appContext.applicationContext)
            }
            return crimeLab as CrimeLab
        }

        const val FILE_NAME = "crimes.json"
    }
}