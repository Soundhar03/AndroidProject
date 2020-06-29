package com.example.weatherapp.countrylist

import com.sample.firebaseauthapp.clientApi.ApiClient
import com.sample.firebaseauthapp.clientApi.IApiClient
import com.sample.firebaseauthapp.country.CountryImpl
import com.sample.firebaseauthapp.interactor.CountryListInteractor
import com.sample.firebaseauthapp.model.CountryBO
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class CountryPesenterTest {

    internal var countryList: List<CountryBO>? = null

    @Mock
    private val view: CountryListInteractor.View? = null

    @Mock
    private var countryImpl: CountryImpl? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        countryImpl = Mockito.spy<CountryImpl>(CountryImpl(view))
    }

    @Test
    fun getCountryList() {

        val apiService: IApiClient = ApiClient.Client.getClient()!!.create(IApiClient::class.java)
        val call: Call<List<CountryBO>> = apiService.getCountryList()!!
        try {
            val response: Response<List<CountryBO>> = call.execute()
            countryList = response.body()
            Mockito.verify<CountryListInteractor.View>(view, Mockito.never()).setAdapter(countryList!!)

        } catch (e: IOException) {
            e.printStackTrace()

        }

    }

}



