package com.example.criminalhamster.ui

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.criminalhamster.Constants
import com.example.criminalhamster.model.Crime
import com.example.criminalhamster.Utils
import com.example.criminalhamster.data.CrimeLab
import com.example.criminalhamster.databinding.FragmentCriminalBinding
import com.example.criminalhamster.model.Photo
import java.util.Calendar
import java.util.UUID


class CriminalFragment : Fragment() {
    lateinit var crime: Crime
    private lateinit var binding: FragmentCriminalBinding
    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onStart() {
        super.onStart()
        showPhoto()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeID = arguments?.getString(Constants.CRIMINAL_ID)
        crime = CrimeLab.getInstance(requireContext()).getCrime(UUID.fromString(crimeID)) ?: Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCriminalBinding.inflate(inflater)
        val view = binding.root
        updateScreenData()
        with(binding) {
            crimeTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    crime.setTitle(p0.toString())
                    Toast.makeText(this@CriminalFragment.context, p0.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            crimeIsSolved.setOnCheckedChangeListener { btnView, isChecked ->
                crime.setSolved(isChecked)
            }


            crimeDateBtn.isEnabled = true
            crimeTimeBtn.isEnabled = true

            crimeDateBtn.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    crime.getDate().get(Calendar.YEAR),
                    crime.getDate().get(Calendar.MONTH),
                    crime.getDate().get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            crimeTimeBtn.setOnClickListener {
                TimePickerDialog(
                    requireContext(),
                    timeSetListener,
                    crime.getDate().get(Calendar.HOUR_OF_DAY),
                    crime.getDate().get(Calendar.MINUTE),
                    true
                ).show()

            }
            crimeButton.setOnClickListener{
                val i = Intent(requireActivity(), CrimeCameraActivity::class.java)
                i.putExtra("FILE_URI", crime.getPhoto()?.fileName)
                launcher?.launch(i)
            }

            crimeImage.setOnClickListener{
                val FM = requireActivity().supportFragmentManager
                val imageFragment = ImageFragment.newInstance(crime.getPhoto()?.fileName.toString())

                imageFragment.show(FM, "ImageFragment")
            }

            val pm = requireActivity().packageManager
            if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
                crimeButton.isEnabled = false



            launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
                if(result.resultCode == RESULT_OK){
                    val filePath = result.data?.getStringExtra("FILE_PATH")

                    if(filePath != null)
                    {
                        crimeImage.setImageURI(filePath.toUri())
                        crime.setPhoto(Photo(filePath))
                        showPhoto()
                    }
                }
            }
        }
        return view
    }

    private fun updateScreenData() {
        binding.crimeIsSolved.isChecked = crime.isSolved()
        binding.crimeTitle.setText(crime.getTitle())
        binding.crimeDateBtn.text = Utils.getStringDateOfCrime(crime)
        binding.crimeTimeBtn.text = Utils.getStringTimeOfCrime(crime)
    }


    private val dateSetListener: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, month, day ->
            updateCrimeDate(year, month, day)
        }
    private val timeSetListener: OnTimeSetListener =
        OnTimeSetListener { view, hour, minute ->
            updateCrimeTime(hour, minute)
        }

    private fun updateCrimeDate(year: Int, month: Int, day: Int) {
        val newCrimeDate = crime.getDate()
        newCrimeDate.set(Calendar.YEAR, year)
        newCrimeDate.set(Calendar.MONTH, month)
        newCrimeDate.set(Calendar.DAY_OF_MONTH, day)

        crime.setDate(newCrimeDate)
        updateScreenData()
    }

    private fun updateCrimeTime(hour: Int, minute: Int) {
        val newCrimeTime = crime.getDate()
        newCrimeTime.set(Calendar.HOUR_OF_DAY, hour)
        newCrimeTime.set(Calendar.MINUTE, minute)

        crime.setDate(newCrimeTime)
        updateScreenData()
    }

    private fun showPhoto(){
        val photo = crime.getPhoto()
        if(photo != null){
            binding.crimeImage.setImageURI(photo.fileName.toUri())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(crimeID: String): CriminalFragment {
            val args = Bundle()
            args.putString(Constants.CRIMINAL_ID, crimeID)
            val fragment = CriminalFragment()
            fragment.arguments = args
            return fragment
        }
    }
}