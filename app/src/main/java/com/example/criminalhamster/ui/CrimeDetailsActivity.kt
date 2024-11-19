package com.example.criminalhamster.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.criminalhamster.Constants
import com.example.criminalhamster.R
import com.example.criminalhamster.data.CrimeLab
import java.util.UUID

class CrimeDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_details)

        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
        if(NavUtils.getParentActivityName(this) != null)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val drawable = toolbar.navigationIcon
        drawable?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                if(NavUtils.getParentActivityName(this) != null)
                    NavUtils.navigateUpFromSameTask(this)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}