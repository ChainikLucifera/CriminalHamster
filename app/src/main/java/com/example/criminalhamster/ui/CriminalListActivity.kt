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

    private var subtitleVisibility = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criminal_list)

        val toolBar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)

        if(savedInstanceState != null)
            subtitleVisibility = savedInstanceState.getBoolean(SUBTITLE_VISIBILITY, false)
        if(subtitleVisibility) supportActionBar?.subtitle = getString(R.string.subtitle)
        else supportActionBar?.subtitle = null

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, CrimeListFragment.newInstance())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crime_menu, menu)

        val showSubtitleBtn = menu?.findItem(R.id.menuItemShowSubtitle)
        if(subtitleVisibility) showSubtitleBtn?.title = getString(R.string.hide_sub)
        else showSubtitleBtn?.title = getString(R.string.show_sub)

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

            R.id.menuItemShowSubtitle -> {
                if(supportActionBar?.subtitle == null) {
                    supportActionBar?.subtitle = getString(R.string.subtitle)
                    item.title = getString(R.string.hide_sub)
                    subtitleVisibility = true
                }
                else
                {
                    supportActionBar?.subtitle = null
                    item.title = getString(R.string.show_sub)
                    subtitleVisibility = false
                }
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SUBTITLE_VISIBILITY, subtitleVisibility)
    }

    override fun onPause() {
        CrimeLab.getInstance(this).saveCrimes()
        super.onPause()
    }

    companion object{
        const val SUBTITLE_VISIBILITY = "SUBTITLE_VISIBILITY"
    }
}