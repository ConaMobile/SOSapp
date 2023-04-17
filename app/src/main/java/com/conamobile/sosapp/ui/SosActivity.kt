package com.conamobile.sosapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.conamobile.sosapp.databinding.ActivitySosBinding

class SosActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySosBinding

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
            binding.latLng.text = "latitude: $lat\nlongitude: $lng"
            Toast.makeText(this, "Your location lat and lng: $lat, $lng ", Toast.LENGTH_LONG).show()
        } else Toast.makeText(
            this,
            "Please enable location and location permission",
            Toast.LENGTH_LONG
        ).show()
    }
}