package com.example.semillaviva.ui.auth.fragments

import android.annotation.TargetApi
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.semillaviva.MenuActivity
import com.example.semillaviva.R
import com.example.semillaviva.data.preferences.UserPreferences
import com.example.semillaviva.databinding.FragmentMainRegisterBinding
import com.example.semillaviva.util.text
import com.example.semillaviva.util.toastF
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main_register.*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
@AndroidEntryPoint
class RegisterAuthFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentMainRegisterBinding

    private lateinit var preferences : UserPreferences
    var token: String = ""
    var url: String = ""
    private var GALLERY_INTENT = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMainRegisterBinding = FragmentMainRegisterBinding.inflate(inflater, container, false)

        loadPreferences()
        return binding.root
    }

    private fun loadPreferences() {
        preferences = UserPreferences(requireActivity())
        token = preferences.getValueString(UserPreferences.USER_TOKEN).toString()
    }


    override fun onResume() {
        super.onResume()
        buttonBack.setOnClickListener {

            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = MainAuthFragment()
            fragmentTransaction.add(R.id.fragment_container, fragment)
            fragmentTransaction.commit()
        }
        imgUser.setOnClickListener {

            if (inputCedula.text().isEmpty() || inputName.text().isEmpty()){
                toastF("Por favor dijite número de documento y nombres")
            }else{
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, GALLERY_INTENT)
            }
        }
        buttonReg.setOnClickListener {
            val referenceLcl = FirebaseDatabase.getInstance().reference
            val  myRef= referenceLcl.child("productores")
            val hashLcl: HashMap<String, Any?> = HashMap()
            hashLcl["cedula"] = inputCedula.text()
            hashLcl["nombre"] = inputName.text()
            hashLcl["telefono"] = inputPhone.text()
            hashLcl["imagen"] = url
            hashLcl["hora_atencion"] = inputRestricciones.text()
            hashLcl["ubicacion"] = inputDirecciones.text()
            hashLcl["contrasenia"] = inputPassword.text()
            val keyLcl = myRef.push().key
           // hashLcl["key"] = keyLcl

            preferences.save(UserPreferences.USER_ID, keyLcl.toString())
            val task: Task<*> = referenceLcl.child("productores").child(keyLcl!!).setValue(hashLcl)
            task.addOnSuccessListener { aVoid: Any? ->
                 toastF("Se registro sacctisfactoriamente")
                preferences.save(UserPreferences.USER_ID, keyLcl)
                preferences.save(UserPreferences.USER_CEDULA, inputCedula.text())
                preferences.save(UserPreferences.USER_PHONE, inputPhone.text())
                preferences.save(UserPreferences.USER_NOMBRE, inputName.text())
                preferences.save(UserPreferences.USER_IMAGEN, url)
                preferences.save(UserPreferences.USER_HORA, inputRestricciones.text())
                preferences.save(UserPreferences.USER_UBICACION, inputDirecciones.text())
                goToMenu()
            }

        }
        }
    private fun goToMenu() {
        preferences.save(UserPreferences.IS_LOGGED, true)
        val intent = Intent(requireActivity(), MenuActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        //VERIFICA QUE SE HAYA SELECCIONADO UNA FOTO
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            val progressDialog = ProgressDialog(requireContext())
            //si todo sale  bien entonces cargamos la barra de espera
            progressDialog.setTitle("Subiendo...")
            progressDialog.setMessage("Subiendo Foto")
            progressDialog.setCancelable(false) //para que al clickear fuera del cuadrado no se salga
            progressDialog.show()
            val uri: Uri? = data!!.data
            val storageRef = FirebaseStorage.getInstance().reference
            val filePath: StorageReference =
                storageRef.child("images/").child(inputCedula.text()+inputName.text())

            filePath.putFile(uri!!).addOnSuccessListener {
                progressDialog.dismiss()
                filePath.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .into(
                            imgUser
                        )
                    url=uri.toString()
                    buttonReg.isEnabled =true
                }
            }
        }
    }
}