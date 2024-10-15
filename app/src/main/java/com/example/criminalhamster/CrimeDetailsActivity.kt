package com.example.criminalhamster

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.util.UUID

class CrimeDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_details)

        val crimes = CrimeLab.getInstance(this).getCrimes()
        val crimePagerAdapter = CrimePagerAdapter(this, crimes)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter = crimePagerAdapter
        val crimeID = UUID.fromString(intent.getStringExtra(Constants.CRIMINAL_ID))
        if(crimeID != null){
            val crimePosition = crimes.indexOfFirst{
                it.getId() == crimeID
            }
            viewPager.setCurrentItem(crimePosition, false)
        }

    }
}