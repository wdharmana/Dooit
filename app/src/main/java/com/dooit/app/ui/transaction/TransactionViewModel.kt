package com.dooit.app.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dooit.app.model.Transaction
import com.dooit.app.repository.AccountRepository
import com.dooit.app.repository.TransactionRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import timber.log.Timber

class TransactionViewModel : ViewModel() {

    var transactionRepository = TransactionRepository()

    var transListLiveData: MutableLiveData<List<Transaction>> = MutableLiveData()
    var isLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var isSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun saveTransaction(transaction: Transaction) {
        isLoadingLiveData.value = true
        transactionRepository.saveTransaction(transaction).addOnSuccessListener {
            isLoadingLiveData.value = false
            isSuccess.value = true
        }
            .addOnFailureListener {
                isLoadingLiveData.value = false
            }
    }

    fun getTransactionList()  {
        val lists: ArrayList<Transaction> = arrayListOf()
        transactionRepository.getTransactionList().get().addOnSuccessListener {

            it.forEach {  qs ->
                val doc = qs.toObject(Transaction::class.java)
                doc.id = qs.id

                lists.add(doc)
            }

            transListLiveData.value = lists

            Timber.d(Gson().toJson(lists))

        }.addOnFailureListener {
            Timber.e(it.message)
            transListLiveData.value = lists
        }

    }

}