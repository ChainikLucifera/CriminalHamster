package com.example.criminalhamster

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import java.util.UUID


class CriminalFragment : Fragment() {
    var crime: Crime? = null
    lateinit var titleField: EditText
    lateinit var dateBtn: Button
    lateinit var isSolvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeID = arguments?.getString(Constants.CRIMINAL_ID)
        crime = CrimeLab.getInstance(requireContext()).getCrime(UUID.fromString(crimeID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_criminal, container, false)
        titleField = view.findViewById(R.id.crimeTitle)
        dateBtn = view.findViewById(R.id.crimeDateBtn)

        isSolvedCheckBox = view.findViewById(R.id.crimeIsSolved)
        isSolvedCheckBox.isChecked = crime?.isSolved() ?: false
        titleField.setText(crime?.getTitle())

        titleField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                crime?.setTitle(p0.toString())
                Toast.makeText(this@CriminalFragment.context, p0.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        isSolvedCheckBox.setOnCheckedChangeListener { btnView, isChecked ->
            crime?.setSolved(isChecked)
        }

        dateBtn.text = crime?.getDate().toString()
        dateBtn.isEnabled = false


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(crimeID : String) : CriminalFragment {
            val args = Bundle()
            args.putString(Constants.CRIMINAL_ID, crimeID)
            val fragment = CriminalFragment()
            fragment.arguments = args
            return  fragment
        }
    }
}