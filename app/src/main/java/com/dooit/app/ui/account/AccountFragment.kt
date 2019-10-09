package com.dooit.app.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.dooit.app.AuthActivity
import com.dooit.app.R
import com.dooit.app.util.GlideApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel

    private lateinit var mAuth: FirebaseAuth
    private var mUser: FirebaseUser? = null

    private var tvName: TextView? = null
    private var imgAvatar: ImageView? = null
    private var progress: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel =
            ViewModelProviders.of(this).get(AccountViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_account, container, false)
        val tvLogout: TextView = root.findViewById(R.id.tv_logout)

        imgAvatar = root.findViewById<ImageView>(R.id.img_avatar)
        tvName = root.findViewById<TextView>(R.id.tv_name)
        progress = root.findViewById(R.id.progress)

        accountViewModel.text.observe(this, Observer {

        })

        tvLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            activity?.finishAffinity()
            startActivity(Intent(activity, AuthActivity::class.java))
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        observeViewModel()

        if (mAuth.currentUser != null) {
            accountViewModel.getAccount()
        }
    }

    private fun observeViewModel() {
        accountViewModel.accountLiveData.observe(this, Observer {
            tvName?.text = it.get("name").toString()

            context?.let { ctx ->

                imgAvatar?.let { imgAvatar ->
                    GlideApp.with(ctx)
                        .asBitmap()
                        .centerCrop()
                        .load(it.get("photo").toString())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgAvatar)
                }

            }
        })

        accountViewModel.isLoadingLiveData.observe(this, Observer {
            if(it) {
                progress?.visibility = View.VISIBLE
            } else {
                progress?.visibility = View.GONE
            }
        })

    }

}