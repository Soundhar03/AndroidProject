package com.sample.firebaseauthapp.interactor

import android.content.Context
import com.sample.firebaseauthapp.model.CityBo
import com.sample.firebaseauthapp.model.StateBO

interface StateCityListIntereactor {

    interface View {
        fun setAdapter(stateList: List<StateBO>)
        fun addCity(cityList: List<CityBo>, grouppos: Int)
        fun getStateList(): List<StateBO>
        fun setStateList(stateList: List<StateBO>)
        fun showPogress()
        fun hideProgress()
    }

    interface Presenter {
        fun setView(view: View)
        fun callStateListApi(stateName: String): List<StateBO>
        fun callCityListApi(context: Context, cityName: String, position: Int)

    }
}