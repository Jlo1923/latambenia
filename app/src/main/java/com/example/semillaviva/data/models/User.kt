package com.example.semillaviva.data.models

import com.google.firebase.firestore.DocumentId

        data class User(
                var cedula:String,
        val nombre:String,
        val telefono:String,
        val imagen:String,
        val restriccion:String,
        val hora_atencion:String,
        val ubicacion:String,
        val contrasena:String
       // val location:Location
        )
        {
                constructor() : this("", "","","", "","", "", "")
        }



