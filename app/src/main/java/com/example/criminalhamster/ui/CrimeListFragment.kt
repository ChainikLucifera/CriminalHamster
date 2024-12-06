package com.example.criminalhamster.ui

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalhamster.model.Crime
import com.example.criminalhamster.R
import com.example.criminalhamster.data.CrimeLab

//class CrimeListFragment : Fragment(), OnCrimeLongClickListener { - закоменченные это второй способ через interface в адаптере
class CrimeListFragment : Fragment() {
    private var crimes = arrayListOf<Crime>()
    private lateinit var adapter: CrimeAdapter
    private lateinit var recyclerView: RecyclerView
    private var selectedPosition: Int? = null

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
        adapter = CrimeAdapter(crimes) { position ->
            requireActivity().openContextMenu(recyclerView)
            selectedPosition = position

        }
        recyclerView.adapter = adapter

        registerForContextMenu(recyclerView)

        return view
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
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
        when(item.itemId){
            R.id.menuItemDeleteCrime ->{
                val selectedCrime = crimes[selectedPosition!!]
                CrimeLab.getInstance(requireContext()).deleteCrime(selectedCrime)
                adapter.notifyDataSetChanged()
            }
        }
        return super.onContextItemSelected(item)
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