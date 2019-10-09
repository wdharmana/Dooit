package com.dooit.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.dooit.app.model.User
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_auth.*
import timber.log.Timber

class AuthActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mGoogleApiClient: GoogleApiClient

    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        supportActionBar?.hide()

        setContentView(R.layout.activity_auth)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        mAuth = FirebaseAuth.getInstance()

        if(mAuth.currentUser != null) {
            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()


        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                Timber.d("onAuthStateChanged:signed_in:" + user.uid)
            } else {
               Timber.d("onAuthStateChanged:signed_out")
            }
        }

        mAuth.addAuthStateListener(mAuthListener)

        login_google.setOnClickListener {
            val signInIntent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, 1)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            val account = completedTask?.getResult(ApiException::class.java)

            Timber.e(Gson().toJson(account))

            progress.visibility = View.VISIBLE

            firebaseAuthWithGoogle(account)

        } catch (e: ApiException) {
            Timber.e(e)
            progress.visibility = View.GONE
        } catch (e: Exception) {
            progress.visibility = View.GONE
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {

        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    val mUser = mAuth.currentUser

                    val db = FirebaseFirestore.getInstance()
                    val cref = db.collection("users")
                    val q1 = cref.whereEqualTo("email", mUser?.getEmail())

                    q1.get().addOnSuccessListener{ task ->
                        if(task.size()==0) {
                            insertData(mUser)
                        } else {
                            finishAffinity()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }.addOnFailureListener { error ->
                        Timber.d(error)
                    }

                } else {
                    Timber.d("signInWithCredential:failure, ${it.exception?.message}")
                }
                progress.visibility = View.GONE
            }
    }

    private fun insertData(mUser: FirebaseUser?) {

        val db = FirebaseFirestore.getInstance()

        val user = User(
            email=mUser?.email,
            name = mUser?.displayName,
            phone = mUser?.phoneNumber,
            photo = mUser?.photoUrl.toString(),
            auth_mode = "1"
        )

        db.collection("users")
            .document(mUser?.uid!!)
            .set(user)
            .addOnSuccessListener {
                finishAffinity()
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener {
                Timber.d(it.message)
                finishAffinity()
                startActivity(Intent(this, MainActivity::class.java))
            }

    }
}
