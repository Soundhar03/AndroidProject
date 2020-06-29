package com.sample.firebaseauthapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityBo {
    @Expose
    @SerializedName("city_name")
    var city_name: String? = null

}