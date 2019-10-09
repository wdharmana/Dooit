package com.dooit.app.ui.transaction

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dooit.app.MainActivity
import com.dooit.app.R
import com.dooit.app.model.Transaction
import kotlinx.android.synthetic.main.activity_transaction_form.*

class TransactionFormActivity : AppCompatActivity() {

    private lateinit var transactionViewModel: TransactionViewModel
    private var selectedType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_form)

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel::class.java)

        setupTypes()

        btn_save.setOnClickListener {

            var debit = 0
            var credit = 0

            if(selectedType==0) {
                credit = et_amount.text.toString().toInt()
            } else {
                debit = et_amount.text.toString().toInt()
            }

            val transaction = Transaction(
                remark = et_remark.text.toString() ?: "",
                debit = debit,
                credit = credit
            )

            transactionViewModel.saveTransaction(transaction)

        }

        observeViewModel()


    }

    private fun observeViewModel() {
        transactionViewModel.isSuccess.observe(this, Observer {
            if(it) {
                Toast.makeText(this, "Transaksi berhasil ditambahkan!", Toast.LENGTH_LONG).show()
                finishAffinity()
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        transactionViewModel.isLoadingLiveData.observe(this, Observer {
            if(it) {
                progress?.visibility = View.VISIBLE
            } else {
                progress?.visibility = View.GONE
            }
        })

    }

    private fun setupTypes() {
        val lists = arrayListOf<String>()
        lists.add("Pengeluaran")
        lists.add("Pendapatan")
        et_type.setText(lists.get(selectedType))

        val popupTypes = PopupMenu(this, et_type)
        et_type.setOnClickListener {

            popupTypes.menu.clear()
            var i = 0
            for(x in lists) {
                popupTypes.menu.add(i, Menu.NONE, Menu.NONE, x)
                i++
            }

            popupTypes.setOnMenuItemClickListener {
                selectedType = it.groupId
                et_type.setText(lists.get(selectedType))
                true
            }

            popupTypes.show()

        }
    }


}
