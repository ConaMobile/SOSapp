package com.conamobile.sosapp.api

import com.google.gson.annotations.SerializedName

data class Yordam(
    @SerializedName("ism") val name: List<String>,

    @SerializedName("tugilgan_yil") val birthYear: List<String>,

    @SerializedName("tel_raqam") val phoneNumber: List<String>,

    @SerializedName("longlitude") val longitude: List<String>,

    @SerializedName("latitude") val latitude: List<String>,
)