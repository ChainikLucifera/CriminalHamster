package com.example.criminalhamster.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalhamster.Constants
import com.example.criminalhamster.Utils
import com.example.criminalhamster.model.Crime
import com.example.criminalhamster.databinding.CrimeItemBinding

//class CrimeAdapter(val crimes : ArrayList<Crime>, private val listener: OnCrimeLongClickListener) : RecyclerView.Adapter<CrimeAdapter.CrimeViewHolder>() {
class CrimeAdapter(
    val crimes: ArrayList<Crime>,
    private val onItemClick: (Int) -> Unit
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
                val intent = Intent(root.context, CrimeDetailsActivity::class.java)
                intent.putExtra(Constants.CRIMINAL_ID, crime.getId().toString())
                root.context.startActivity(intent)
            }

            root.setOnLongClickListener {
                Log.d("TEST", "LONGCLICK")

                onItemClick(position)

                //listener.onCrimeLongClick()
                true
            }
        }

    }

}


//interface OnCrimeLongClickListener{
//    fun onCrimeLongClick()
//}