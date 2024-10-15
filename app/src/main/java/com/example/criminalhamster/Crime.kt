package com.example.criminalhamster

import java.util.Date
import java.util.UUID

class Crime {
    private val id: UUID = UUID.randomUUID()
    private var title: String = ""
    private var date: Date = Date()
    private var isSolved: Boolean = false

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

    fun setDate(date: Date){
       this.date = date
    }

    fun isSolved() = isSolved

    fun setSolved(isSolved: Boolean){
       this.isSolved = isSolved
    }
}