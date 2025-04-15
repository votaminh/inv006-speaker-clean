package com.msc.speaker_cleaner.utils

import com.google.gson.annotations.SerializedName

data class RobotDevice(
    @SerializedName("version_check_device")
    val version : Int,
    @SerializedName("tracking_device")
    val trackingDevice : Boolean,
    @SerializedName("devices")
    val devices : List<String>,
    @SerializedName("brands")
    val brands : List<String>
)