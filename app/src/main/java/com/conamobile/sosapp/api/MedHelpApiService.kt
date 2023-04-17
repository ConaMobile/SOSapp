package com.conamobile.sosapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MedHelpApiService {

    @POST("/yordam/")
    fun createYordam(@Body helper: Helper): Call<Helper>

}