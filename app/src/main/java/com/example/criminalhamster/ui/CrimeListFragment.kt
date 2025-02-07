package com.example.criminalhamster.ui

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalhamster.Constants
import com.example.criminalhamster.R
import com.example.criminalhamster.data.CrimeLab
import com.example.criminalhamster.model.Crime

//class CrimeListFragment : Fragment(), OnCrimeLongClickListener { - закоменченные это второй способ через interface в адаптере
class CrimeListFragment : Fragment() {
    private var crimes = arrayListOf<Crime>()
    private lateinit var adapter: CrimeAdapter
    private lateinit var recyclerView: RecyclerView
    private var selectedPosition: Int? = null
    public lateinit var detailsFragment: CriminalFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimes = CrimeLab.getInstance(requireContext()).getCrimes()

        recyclerView = view.findViewById<RecyclerView>(R.id.crimeRecyclerView)
        //adapter = CrimeAdapter(crimes, this)
        adapter = CrimeAdapter(
            crimes = crimes,
            onItemClick = { id ->
                if (requireActivity().findViewById<FrameLayout>(R.id.detailedFragmentContainer) != null) {
                    detailsFragment = CriminalFragment.newInstance(crimeID = id, onDataChanged = {
                        updateRV()
                    })
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.detailedFragmentContainer, detailsFragment).commit()
                } else {
                    val intent = Intent(requireActivity(), CrimeDetailsActivity::class.java)
                    intent.putExtra(Constants.CRIMINAL_ID, id)
                    requireActivity().startActivity(intent)
                }
            },
            onLongItemClick =
            { position ->
                requireActivity().openContextMenu(recyclerView)
                selectedPosition = position
            })
        recyclerView.adapter = adapter

        registerForContextMenu(recyclerView)

        return view
    }

    override fun onResume() {
        super.onResume()
        updateRV()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.crime_list_item_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemDeleteCrime -> {
                val selectedCrime = crimes[selectedPosition!!]
                CrimeLab.getInstance(requireContext()).deleteCrime(selectedCrime)
                if (::detailsFragment.isInitialized)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(detailsFragment).commit()
                updateRV()
            }
        }
        return super.onContextItemSelected(item)
    }

    fun updateRV() {
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CrimeListFragment()
    }

    //override fun onCrimeLongClick() {
    //    requireActivity().openContextMenu(recyclerView)
    //}
}