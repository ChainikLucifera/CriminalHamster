package com.example.criminalhamster

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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