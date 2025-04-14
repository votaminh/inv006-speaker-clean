package com.msc.speaker_cleaner

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.applovin.sdk.AppLovinPrivacySettings
import com.google.ads.mediation.inmobi.InMobiConsent
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.FirebaseApp
import com.inmobi.sdk.InMobiSdk
import com.msc.speaker_cleaner.admob.OpenAdmob
import com.msc.speaker_cleaner.utils.AppEx.getDeviceLanguage
import com.msc.speaker_cleaner.utils.LocaleHelper
import com.msc.speaker_cleaner.utils.NetworkUtil
import com.msc.speaker_cleaner.utils.RemoteConfig
import com.msc.speaker_cleaner.utils.SpManager
import dagger.hilt.android.HiltAndroidApp
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import com.ironsource.mediationsdk.IronSource;
import com.mbridge.msdk.MBridgeConstans
import com.mbridge.msdk.out.MBridgeSDKFactory
import com.vungle.ads.VunglePrivacySettings

@HiltAndroidApp
@Singleton
class App : Application(), Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {
    companion object {
        var instance: App? = null
    }

    @Inject
    lateinit var spManager: SpManager

    private var currentActivity: Activity? = null
    private var openAdmob: OpenAdmob? = null

    override fun onCreate() {
        super<Application>.onCreate()
        instance = this

        FirebaseApp.initializeApp(this)
        RemoteConfig.instance().fetch()
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        NetworkUtil.initNetwork(this)
    }

    fun initAds() {
        MobileAds.initialize(this)
        val requestConfiguration = RequestConfiguration.Builder().build()
        MobileAds.setRequestConfiguration(requestConfiguration)

        if(spManager.canShowAds()){
            openAdmob = OpenAdmob(this, BuildConfig.open_resume)
        }

        initMediation()
    }

    private fun initMediation() {
        initPangle()
        initVungle()
        initApplovin()
        initFAN()
        initMintegral()
        initInMobi()
        initIronSource()
    }

    private fun initFAN() {
        // no request code
    }

    private fun initVungle() {
        VunglePrivacySettings.setGDPRStatus(true, "v1.0.0");
    }

    private fun initPangle() {
        // no request code
    }

    private fun initApplovin(){
        AppLovinPrivacySettings.setDoNotSell(true, this);
        VunglePrivacySettings.setCCPAStatus(true);
    }

    private fun initInMobi(){
        val consentObject = JSONObject()
        try {
            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true)
            consentObject.put("gdpr", "1")
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }

        InMobiConsent.updateGDPRConsent(consentObject)
    }

    private fun initMintegral(){
        val sdk = MBridgeSDKFactory.getMBridgeSDK()
        sdk.setConsentStatus(this, MBridgeConstans.IS_SWITCH_ON)
    }

    private fun initIronSource(){
        IronSource.setConsent(true);
        IronSource.setMetaData("do_not_sell", "true")
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        val locale = getDeviceLanguage()
        val language = spManager.getLanguage()
        language?.let {
            LocaleHelper.onAttach(this, language.languageCode)
            super.onConfigurationChanged(newConfig)
        }
    }

    override fun attachBaseContext(base: Context?) {
        val context = LocaleHelper.onAttach(base, Locale.getDefault().language)
        super.attachBaseContext(context)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if(spManager.canShowAds()){
            openAdmob?.run {
                currentActivity?.let { showAdIfAvailable(it) }
            }
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}