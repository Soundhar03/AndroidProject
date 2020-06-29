package com.sample.firebaseauthapp.presenter

import android.content.Context
import com.sample.firebaseauthapp.clientApi.ApiClient
import com.sample.firebaseauthapp.clientApi.IApiClient
import com.sample.firebaseauthapp.model.CityBo
import com.sample.firebaseauthapp.model.StateBO
import com.sample.firebaseauthapp.interactor.StateCityListIntereactor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StateCityPresenter :
    StateCityListIntereactor.Presenter {
    lateinit var mView: StateCityListIntereactor.View

    override fun setView(view: StateCityListIntereactor.View) {
        mView = view
    }

    override fun callStateListApi(stateName: String): List<StateBO> {
        var stateList: List<StateBO>? = ArrayList()
        val apiService: IApiClient = ApiClient.Client.getClient()!!.create(IApiClient::class.java)
        val call: Call<List<StateBO>> = apiService.getStateList(stateName)!!
        mView.showPogress()
        call.enqueue(object : Callback<List<StateBO>> {
            override fun onResponse(
                call: Call<List<StateBO>>,
                response: Response<List<StateBO>>
            ) {
                mView.hideProgress()
                if(response.body()!=null){
                    stateList = response.body()
                    mView.setAdapter(stateList!!)
                    mView.setStateList(stateList!!)
                }
            }

            override fun onFailure(
                call: Call<List<StateBO>>,
                t: Throwable
            ) {

                if (t.toString() == "java.net.SocketTimeoutException: timeout") {
                    callStateListApi(stateName)
                }
                mView.hideProgress()

            }
        })
        return stateList!!
    }

    override fun callCityListApi(context: Context, cityName: String, position: Int) {
        var cityList: List<CityBo>? = ArrayList()
        val apiService: IApiClient = ApiClient.Client.getClient()!!.create(IApiClient::class.java)
        val call: Call<List<CityBo>> = apiService.getCityList(cityName)!!

        call.enqueue(object : Callback<List<CityBo>> {
            override fun onResponse(
                call: Call<List<CityBo>>,
                response: Response<List<CityBo>>
            ) {
                mView.hideProgress()
                cityList = response.body()
                mView.addCity(cityList!!, position)

            }

            override fun onFailure(
                call: Call<List<CityBo>>,
                t: Throwable
            ) {
                if (t.toString() == "java.net.SocketTimeoutException: timeout") {
                    callCityListApi(context, cityName, position)
                }
                mView.hideProgress()

            }
        })

    }


}