package com.example.criminalhamster.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalhamster.Constants
import com.example.criminalhamster.Utils
import com.example.criminalhamster.databinding.CrimeItemBinding
import com.example.criminalhamster.model.Crime

//class CrimeAdapter(val crimes : ArrayList<Crime>, private val listener: OnCrimeLongClickListener) : RecyclerView.Adapter<CrimeAdapter.CrimeViewHolder>() {
class CrimeAdapter(
    val crimes: ArrayList<Crime>,
    private val onItemClick: (String) -> Unit,
    private val onLongItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<CrimeAdapter.CrimeViewHolder>() {


    class CrimeViewHolder(val binding: CrimeItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CrimeItemBinding.inflate(inflater, parent, false)

        return CrimeViewHolder(binding)
    }

    override fun getItemCount(): Int = crimes.size

    override fun onBindViewHolder(holder: CrimeViewHolder, position: Int) {
        val crime = crimes[position]

        with(holder.binding) {
            if (crime.getTitle().length < 30)
                crimeTitle.text = crime.getTitle()
            else
                crimeTitle.text = crime.getTitle().substring(0, 29) + "..."
            crimeDate.text = Utils.getStringFullDate(crime)
            crimeCheckBox.isChecked = crime.isSolved()

            root.setOnClickListener {
                onItemClick(crime.getId().toString())
            }

            root.setOnLongClickListener {
                onLongItemClick(position)

                //listener.onCrimeLongClick()
                true
            }
        }

    }
}


//interface OnCrimeLongClickListener{
//    fun onCrimeLongClick()
//}