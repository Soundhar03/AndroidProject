package com.sample.firebaseauthapp.presenter

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sample.firebaseauthapp.R
import com.sample.firebaseauthapp.interactor.GoogleSignInInteractor
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager



class GoogleSignInPresenter : GoogleSignInInteractor.Presenter {
    lateinit var mView: GoogleSignInInteractor.View
    override fun setView(view: GoogleSignInInteractor.View) {
        mView = view
    }

    override fun initGoogleAuth(context: Context): GoogleSignInOptions {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return gso
    }

    override fun isInternetConnected(context: Context) : Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        return isConnected
    }

}