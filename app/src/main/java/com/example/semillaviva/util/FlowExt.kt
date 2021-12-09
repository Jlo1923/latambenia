package com.example.semillaviva.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
inline fun <reified T>CollectionReference.toFlow() = callbackFlow<List<T>> {

    val listener = this@toFlow.addSnapshotListener { value, error ->
        if(error == null){
            offer(value?.toObjects(T::class.java) ?: emptyList())
        }else{
            error(error)
        }
    }

    awaitClose {
        listener.remove()
    }
}

@ExperimentalCoroutinesApi
inline fun <reified T> DocumentReference.toFlow() = callbackFlow<T?> {

    val listener = this@toFlow.addSnapshotListener { value, error ->
        if(error == null){
            offer(value?.toObject(T::class.java))
        }else{
            error(error)
        }
    }

    awaitClose {
        listener.remove()
    }
}