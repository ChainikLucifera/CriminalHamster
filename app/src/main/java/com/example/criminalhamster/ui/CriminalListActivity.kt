package com.example.criminalhamster.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.criminalhamster.Constants
import com.example.criminalhamster.R
import com.example.criminalhamster.data.CrimeLab
import com.example.criminalhamster.model.Crime

class CriminalListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criminal_list)

        val toolBar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, CrimeListFragment.newInstance())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crime_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.menuItemNewCrime -> {
                val crime = Crime()
                CrimeLab.getInstance(this).addCrime(crime)
                val intent = Intent(this, CrimeDetailsActivity::class.java)
                intent.putExtra(Constants.CRIMINAL_ID, crime.getId().toString())
                startActivity(intent)
            }

        }
        return true
    }
}