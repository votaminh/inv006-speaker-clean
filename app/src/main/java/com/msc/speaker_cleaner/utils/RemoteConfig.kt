package com.msc.speaker_cleaner.utils

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.ironsource.ro
import com.msc.speaker_cleaner.App
import com.msc.speaker_cleaner.BuildConfig

class RemoteConfig {

    private val TAG = "remoteConfig"
    var robotDevice : RobotDevice? = null

    companion object {
        private var mInstance : RemoteConfig? = null

        fun instance(): RemoteConfig {
            if(mInstance == null){
                mInstance = RemoteConfig()
            }
            return mInstance as RemoteConfig
        }

    }

    fun fetch(success : (() -> Unit)? = null) {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {

                }
                success?.invoke()
                updateConfig()
            }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                remoteConfig.activate().addOnCompleteListener {
                    updateConfig()
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {

            }
        })
    }

    private fun updateConfig() {

        val robotDeviceString: String = Firebase.remoteConfig.getString("robot_device_check")
        if (robotDeviceString.isEmpty()) {
            robotDevice = RobotDevice(0, false, ArrayList<String>(), ArrayList<String>())
        } else {
            robotDevice = Gson().fromJson(robotDeviceString, RobotDevice::class.java)
        }

        processRobotCheck()

        kotlin.runCatching {
            val remoteConfig = Firebase.remoteConfig
            putBooleanToSP(remoteConfig, "can_show_ads")
        }
    }

    private fun putBooleanToSP(remoteConfig: FirebaseRemoteConfig, name: String) {
        val spManager = App.instance?.applicationContext?.let { SpManager.getInstance(it) }
        val values = remoteConfig.getBoolean(name)
        spManager?.putBoolean(name, values)
        Log.i(TAG, "$name : $values")
    }

    private fun processRobotCheck() {
        robotDevice?.let{ robotDevice ->
            val brand1 = Build.BRAND
            val brand2 = Build.MANUFACTURER

            @SuppressLint("HardwareIds") val deviceID = Settings.Secure.getString(
                App.instance?.getContentResolver(),
                Settings.Secure.ANDROID_ID
            )

            if ((robotDevice.brands.contains(brand1) ||
                        robotDevice.brands.contains(brand2) ||
                        robotDevice.devices.contains(deviceID)) && !BuildConfig.DEBUG
            ) {
                val spManager = App.instance?.applicationContext?.let { SpManager.getInstance(it) }
                spManager?.putBoolean("can_show_ads", false)
            }
        }
    }
}