package com.example.criminalhamster.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment

class ImageFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val imageUri = arguments?.getString("IMAGE_PATH")
        val image = ImageView(requireActivity()).also {
            it.setImageURI(imageUri?.toUri())
        }


        return image
    }

    companion object {
        @JvmStatic
        fun newInstance(imagePath: String) = ImageFragment().also {

            it.arguments = Bundle().also { args ->
                args.putSerializable("IMAGE_PATH", imagePath)
            }
            it.setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        }
    }
}