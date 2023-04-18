package com.conamobile.sosapp.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.conamobile.sosapp.R
import com.conamobile.sosapp.pref.SharedPreferences
import com.conamobile.sosapp.util.isLocationEnabled
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.permissionx.guolindev.PermissionX

class SplashActivity : AppCompatActivity() {
    private val sharedPreferences by lazy {
        SharedPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList, "Core fundamental are based on these permissions", "OK", "Cancel"
                )
            }.request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    if (!isLocationEnabled()) showLocationOn()
                    else checkInternetEnabled()
                } else {
                    Toast.makeText(this, "Please turn on the permission", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                    finish()
                }
            }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                return true else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            ) return true else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) return true
        }
        return false
    }

    private fun checkInternetEnabled() {
        if (isOnline(this)) {
            startActivity(
                Intent(
                    this,
                    if (sharedPreferences.isLogin()) MainActivity::class.java else LoginActivity::class.java
                )
            )
            finish()
        } else {
            Toast.makeText(
                this,
                "Iltmos internet yoki wifini yoqib qaytadan kiring!",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            finish()
        }
    }

    private fun showLocationOn() {
        val locationRequest = LocationRequest.create()
        locationRequest.apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 30 * 1000.toLong()
            fastestInterval = 5 * 1000.toLong()
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
                if (response.locationSettingsStates!!.isGpsPresent) Log.d("@@@", "ERROR")
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(e.status.resolution!!).build()
                        launcher.launch(intentSenderRequest)
                    } catch (e: IntentSender.SendIntentException) {
                    }
                }
            }
        }
    }

    private var launcher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                checkInternetEnabled()
            } else {
                Toast.makeText(this, "Iltmos joylashuvni yoqib qaytadan kiring", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                finish()
            }
        }
}