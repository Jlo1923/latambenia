package com.example.semillaviva.ui.productos

import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.semillaviva.R
import com.example.semillaviva.data.models.Producto
import com.example.semillaviva.data.preferences.UserPreferences
import com.example.semillaviva.ui.adapters.ProductosAdapter
import com.example.semillaviva.util.text
import com.example.semillaviva.util.toastF
import com.google.android.gms.tasks.Task
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_mis_productos.*
import kotlinx.android.synthetic.main.fragment_mis_productos.view.*

class MisProductosFragment : Fragment() {

    private lateinit var misProductosViewModel: MisProductosViewModel
    private lateinit var todoAdapter: ProductosAdapter
    private lateinit var preferences :UserPreferences
    var nombre: String = ""
    var cedula: String = ""
    var hora_atencion: String = ""
    var telefono_productor: String = ""
    var ubicacion_productor: String = ""
    var url: String = ""
    private var GALLERY_INTENT = 123

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        misProductosViewModel =
            ViewModelProvider(this).get(MisProductosViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mis_productos, container, false)
        misProductosViewModel.text.observe(viewLifecycleOwner, Observer {
        })

        loadPreferences()

        val tipo: Spinner = root.findViewById(R.id.tipo)
        val tipo_cultivo: Spinner = root.findViewById(R.id.tipo_cultivo)
        ArrayAdapter.createFromResource(
            requireContext(), R.array.tipos, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tipo.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            requireContext(), R.array.tipos_cultivos, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tipo_cultivo.adapter = adapter
        }
        val list :ArrayList<Producto> = ArrayList()
        val rootRef = FirebaseDatabase.getInstance().reference

        val query = rootRef.child("elementos").orderByChild("documento").equalTo(cedula)
        query.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, key: String?) {
                val outbox = dataSnapshot.getValue(Producto::class.java)
                val produ= Producto()
                produ.id = key.toString()
                produ.titulo = outbox!!.titulo
                produ.tipo = outbox.tipo
                produ.imagen = outbox.imagen
                produ.resumen = outbox.resumen
                list.add(produ)

                val rv: RecyclerView = root.findViewById(R.id.recyclerView)
                rv.layoutManager = LinearLayoutManager(context)
                todoAdapter = ProductosAdapter(list)
                rv.adapter = todoAdapter
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })

        root.buttonRegPro.setOnClickListener {
            val referenceLcl = FirebaseDatabase.getInstance().reference
            val  myRef= referenceLcl.child("elementos")
            val hashLcl: HashMap<String, Any?> = HashMap()
            hashLcl["descripcion_larga"] = inputdescripcion.text()
            hashLcl["resumen"] = precio.text()
            hashLcl["productor"] = nombre
            hashLcl["imagen"] = url
            hashLcl["documento"] = cedula
            hashLcl["hora_atencion"] = hora_atencion
            hashLcl["telefono_productor"] = telefono_productor
            hashLcl["tipo"] = tipo.selectedItem.toString()
            hashLcl["tipo_cultivo"] = tipo_cultivo.selectedItem.toString()
            hashLcl["titulo"] = input_nombre.text()
            hashLcl["ubicacion_productor"] = ubicacion_productor
            val keyLcl = myRef.push().key

            preferences.save(UserPreferences.USER_ID, keyLcl.toString())
            val task: Task<*> = referenceLcl.child("elementos").child(keyLcl!!).setValue(hashLcl)
            task.addOnSuccessListener { aVoid: Any? ->
                toastF("Se registro sacctisfactoriamente")
            }
        }

        root.imgProducto.setOnClickListener {

            if (input_nombre.text().isEmpty()){
                toastF("Por favor dijite el nombre del producto")
            }else{
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, GALLERY_INTENT)
            }
        }
        return root
    }

    private fun loadPreferences() {
        preferences = UserPreferences(requireContext())
        cedula = preferences.getValueString(UserPreferences.USER_CEDULA).toString()
        nombre = preferences.getValueString(UserPreferences.USER_NOMBRE).toString()
        telefono_productor = preferences.getValueString(UserPreferences.USER_PHONE).toString()
        ubicacion_productor = preferences.getValueString(UserPreferences.USER_UBICACION).toString()
        hora_atencion = preferences.getValueString(UserPreferences.USER_HORA).toString()

    }
    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        //VERIFICA QUE SE HAYA SELECCIONADO UNA FOTO
        if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK) {
            val progressDialog = ProgressDialog(requireContext())
            //si todo sale  bien entonces cargamos la barra de espera
            progressDialog.setTitle("Subiendo...")
            progressDialog.setMessage("Cargando producto")
            progressDialog.setCancelable(false) //para que al clickear fuera del cuadrado no se salga
            progressDialog.show()
            val uri: Uri? = data!!.data
            val storageRef = FirebaseStorage.getInstance().reference
            val filePath: StorageReference =
                storageRef.child("images/").child(input_nombre.text())

            filePath.putFile(uri!!).addOnSuccessListener {
                progressDialog.dismiss()
                filePath.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .into(
                            imgProducto
                        )
                    url=uri.toString()
                }
            }
        }
    }
}