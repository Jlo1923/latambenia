package com.example.semillaviva.ui.auth.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.semillaviva.MenuActivity
import com.example.semillaviva.R
import com.example.semillaviva.data.preferences.UserPreferences
import com.example.semillaviva.databinding.FragmentMainAuthBinding
import com.example.semillaviva.util.text
import com.example.semillaviva.util.toastF
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main_auth.*


// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@AndroidEntryPoint
class MainAuthFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentMainAuthBinding

    private lateinit var preferences : UserPreferences
    var token: String = ""
    var contrasenia: String = ""
    var nombre: String = ""
    var hora: String = ""
    var cedula: String = ""
    var imagen: String = ""
    var telefono: String = ""
    var ubicacion: String = ""
    var key: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainAuthBinding.inflate(inflater, container, false)

        loadPreferences()
        return binding.root
    }

    private fun loadPreferences() {
        preferences = UserPreferences(requireActivity())
        token = preferences.getValueString(UserPreferences.USER_TOKEN).toString()
    }


    override fun onResume() {
        super.onResume()
            buttonLog.setOnClickListener {
                val progressDialog = ProgressDialog(requireContext())
                progressDialog.setTitle("Semilla viva")
                progressDialog.setMessage("Espere un momento por favor")
                progressDialog.show()
                val rootRef = FirebaseDatabase.getInstance().reference
                val itemsRef = rootRef.child("productores").orderByChild("cedula").equalTo(inputUser.text())
                val valueEventListener: ValueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val list: MutableList<String> = ArrayList()
                        for (ds in dataSnapshot.children) {
                            key= ds.key!!
                            contrasenia= ds.child("contrasenia").value.toString()
                            nombre= ds.child("nombre").value.toString()
                            telefono= ds.child("telefono").value.toString()
                            cedula= ds.child("cedula").value.toString()
                            imagen= ds.child("imagen").value.toString()
                            hora= ds.child("hora_atencion").value.toString()
                            ubicacion= ds.child("ubicacion").value.toString()
                            var contrasenia1 = ds.child("contrasenia")
                                .getValue(String::class.java)!!
                            list.add(contrasenia1)
                        }
                        Log.d("prueba", list.toString())
                        if (contrasenia.isEmpty()){
                            toastF("el usuario no se encuentra registrado")
                            progressDialog.dismiss()
                        }else if (contrasenia == inputPass.text()){
                            progressDialog.dismiss()
                            preferences.save(UserPreferences.USER_ID, key)
                            preferences.save(UserPreferences.USER_CEDULA, cedula)
                            preferences.save(UserPreferences.USER_PHONE, telefono)
                            preferences.save(UserPreferences.USER_NOMBRE, nombre)
                            preferences.save(UserPreferences.USER_IMAGEN, imagen)
                            preferences.save(UserPreferences.USER_HORA, hora)
                            preferences.save(UserPreferences.USER_UBICACION, ubicacion)
                            goToMenu()
                        }else{
                            toastF("La contraseña es incorrecta")
                            progressDialog.dismiss()
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                }
                itemsRef.addListenerForSingleValueEvent(valueEventListener)
            }
            buttonReg.setOnClickListener {
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment = RegisterAuthFragment()
                fragmentTransaction.add(R.id.fragment_container, fragment)
                fragmentTransaction.commit()
            }

        }

    private fun goToMenu() {
        preferences.save(UserPreferences.IS_LOGGED, true)
        val intent = Intent(requireActivity(), MenuActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainAuthFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainAuthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}