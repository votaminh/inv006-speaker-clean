package com.msc.speaker_cleaner.component.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.msc.m_utils.external.Ex.openAppInStore
import com.msc.m_utils.external.Ex.showPolicyApp
import com.msc.speaker_cleaner.R
import com.msc.speaker_cleaner.base.activity.BaseActivity
import com.msc.speaker_cleaner.component.language.LanguageActivity
import com.msc.speaker_cleaner.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, SettingActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        viewBinding.run {
            imvBack.setOnClickListener {
                finish()
            }
            llLanguage.setOnClickListener {
                LanguageActivity.start(this@SettingActivity)
            }
            llRate.setOnClickListener {
                openGooglePlayForRating()
            }
            llShare.setOnClickListener {
                shareApp()
            }
            llPrivacy.setOnClickListener {
                openWebPage("")
            }
        }
    }

    private fun openGooglePlayForRating() {
        val packageName = packageName
        try {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun shareApp() {
        val packageName = packageName
        val shareText = "https://play.google.com/store/apps/details?id=$packageName"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Share app")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Share app"))
    }

    private fun openWebPage(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}