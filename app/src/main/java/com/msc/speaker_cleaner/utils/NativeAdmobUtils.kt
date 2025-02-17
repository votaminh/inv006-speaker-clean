package com.msc.speaker_cleaner.utils

import android.annotation.SuppressLint
import com.msc.m_utils.external.BaseAdmob
import com.msc.speaker_cleaner.App
import com.msc.speaker_cleaner.BuildConfig
import com.msc.speaker_cleaner.admob.NameRemoteAdmob
import com.msc.speaker_cleaner.admob.NativeAdmob

class NativeAdmobUtils {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var languageNativeAdmob1: NativeAdmob? = null
        @SuppressLint("StaticFieldLeak")
        var languageNativeAdmob2: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var onboardNativeAdmob1: NativeAdmob? = null
        @SuppressLint("StaticFieldLeak")
        var onboardNativeAdmob2: NativeAdmob? = null
        @SuppressLint("StaticFieldLeak")
        var onboardNativeAdmob3: NativeAdmob? = null
        @SuppressLint("StaticFieldLeak")
        var onboardFullNativeAdmob : NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var permissionNativeAdmob: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var nativeExitLiveData: NativeAdmob? = null

        fun loadNativeLanguage() {
            if(NetworkUtil.isOnline){
                App.instance?.applicationContext?.let { context ->

                    val isFirstLanguage = SpManager.getInstance(context).getBoolean("isFirstLanguage", true)
                    if(isFirstLanguage){
                        SpManager.getInstance(context).putBoolean("isFirstLanguage", false)
                        languageNativeAdmob1 = NativeAdmob(context, BuildConfig.native_language_1_1)
                        languageNativeAdmob1?.load(null)

                        languageNativeAdmob2 = NativeAdmob(context, BuildConfig.native_language_1_2)
                        languageNativeAdmob2?.load(null)
                    }else {
                        languageNativeAdmob1 = NativeAdmob(context, BuildConfig.native_language_2_1)
                        languageNativeAdmob1?.load(null)

                        languageNativeAdmob2 = NativeAdmob(context, BuildConfig.native_language_2_2)
                        languageNativeAdmob2?.load(null)
                    }

                }
            }
        }
        fun loadNativePermission() {
            App.instance?.applicationContext?.let { context ->
                if(SpManager.getInstance(context).getBoolean(NameRemoteAdmob.NATIVE_FEATURE, true)){
                    permissionNativeAdmob = NativeAdmob(
                        context,
                        BuildConfig.native_feature
                    )
                    permissionNativeAdmob?.load(null)
                }
            }
        }

        fun loadNativeOnboard() {
            if(NetworkUtil.isOnline){
                App.instance?.applicationContext?.let {context ->

                    val isFirstOnboarding  = SpManager.getInstance(context).getBoolean("isFirstOnboarding", true)
                    if(isFirstOnboarding){
                        SpManager.getInstance(context).putBoolean("isFirstOnboarding", false)
                        onboardNativeAdmob1 = NativeAdmob(context, BuildConfig.native_onboarding)
                        onboardNativeAdmob1?.load(null)

                        onboardNativeAdmob2 = NativeAdmob(context, BuildConfig.native_onboarding2)
                        onboardNativeAdmob2?.load(null)

                        onboardNativeAdmob3 = NativeAdmob(context, BuildConfig.native_onboarding3)
                        onboardNativeAdmob3?.load(null)
                    }else{
                        onboardNativeAdmob1 = NativeAdmob(context, BuildConfig.native_onboarding_2_1)
                        onboardNativeAdmob1?.load(null)


                        onboardNativeAdmob3 = NativeAdmob(context, BuildConfig.native_onboarding_2_3)
                        onboardNativeAdmob3?.load(null)
                    }

                    onboardFullNativeAdmob = NativeAdmob(context, BuildConfig.native_full_screen2)
                    onboardFullNativeAdmob?.load(object : BaseAdmob.OnAdmobLoadListener {
                        override fun onLoad() {
                        }

                        override fun onError(e: String?) {
                            onboardFullNativeAdmob = NativeAdmob(context, BuildConfig.native_full_screen)
                            onboardFullNativeAdmob?.load(null)
                        }
                    })
                }
            }
        }

        fun loadNativeExit(){
//            App.instance?.applicationContext?.let {context ->
//                nativeExitLiveData = NativeAdmob(
//                    context,
//                    BuildConfig.native_exit
//                )
//                nativeExitLiveData?.load(null)
//            }
        }
    }
}