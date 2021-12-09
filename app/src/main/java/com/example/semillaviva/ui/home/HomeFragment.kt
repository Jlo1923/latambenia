 package com.example.semillaviva.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semillaviva.R
import com.example.semillaviva.data.models.Producto
import com.example.semillaviva.ui.adapters.ProductoresAdapter
import com.example.semillaviva.ui.adapters.ProductosAdapter
import com.example.semillaviva.util.text
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_main_auth.*

 class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var todoAdapter: ProductoresAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val list :ArrayList<Producto> = ArrayList()
        val rootRef = FirebaseDatabase.getInstance().reference

        val query = rootRef.child("elementos")
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
                    produ.titulo = outbox!!.titulo
                    produ.tipo = outbox.tipo
                    produ.productor = outbox.productor
                    produ.telefono = outbox.telefono
                    produ.imagen = outbox.imagen
                    produ.resumen = outbox.resumen
                    list.add(produ)

                    val rv: RecyclerView = root.findViewById(R.id.recyclerView)
                    val search: androidx.appcompat.widget.SearchView = root.findViewById(R.id.searchv)
                    rv.layoutManager = LinearLayoutManager(requireContext())
                    todoAdapter = ProductoresAdapter(list)
                    rv.adapter = todoAdapter

                    search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        todoAdapter.filter.filter(newText!!)
                        return false
                    }
                })
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        })
        return root
    }
}