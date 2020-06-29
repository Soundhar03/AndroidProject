package com.sample.firebaseauthapp.presenter

import com.sample.firebaseauthapp.model.CountryBO
import com.sample.firebaseauthapp.clientApi.ApiClient
import com.sample.firebaseauthapp.clientApi.IApiClient
import com.sample.firebaseauthapp.interactor.CountryListInteractor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryPesenter : CountryListInteractor.Presenter {
    lateinit var mView: CountryListInteractor.View

    override fun setView(view: CountryListInteractor.View) {
        mView = view
    }

    override fun callCountryListApi() {

        var country: List<CountryBO>? = ArrayList()
        val apiService: IApiClient = ApiClient.Client.getClient()!!.create(IApiClient::class.java)
        val call: Call<List<CountryBO>> = apiService.getCountryList()!!
        mView.showProgressbar()
        call.enqueue(object : Callback<List<CountryBO>> {
            override fun onResponse(
                call: Call<List<CountryBO>>,
                response: Response<List<CountryBO>>
            ) {
                if (response.body() != null) {
                    country = response.body()
                    mView.setAdapter(country!!)
                    mView.setCountryList(country!!)
                }
                mView.hidePogressbar()
            }

            override fun onFailure(
                call: Call<List<CountryBO>>,
                t: Throwable
            ) {
                if (t.toString() == "java.net.SocketTimeoutException: timeout") {
                    callCountryListApi()
                }
                mView.showErrorMessage(t.toString())
                mView.hidePogressbar()
            }
        })
    }
}