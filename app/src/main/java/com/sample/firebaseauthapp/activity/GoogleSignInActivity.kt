package com.sample.firebaseauthapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.sample.firebaseauthapp.R
import com.sample.firebaseauthapp.databinding.ActivityLoginBinding
import com.sample.firebaseauthapp.interactor.GoogleSignInInteractor
import com.sample.firebaseauthapp.presenter.GoogleSignInPresenter


class GoogleSignInActivity : AppCompatActivity(),
    GoogleSignInInteractor.View {

    private val TAG = "GoogleSignIn"
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mView: GoogleSignInInteractor.View
    private var mPresenter: GoogleSignInInteractor.Presenter? = null
    private var googleSignInButton: SignInButton? = null
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_login
        )
        init()

        binding.signInButton?.setOnClickListener {

            var isConnected: Boolean? = mPresenter?.isInternetConnected(this)
            if (isConnected!!) {
                val signInIntent = googleSignInClient?.getSignInIntent()
                startActivityForResult(signInIntent, 101)
            } else {
                mView.showErrorMessage("Check the internet connection")
            }
        }
    }

    private fun init() {
        mPresenter = GoogleSignInPresenter()
        mPresenter?.setView(this)
        mView = this

        var gso: GoogleSignInOptions? = mPresenter?.initGoogleAuth(this)
        googleSignInClient = GoogleSignIn.getClient(this, gso!!)
    }

    override fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun onLoggedIn(account: GoogleSignInAccount) {
        mView.showErrorMessage(account.displayName + " successfully logged In")
        val intent = Intent(this, CountryActivity::class.java)
        startActivity(intent)
        finish()
    }

    public override fun onStart() {
        super.onStart()
        val alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (alreadyloggedAccount != null) {
            Toast.makeText(
                this,
                alreadyloggedAccount.displayName + " Already Logged In",
                Toast.LENGTH_SHORT
            ).show()
            onLoggedIn(alreadyloggedAccount)
        } else {
            Log.e(TAG, "Not logged in")
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                101 -> try {
                    // The Task returned from this call is always completed, no need to attach
                    // a listener.
                    val task =
                        GoogleSignIn.getSignedInAccountFromIntent(
                            data
                        )
                    val account = task.getResult(ApiException::class.java)
                    onLoggedIn(account!!)

                } catch (e: ApiException) {
                    // The ApiException status code indicates the detailed failure reason.
                    mView.showErrorMessage("Google singIn failed: " + e.statusCode)
                }

            }
    }

}
