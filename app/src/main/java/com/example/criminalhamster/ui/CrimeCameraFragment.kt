package com.example.criminalhamster.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.criminalhamster.databinding.FragmentCrimeCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CrimeCameraFragment : Fragment() {

    private lateinit var binding: FragmentCrimeCameraBinding
    private lateinit var captureIV: ImageView
    private lateinit var imageURI: Uri

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        captureIV.setImageURI(null)
        captureIV.setImageURI(imageURI)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCrimeCameraBinding.inflate(inflater)
        imageURI = createImageUri()

        with(binding) {
            mraz.setOnClickListener {
                cameraLauncher.launch(imageURI)
            }
            backBtn.setOnClickListener {
                requireActivity().finish()
            }
            saveBtn.setOnClickListener {
                val i = Intent()
                i.putExtra("FILE_PATH", imageURI.toString())
                requireActivity().setResult(RESULT_OK, i)
                requireActivity().finish()
            }

            captureIV = IV

            val uriFromIntent = requireActivity().intent?.getStringExtra("FILE_URI")
            if(uriFromIntent  != null){
                val imageUri = uriFromIntent.toUri()
                captureIV.setImageURI(imageUri)
            }
        }


        return binding.root
    }

    fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "camera_photo_$timeStamp.png"
        Log.d("TEST", imageFileName)

        val image = File(requireActivity().filesDir, imageFileName )
        return FileProvider.getUriForFile(
            requireActivity(),
            "com.example.criminalhamster.ui.FileProvider",
            image
        )
    }
}