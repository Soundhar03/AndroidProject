package com.sample.firebaseauthapp.interactor

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

interface GoogleSignInInteractor {
    interface View {
        fun showErrorMessage(errorMessage:String)
    }

    interface Presenter {
        fun setView(view: View)
        fun initGoogleAuth(context: Context): GoogleSignInOptions
        fun isInternetConnected(context: Context) :Boolean
    }
}