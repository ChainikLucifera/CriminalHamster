package com.example.criminalhamster.data

import android.content.Context
import android.util.Log
import com.example.criminalhamster.model.Crime
import org.json.JSONArray
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Writer

class JSONSerializer(private val context: Context, private val fileName: String) {
    fun saveCrimes(crimes: List<Crime>) {
        val array = JSONArray()
        for (crime in crimes) {
            array.put(crime.toJSON())
        }

        Log.d("TEST", array.toString())

        var writer: Writer? = null;

        try {
            val out = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            writer = OutputStreamWriter(out)
            writer.write(array.toString())
        } catch (e: Exception) {
            Log.e("TEST", e.message.toString())
        } finally {
            writer?.close()
        }
    }

    fun loadCrimes(): List<Crime> {
        val crimes = ArrayList<Crime>()
        var reader: BufferedReader? = null

        try {
            val inputStream = context.openFileInput(fileName)
            reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = StringBuilder()
            var line: String? = null

            while (reader.readLine().also { line = it } != null) {
                jsonString.append(line)
            }

            val array = JSONTokener(jsonString.toString()).nextValue() as JSONArray
            for (i in 0 until array.length()) {
                crimes.add(Crime(array.getJSONObject(i)))
            }
        } catch (e: FileNotFoundException) {
            Log.e("TEST", e.message.toString())
        } finally {
            reader?.close()
        }

        return crimes
    }
}