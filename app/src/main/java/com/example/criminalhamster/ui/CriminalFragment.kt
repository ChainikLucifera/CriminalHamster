package com.example.criminalhamster.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.criminalhamster.Constants
import com.example.criminalhamster.R
import com.example.criminalhamster.Utils
import com.example.criminalhamster.data.CrimeLab
import com.example.criminalhamster.databinding.FragmentCriminalBinding
import com.example.criminalhamster.model.Crime
import com.example.criminalhamster.model.Photo
import java.util.Calendar
import java.util.UUID


class CriminalFragment : Fragment() {
    lateinit var crime: Crime
    private lateinit var binding: FragmentCriminalBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var pickContactLauncher: ActivityResultLauncher<Void?> =
        registerForActivityResult(ActivityResultContracts.PickContact()) { contactData ->
            contactData?.let { uri ->
                Log.d("TEST", contactData.toString())

                val projection = arrayOf(
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
                )

                val phoneCursor: Cursor? = requireContext().contentResolver
                    .query(uri, projection, null, null)

                if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    val contactNameIndex = phoneCursor
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val contactIdIndex = phoneCursor
                        .getColumnIndex(ContactsContract.Contacts._ID)
                    val contactHasPhoneNumberIndex = phoneCursor
                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                    if (
                        contactNameIndex >= 0 &&
                        contactIdIndex >= 0 &&
                        contactHasPhoneNumberIndex >= 0
                    ) {

                        val contactName = phoneCursor.getString(contactNameIndex)
                        val contactID = phoneCursor.getString(contactIdIndex)
                        var phoneNumber: String = "No phone number"
                        if (phoneCursor.getInt(contactHasPhoneNumberIndex) > 0) {
                            val phoneProjection = arrayOf(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                            val numberCursor = requireContext().contentResolver
                                .query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    phoneProjection,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    arrayOf(contactID),
                                    null
                                )
                            if (numberCursor != null) {
                                while (numberCursor.moveToNext()) {
                                    val numberIndex = numberCursor
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    if (numberIndex >= 0) {
                                        phoneNumber = numberCursor.getString(numberIndex)
                                    } else {
                                        Log.e("TEST_ERROR", "Phone number index column is invalid")
                                    }
                                }
                                numberCursor.close()
                            } else {
                                Log.e("TEST_ERROR", "Number cursor is null")
                            }
                        } else {
                            Log.e("TEST_ERROR", "No phone number is connected to this person")
                        }
                        Log.d("TEST", "$contactName, $contactID, $phoneNumber")
                        binding.suspectBtn.text = getString(R.string.crime_report_suspect, contactName)
                        crime.setSuspect(contactName)
                    } else {
                        Log.e("TEST_ERROR", "Phone cursor is null or empty")
                    }
                    phoneCursor.close()
                }
            }
        }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts
            .RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickContactLauncher.launch(null)
        } else {
            Toast.makeText(
                requireContext(),
                "You need to give permission to choose suspect",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

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
            crimeButton.setOnClickListener {
                val i = Intent(requireActivity(), CrimeCameraActivity::class.java)
                i.putExtra("FILE_URI", crime.getPhoto()?.fileName)
                launcher?.launch(i)
            }

            crimeImage.setOnClickListener {
                val FM = requireActivity().supportFragmentManager
                val imageFragment = ImageFragment.newInstance(crime.getPhoto()?.fileName.toString())

                imageFragment.show(FM, "ImageFragment")
            }

            reportBtn.setOnClickListener {
                var i = Intent(Intent.ACTION_SEND)
                i.setType("text/plain")
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
                i = Intent.createChooser(i, getString(R.string.send_report))
                startActivity(i)
            }

            suspectBtn.setOnClickListener {
                if(ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                    pickContactLauncher.launch(null)
                }
                else{
                    requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                }
            }

            val pm = requireActivity().packageManager
            if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
                crimeButton.isEnabled = false



            launcher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                    if (result.resultCode == RESULT_OK) {
                        val filePath = result.data?.getStringExtra("FILE_PATH")

                        if (filePath != null) {
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
        with(binding){
            crimeIsSolved.isChecked = crime.isSolved()
            crimeTitle.setText(crime.getTitle())
            crimeDateBtn.text = Utils.getStringDateOfCrime(crime)
            crimeTimeBtn.text = Utils.getStringTimeOfCrime(crime)
            Log.d("TEST", "Update screen data")
            if(crime.getSuspect() != null){
                binding.suspectBtn.text = getString(R.string.crime_report_suspect, crime.getSuspect())
            }
            else{
                binding.suspectBtn.text = getString(R.string.suspect_text)
            }
        }
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

    private fun showPhoto() {
        val photo = crime.getPhoto()
        if (photo != null) {
            binding.crimeImage.setImageURI(photo.fileName.toUri())
        }
    }

    private fun getCrimeReport(): String {

        val solvedString = if (crime.isSolved()) getString(R.string.crime_report_solved) else
            getString(R.string.crime_report_unsolved)

        val dateFormat = "EEE, MMM dd"
        val dateString = DateFormat.format(dateFormat, crime.getDate()).toString()

        val suspect = crime.getSuspect()
        val suspectString = if (suspect == null) getString(R.string.crime_report_no_suspect) else
            getString(R.string.crime_report_suspect, suspect)

        val report = getString(
            R.string.crime_report,
            crime.getTitle(), dateString, solvedString, suspectString
        )
        return report
    }

    override fun onPause() {
        super.onPause()
        CrimeLab.getInstance(requireContext()).saveCrimes()
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