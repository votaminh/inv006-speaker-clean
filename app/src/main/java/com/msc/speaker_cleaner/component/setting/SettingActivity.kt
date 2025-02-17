package com.msc.speaker_cleaner.component.setting

import android.app.Activity
import android.content.Intent
import com.msc.m_utils.external.Ex.openAppInStore
import com.msc.m_utils.external.Ex.showPolicyApp
import com.msc.speaker_cleaner.base.activity.BaseActivity
import com.msc.speaker_cleaner.component.language.LanguageActivity
import com.msc.speaker_cleaner.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SettingActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        viewBinding.apply {
            topSetting
            btnLanguage.setOnClickListener {
                startActivity(
                    Intent(
                        this@SettingActivity,
                        LanguageActivity::class.java
                    )
                )
            }
            btnSetting.setOnClickListener {
                finish()
            }
            btnPrivacyPolicy.setOnClickListener { showPolicyApp("https://sites.google.com/view/speaker-cleaner1/trang-ch%E1%BB%A7") }
            btnRateUs.setOnClickListener { openAppInStore() }
        }
    }

}