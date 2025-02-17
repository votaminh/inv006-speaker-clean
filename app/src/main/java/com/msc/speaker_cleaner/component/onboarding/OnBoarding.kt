package com.msc.speaker_cleaner.component.onboarding

import com.msc.speaker_cleaner.admob.NativeAdmob


data class OnBoarding(val resImage: Int, val resDescription: Int, var nativeAdmob : NativeAdmob? = null){
    companion object {
        const val FULL_NATIVE_FLAG = 1822
    }
}
