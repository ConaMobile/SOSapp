package com.conamobile.sosapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.conamobile.sosapp.util.PhoneData
import com.conamobile.sosapp.R
import com.conamobile.sosapp.constants.RequestFor
import com.conamobile.sosapp.constants.KeyConstant
import com.conamobile.sosapp.databinding.ActivityMainBinding
import com.conamobile.sosapp.util.DeviceAdminUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            PhoneData.savePhoneData(this@MainActivity, KeyConstant.UNLOCK_STR, isChecked)

            val text =
                if (isChecked) getString(R.string.enabled_str) else getString(R.string.disabled_str)
            val color: Int = if (isChecked) ContextCompat.getColor(
                this@MainActivity, R.color.green
            ) else ContextCompat.getColor(this@MainActivity, R.color.red)
            binding.statusText.text = text
            binding.statusText.setTextColor(color)
        }

        binding.volumeSwitch.setOnCheckedChangeListener { _, isChecked ->
            PhoneData.savePhoneData(
                this@MainActivity,
                KeyConstant.VOLUME_LOCK_ENABLE_STR,
                binding.volumeSwitch.isChecked && DeviceAdminUtil.checkisDeviceAdminEnabled()
            )

            if (binding.volumeSwitch.isChecked && !DeviceAdminUtil.checkisDeviceAdminEnabled()) {
                DeviceAdminUtil.openDeviceManagerEnableAction(
                    this@MainActivity, RequestFor.REQUEST_VOLUME_ENABLE
                )
            }
        }
        renderView(this)
    }

    private fun renderView(context: Context) {
        val isChecked = PhoneData.getPhoneData(this, KeyConstant.UNLOCK_STR, false)
        val text =
            if (isChecked) getString(R.string.enabled_str) else getString(R.string.disabled_str)
        val color =
            if (isChecked) ContextCompat.getColor(this, R.color.green) else ContextCompat.getColor(
                this, R.color.red
            )
        binding.apply {
            checkBox.isChecked = isChecked
            statusText.text = text
            statusText.setTextColor(color)
            val isKeyEnabled =
                PhoneData.getPhoneData(context, KeyConstant.VOLUME_LOCK_ENABLE_STR, false)
            val isFloatingEnabed =
                PhoneData.getPhoneData(context, KeyConstant.FLOATING_LOCK_STR, false)
            volumeSwitch.isChecked = isKeyEnabled && DeviceAdminUtil.checkisDeviceAdminEnabled()
            if (isFloatingEnabed) {
                checkDrawOverlayPermission()
            }
        }
    }


    private fun checkDrawOverlayPermission(): Boolean {
        var isPermission = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                        "package:$packageName"
                    )
                )
                startActivityForResult(intent, RequestFor.REQUEST_FLOATING_PERMISSION)
                isPermission = false
            }
        }
        return isPermission
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestFor.REQUEST_VOLUME_ENABLE) {
            binding.volumeSwitch.isChecked = DeviceAdminUtil.checkisDeviceAdminEnabled()
            PhoneData.savePhoneData(
                this,
                KeyConstant.VOLUME_LOCK_ENABLE_STR,
                DeviceAdminUtil.checkisDeviceAdminEnabled()
            )
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        renderView(this)
    }

}