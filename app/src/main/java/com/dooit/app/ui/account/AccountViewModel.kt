package com.dooit.app.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dooit.app.model.User
import com.dooit.app.repository.AccountRepository
import com.google.firebase.firestore.DocumentSnapshot

class AccountViewModel : ViewModel() {

    var accountRepository = AccountRepository()

    var accountLiveData : MutableLiveData<DocumentSnapshot> = MutableLiveData()
    var isLoadingLiveData : MutableLiveData<Boolean> = MutableLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getAccount() {
        isLoadingLiveData.value = true
        accountRepository.getAccount().get().addOnSuccessListener {
            isLoadingLiveData.value = false
            accountLiveData.value = it
        }
    }

}