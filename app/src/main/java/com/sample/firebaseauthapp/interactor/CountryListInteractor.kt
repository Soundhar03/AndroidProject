package com.sample.firebaseauthapp.interactor

import com.sample.firebaseauthapp.model.CountryBO

interface CountryListInteractor {
    interface View {
        fun setAdapter(country: List<CountryBO>)
        fun showErrorMessage(msg:String)
        fun getCountryList():List<CountryBO>
        fun setCountryList(country: List<CountryBO>)
        fun showProgressbar()
        fun hidePogressbar()
    }

    interface Presenter {
        fun setView(view: View)
        fun callCountryListApi()

    }
}