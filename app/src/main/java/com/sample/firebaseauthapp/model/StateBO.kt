package com.sample.firebaseauthapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StateBO {
    @Expose
    @SerializedName("state_name")
    var state_name: String? = null

    var cityBo: List<CityBo>? = null
    var isLoaded:Boolean=false




}