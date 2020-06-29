package com.sample.firebaseauthapp.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class CountryBO {

    @Expose
    @SerializedName("country_name")
    var country_name: String? = null


}