package com.example.criminalhamster.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.criminalhamster.R

class CriminalListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criminal_list)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, CrimeListFragment.newInstance())
            .commit()
    }
}