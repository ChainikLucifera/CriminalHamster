package com.example.criminalhamster.model

import com.example.criminalhamster.data.JSONSerializer
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class Crime {
    private var id: UUID = UUID.randomUUID()
    private var title: String = ""
    private var date: Calendar = Calendar.getInstance()
    private var isSolved: Boolean = false
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private var photo: Photo? = null

    constructor(json: JSONObject):this(){
        id = UUID.fromString(json.getString(ID))
        isSolved = json.getBoolean(SOLVED)
        title = json.getString(TITLE)
        date = Calendar.getInstance().apply {
            time = dateFormat.parse(json.getString(DATE)) ?: Date()
        }
        if(json.has(PHOTO))
            photo = Photo(json.getJSONObject(PHOTO))
    }

    constructor()

    fun setTitle(title: String) {
        this.title = title
    }

    fun getTitle(): String {
        return title
    }

    fun getId(): UUID {
        return id
    }

    fun getDate() = date

    fun getPhoto() = photo

    fun setPhoto(photo: Photo){
        this.photo = photo
    }

    fun setDate(date: Calendar){
       this.date = date
    }

    fun isSolved() = isSolved

    fun setSolved(isSolved: Boolean){
       this.isSolved = isSolved
    }

    fun toJSON() : JSONObject{
        val json = JSONObject()
        json.put(ID, id)
        json.put(SOLVED, isSolved)
        json.put(TITLE, title)
        json.put(DATE, dateFormat.format(date.time))
        if(photo != null)
            json.put(PHOTO, photo!!.toJson())

        return json
    }

    companion object{
        const val ID = "ID"
        const val SOLVED = "SOLVED"
        const val TITLE = "TITLE"
        const val DATE = "DATE"
        const val PHOTO = "PHOTO"
    }
}