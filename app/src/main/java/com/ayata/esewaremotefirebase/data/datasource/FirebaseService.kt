package com.ayata.esewaremotefirebase.data.datasource

import com.ayata.esewaremotefirebase.data.model.Commands
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseService {
    val documentReference = FirebaseFirestore.getInstance().document("sample/command")
    fun fetchAllCommands() {
        documentReference.get().addOnSuccessListener { snapshots ->
            if (snapshots.exists()) {
                val list = snapshots.toObject(Commands::class.java)?.list
            }
        }
    }
}