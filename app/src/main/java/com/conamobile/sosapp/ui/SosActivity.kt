package com.conamobile.sosapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.telephony.SmsManager
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import com.conamobile.sosapp.api.Helper
import com.conamobile.sosapp.api.MedHelpApiService
import com.conamobile.sosapp.databinding.ActivitySosBinding
import com.conamobile.sosapp.pref.SharedPreferences
import com.conamobile.sosapp.util.LocationData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SosActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySosBinding
    private var saveLocation = LocationData()
    private val sharedPreferences by lazy {
        SharedPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun initViews() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_COARSE
        criteria.isAltitudeRequired = false
        criteria.isSpeedRequired = false
        criteria.isBearingRequired = false
        criteria.isCostAllowed = false
        val lat: Double
        val lng: Double
        val provider = locationManager.getBestProvider(criteria, false)

        val location: Location? = provider?.let { locationManager.getLastKnownLocation(it) }
        if (location != null) {
            lat = location.latitude
            lng = location.longitude
            saveLocation = LocationData(latitude = lat, longitude = lng)
            binding.latLng.text = "latitude: $lat\nlongitude: $lng"
            sendSms(lat, lng)
        } else Toast.makeText(
            this, "Please enable location and location permission", Toast.LENGTH_LONG
        ).show()
        sendLocation()
        binding.exit.setOnClickListener {
            this.finishAffinity()
        }
    }

    private fun sendSms(lat: Double, lng: Double) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(
            sharedPreferences.isHelpNumber(),
            null,
            "${sharedPreferences.isName()}ga yordam kerak!\n${sharedPreferences.isPhone()}.\nhttps://www.google.com/maps/search/?api=1&query=$lat,$lng",
            null,
            null
        )
    }

    private fun sendLocation() {
        val retrofit = Retrofit.Builder().baseUrl("https://medhelp23.pythonanywhere.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val apiService = retrofit.create(MedHelpApiService::class.java)

        val yordam = Helper(
            ism = sharedPreferences.isName(),
            tugilgan_yil = sharedPreferences.isDate()!!.toInt(),
            tel_raqam = sharedPreferences.isPhone(),
            longlitude = "${saveLocation.latitude}",
            latitude = "${saveLocation.latitude}"
        )

        apiService.createYordam(yordam).enqueue(object : Callback<Helper> {
            override fun onResponse(call: Call<Helper>, response: Response<Helper>) {
                if (response.isSuccessful) {
                    val saveString = binding.latLng.text.toString()
                    val savable = SpannableStringBuilder()
                        .append(saveString)
                        .bold { append("\nSo'rovingiz muvaffaqiyatli jo'natildi!") }
                    binding.latLng.text = savable
                    Toast.makeText(
                        this@SosActivity,
                        "so'rov yuborildi, so'rov idsi ${response.body()?.id}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@SosActivity, response.errorBody()?.string(), Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Helper>, t: Throwable) {
                Toast.makeText(this@SosActivity, "api error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}