package com.msc.speaker_cleaner.component.language

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.nativead.NativeAd
import com.msc.speaker_cleaner.admob.NameRemoteAdmob
import com.msc.speaker_cleaner.base.activity.BaseActivity
import com.flash.light.component.language.LanguageAdapter
import com.msc.m_utils.external.Ex.visible
import com.msc.speaker_cleaner.component.onboarding.OnBoardingActivity
import com.msc.speaker_cleaner.R
import com.msc.speaker_cleaner.admob.NativeAdmob
import com.msc.speaker_cleaner.component.main.MainActivity
import com.msc.speaker_cleaner.databinding.ActivityLanguageBinding
import com.msc.speaker_cleaner.utils.AppEx.setAppLanguage
import com.msc.speaker_cleaner.utils.NativeAdmobUtils
import com.msc.speaker_cleaner.utils.NetworkUtil
import com.msc.speaker_cleaner.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private val viewModel: LanguageViewModel by viewModels()
    private val languageAdapter = LanguageAdapter()

    @Inject
    lateinit var spManager: SpManager

    override fun provideViewBinding(): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        val fromSplash = intent.getBooleanExtra(KEY_FROM_SPLASH, false)
        if (!fromSplash){
            viewBinding.imvBack.visibility = View.VISIBLE
        }else{
            viewBinding.imvBack.visibility = View.GONE
        }

        setStatusBarColor(ContextCompat.getColor(this, R.color.white), true)

        viewBinding.imvBack.setOnClickListener {
            finish()
        }

        viewBinding.rclLanguage.adapter = languageAdapter
        languageAdapter.onClick = {
            languageAdapter.selectLanguage(it.languageCode)
            showNativeS2()
            viewBinding.ivDone.visible()
        }
        viewBinding.ivDone.setOnClickListener {
            languageAdapter.selectedLanguage()?.let { languageModel ->
                spManager.saveLanguage(languageModel)
                setAppLanguage(languageModel.languageCode)
                val fromSplash = intent.getBooleanExtra(KEY_FROM_SPLASH, false)
                if (fromSplash) {
                    OnBoardingActivity.start(this)
                    finish()
                } else {
                    startActivity(Intent(this, MainActivity::class.java).also {
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
                }
            }
        }

        if(spManager.getShowOnBoarding()){
            viewBinding.ivDone.setText(R.string.txt_next)
        }

        if (isTempNativeAd != null) {
            isTempNativeAd = null
        }

        if (spManager.getShowOnBoarding() && NetworkUtil.isOnline) {
            if (spManager.getBoolean(NameRemoteAdmob.NATIVE_ONBOARD, true)) {
                NativeAdmobUtils.loadNativeOnboard()
            }
        }
    }

    private fun showNativeS2() {
        NativeAdmobUtils.languageNativeAdmob2?.run {
            nativeAdLive?.observe(this@LanguageActivity){
                if(available()){
                    showNativeAd(this, viewBinding.flAdplaceholder2.root)
                }
            }
        }
    }

    private fun showNativeAd(nativeAdmob: NativeAdmob, parent : ShimmerFrameLayout) {
        nativeAdmob.showNative(parent, null)
        parent.visible()
    }

    override fun initObserver() {
        viewModel.listLanguage.observe(this) {
            languageAdapter.setData(ArrayList(it))
//            spManager.getLanguage()?.let {
//                languageAdapter.selectLanguage(it.languageCode)
//            }
        }

        if(NativeAdmobUtils.languageNativeAdmob1 == null && spManager.getBoolean(NameRemoteAdmob.NATIVE_LANGUAGE, true)){
            NativeAdmobUtils.loadNativeLanguage()
        }

        NativeAdmobUtils.languageNativeAdmob1?.run {
            nativeAdLive?.observe(this@LanguageActivity){
                if(available()){
                    showNativeAd(this, viewBinding.flAdplaceholder1.root)
                }
            }
        }
    }

    override fun initData() {
        viewModel.loadListLanguage()
    }

    override fun onDestroy() {
        isTempNativeAd = null
        super.onDestroy()
    }

    companion object {
        var isTempNativeAd: NativeAd? = null
        const val KEY_FROM_SPLASH = "key_splash"
        fun start(context: Context, fromSplash: Boolean = false) {
            Intent(context, LanguageActivity::class.java).also {
                it.putExtra(KEY_FROM_SPLASH, fromSplash)
                context.startActivity(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        NativeAdmobUtils.languageNativeAdmob2?.reLoad()
    }
}