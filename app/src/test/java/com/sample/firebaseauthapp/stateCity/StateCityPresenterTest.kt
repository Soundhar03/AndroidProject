package com.sample.firebaseauthapp.stateCity

import com.sample.firebaseauthapp.clientApi.ApiClient
import com.sample.firebaseauthapp.clientApi.IApiClient
import com.sample.firebaseauthapp.interactor.StateCityListIntereactor
import com.sample.firebaseauthapp.model.CityBo
import com.sample.firebaseauthapp.model.StateBO
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class StateCityPresenterTest{

    internal var stateList: List<StateBO>? = null

    @Mock
    private val view: StateCityListIntereactor.View? = null

    @Mock
    private var stateCityImpl: StateCityImpl? = null

    @Before
    fun setup() {
        initMocks(this)
        stateCityImpl = Mockito.spy<StateCityImpl>(StateCityImpl(view))
    }

    @Test
    fun getStateList(){

        val apiService: IApiClient = ApiClient.Client.getClient()!!.create(IApiClient::class.java)
        val call: Call<List<StateBO>> = apiService.getStateList("India")!!
        try {
            val response: Response<List<StateBO>> = call.execute()
            stateList = response.body()

            Mockito.verify<StateCityListIntereactor.View>(view, Mockito.never()).setAdapter(stateList!!)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun getCityList(){
        val cityList: List<CityBo>?
        val apiService: IApiClient = ApiClient.Client.getClient()!!.create(IApiClient::class.java)
        val callCity: Call<List<CityBo>> = apiService.getCityList("Tamil Nadu")!!
        try {
            val responseCity: Response<List<CityBo>> = callCity.execute()
            cityList = responseCity.body()
//            Mockito.verify<StateCityListIntereactor.View>(view, Mockito.never()).setAdapter(stateList!!)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}