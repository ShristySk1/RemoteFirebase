package com.ayata.esewaremotefirebase.data.datasource

import com.ayata.esewaremotefirebase.data.model.Notes
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseService {
    val databaseRef = FirebaseFirestore.getInstance()
    val noteReference=databaseRef.collection("notebook")
    val documentRef=noteReference.document("note")
    fun fetchAllNotes() {
//        documentRef.get().addOnSuccessListener { snapshots ->
//            if (snapshots.exists()) {
//                val list = snapshots.toObject(Notes::class.java)?.list
//            }
//        }
//       val query= noteReference.orderBy()
    }

    fun insertNote() {
        var data: HashMap<String, Any> = HashMap()
        data["title"] = "test"
        data["description"] = "test command"
        documentRef.set(data).addOnSuccessListener { snapshots ->
            //success
        }.addOnFailureListener {
            //failure
        }
    }
}