package com.dooit.app.ui.transaction

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dooit.app.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_transaction.view.*

class TransactionFragment : Fragment() {

    private var fab: FloatingActionButton? = null
    private var list: RecyclerView? = null
    private var progress: ProgressBar? = null

    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        transactionViewModel =
            ViewModelProviders.of(this).get(TransactionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_transaction, container, false)
        fab = root.findViewById(R.id.fab)
        list = root.findViewById(R.id.list)
        progress = root.findViewById(R.id.progress)


        fab?.setOnClickListener {
            startActivity(Intent(activity, TransactionFormActivity::class.java))
        }

        transactionViewModel.getTransactionList()

        fetchData()

        return root
    }

    private fun fetchData() {

        transactionViewModel.transListLiveData.observe(this, Observer {

            if(it!=null) {
                val adapter = TransactionAdapter(it) {}
                val layoutManager = LinearLayoutManager(activity)
                list?.layoutManager = layoutManager
                list?.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
                list?.adapter = adapter
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
}