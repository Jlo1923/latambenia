package com.example.semillaviva.data.models

        data class Producto(
                var id:String,
                var titulo:String,
                var tipo:String,
                var imagen:String,
                var resumen:String,
                var productor:String,
                var telefono: String,
                val tipo_cultivo: String
        )
        {
                constructor() : this("","", "","","","","","")
        }



