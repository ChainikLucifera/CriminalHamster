package com.example.criminalhamster

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalhamster.databinding.CrimeItemBinding

class CrimeAdapter(val crimes : ArrayList<Crime>) : RecyclerView.Adapter<CrimeAdapter.CrimeViewHolder>() {
    class CrimeViewHolder(val binding : CrimeItemBinding) : RecyclerView.ViewHolder(binding.root)
    {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CrimeItemBinding.inflate(inflater, parent, false)

        return CrimeViewHolder(binding)
    }

    override fun getItemCount(): Int = crimes.size

    override fun onBindViewHolder(holder: CrimeViewHolder, position: Int) {
        val crime = crimes[position]

        with(holder.binding){
            crimeTitle.text = crime.getTitle()
            crimeData.text = crime.getDate().toString()
            crimeCheckBox.isChecked = crime.isSolved()

            root.setOnClickListener{
                val intent = Intent(root.context, CrimeDetailsActivity::class.java)
                intent.putExtra(Constants.CRIMINAL_ID, crime.getId().toString())
                root.context.startActivity(intent)
            }
        }

    }

}