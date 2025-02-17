package com.msc.speaker_cleaner.utils

import android.content.Context
import java.util.Locale


object AppEx {
    fun Context.getDeviceLanguage(): String {
        return Locale.getDefault().language
    }

    fun Context.setAppLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun Int.range(min: Float, max: Float): Float {
        return (max - min) * this / 100 + min
    }

    fun Int.invertRange(min: Float, max: Float): Float {
        return (this + min) * 100 / (max - min)
    }
}