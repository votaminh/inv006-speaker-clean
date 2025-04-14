package com.msc.speaker_cleaner.component.ump

import com.msc.m_utils.external.UMPUtils
import com.msc.speaker_cleaner.base.activity.BaseActivity
import com.msc.speaker_cleaner.component.splash.SplashActivity
import com.msc.speaker_cleaner.App
import com.msc.speaker_cleaner.databinding.ActivitySplashBinding
import com.msc.speaker_cleaner.databinding.ActivityUmpBinding
import com.msc.speaker_cleaner.utils.RemoteConfig
import com.msc.speaker_cleaner.utils.SpManager


class UMPActivity : BaseActivity<ActivitySplashBinding>() {
    private val TAG = "ump_activity"

    override fun provideViewBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun initData() {
        super.initData()

        if(SpManager.getInstance(this).isUMPShowed()){
            RemoteConfig.instance().fetch()
            openSplash();
        }else{
            RemoteConfig.instance().fetch{
                initUmp()
            }
        }
    }

    private fun openSplash() {

        val app : App = application as App
        app.initAds()

        SpManager.getInstance(this).setUMPShowed(true)
        SplashActivity.start(this);
        finish()
    }

    private fun initUmp() {
        UMPUtils.init(this@UMPActivity, nextAction = {
            openSplash()
        }, true, false)
    }
}