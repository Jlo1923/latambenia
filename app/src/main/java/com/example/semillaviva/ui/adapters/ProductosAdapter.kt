package com.example.semillaviva.ui.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.semillaviva.App
import com.example.semillaviva.R
import com.example.semillaviva.data.models.Producto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.productos_adapter.view.*
import java.util.*
import kotlin.collections.ArrayList
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProductosAdapter(private var readingsList: ArrayList<Producto>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var filterList = ArrayList<Producto>()
    lateinit var mcontext: Context

    class CountryHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val database: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    init {
        filterList = readingsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val countryListView =
                LayoutInflater.from(parent.context).inflate(R.layout.productos_adapter, parent, false)
        val sch = CountryHolder(countryListView)
        mcontext = parent.context
        return sch
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val nombre: TextView = holder.itemView.nombre
        val tipo: TextView = holder.itemView.tipo
        val precio: TextView = holder.itemView.precio
        val imageView: CircleImageView = holder.itemView.imgProducto
        val ivDelete: ImageView = holder.itemView.eliminar

        nombre.text = filterList[position].titulo
        tipo.text = filterList[position].tipo
        precio.text = filterList[position].resumen
        if (filterList[position].imagen.isEmpty()) {
            Picasso.with(mcontext)
                .load("https://firebasestorage.googleapis.com/v0/b/semilla-viva.appspot.com/o/logo_tambena.png?alt=media&token=90914145-21a4-46ad-b379-c57c66991cd1")
                .into(imageView)
        } else {
            Picasso.with(mcontext).load(filterList[position].imagen).into(imageView)
        }

       /* databaseReference = database!!.getReference("elementos")
        ivDelete.setOnClickListener {
            databaseReference!!.child(filterList[position].id).removeValue();
            filterList.removeAt(position)
            notifyDataSetChanged()
            Toast.makeText(mcontext, "se elimino", Toast.LENGTH_LONG).show()
        }*/
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = readingsList
                } else {
                    val resultList = ArrayList<Producto>()
                    for (row in readingsList) {
                        if (row.tipo.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))||
                                row.titulo.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))||
                                row.productor.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))||
                                row.telefono.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<Producto>
                notifyDataSetChanged()
            }
        }
    }

}
