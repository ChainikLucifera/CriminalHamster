package com.example.criminalhamster

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecyclerListener

class CrimeListFragment : Fragment() {
    private var crimes = arrayListOf<Crime>()
    private lateinit var adapter: CrimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimes = CrimeLab.getInstance(requireContext()).getCrimes()

        val recyclerView = view.findViewById<RecyclerView>(R.id.crimeRecyclerView)
        adapter = CrimeAdapter(crimes)
        recyclerView.adapter = adapter

        return view
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CrimeListFragment()
    }
}