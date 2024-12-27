package com.example.criminalhamster.model

import org.json.JSONObject

class Photo(val fileName: String) {

    constructor(json: JSONObject): this(json.getString(FILE_NAME))

    fun toJson() = JSONObject().put(FILE_NAME, fileName)

    companion object{
        const val FILE_NAME = "fileName"
    }
}