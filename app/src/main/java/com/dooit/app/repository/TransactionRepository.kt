package com.dooit.app.repository

import com.dooit.app.model.Transaction
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class TransactionRepository {

    var firestoreDB = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser
    val path = "users/${user!!.uid}/transactions"

    fun getTransactionList(): Query  {
        return firestoreDB.collection(path)
            .orderBy("created_at",
                Query.Direction.ASCENDING)
    }

    fun saveTransaction(transaction: Transaction) : Task<Void> {
        return firestoreDB.collection(path).document()
            .set(transaction)
    }

}

