package com.example.semillaviva.ui.perfil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.semillaviva.R
import com.example.semillaviva.data.preferences.UserPreferences
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_slideshow.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.IOException

class SlideshowFragment : Fragment(){

    private lateinit var slideshowViewModel: SlideshowViewModel

    private lateinit var preferences :UserPreferences
    var firstName: String = ""
    var lastName: String = ""
    var location: String = ""
    var telefono: String = ""
    var idDriver: String = ""
    var urlPhoto: String = ""
    private val PICK_IMAGE_REQUEST = 71
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var filePath: Uri? = null
    var mCallback: OnCallbackReceived? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val name: TextView = root.findViewById(R.id.firstName)
        val lastname: TextView = root.findViewById(R.id.lastName)
        val ubicacion: TextView = root.findViewById(R.id.ubicacion)
        val img: ImageView = root.findViewById(R.id.imgPerfil)
        loadPreferences()
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
          //  textView.text = it
        })
        name.text = firstName
        lastname.text = lastName
        ubicacion.text = location
        Picasso.with(requireContext()).load(urlPhoto).into(img)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        img.setOnClickListener {
             launchGallery()
        }
        return root
    }

    private fun loadPreferences() {
        preferences = UserPreferences(requireContext())
        firstName = preferences.getValueString(UserPreferences.USER_NOMBRE).toString()
        lastName = preferences.getValueString(UserPreferences.USER_PHONE).toString()
        location = preferences.getValueString(UserPreferences.USER_UBICACION).toString()
        urlPhoto = preferences.getValueString(UserPreferences.USER_IMAGEN).toString()

    }

    interface OnCallbackReceived {
        fun Update()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mCallback = activity as OnCallbackReceived
        } catch (e: ClassCastException) {
        }
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    filePath
                )
                imgPerfil.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(filePath: Uri?) {
        if(filePath != null){
            val ref = storageReference?.child("doc_usuario/$userName")
            val uploadTask = ref?.putFile(filePath)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                   /// downloadUri = task.result.toString()
                   // addUploadRecordToDb(downloadUri)
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(requireContext(), "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }
}