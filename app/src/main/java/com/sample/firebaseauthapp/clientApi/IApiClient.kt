package com.sample.firebaseauthapp.clientApi

import com.sample.firebaseauthapp.model.CountryBO
import com.sample.firebaseauthapp.model.CityBo
import com.sample.firebaseauthapp.model.StateBO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface IApiClient {

    @GET("countries/")
    fun getCountryList(): Call<List<CountryBO>>?

    @GET("states/{name}")
    fun getStateList(@Path(value="name")stateName:String): Call<List<StateBO>>?

    @GET("cities/{name}")
    fun getCityList(@Path(value="name") cityName:String): Call<List<CityBo>>?

}