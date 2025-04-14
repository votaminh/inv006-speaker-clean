package com.msc.speaker_cleaner.component.splash

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import com.msc.m_utils.external.BaseAdmob
import com.msc.m_utils.external.Ex.gone
import com.msc.speaker_cleaner.BuildConfig
import com.msc.speaker_cleaner.admob.BannerAdmob
import com.msc.speaker_cleaner.admob.CollapsiblePositionType
import com.msc.speaker_cleaner.admob.InterAdmob
import com.msc.speaker_cleaner.base.activity.BaseActivity
import com.msc.speaker_cleaner.component.language.LanguageActivity
import com.msc.speaker_cleaner.component.main.MainActivity
import com.msc.speaker_cleaner.databinding.ActivitySplashBinding
import com.msc.speaker_cleaner.utils.NativeAdmobUtils
import com.msc.speaker_cleaner.utils.NetworkUtil
import com.msc.speaker_cleaner.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var progressAnimator: ValueAnimator? = null

    @Inject
    lateinit var spManager: SpManager

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SplashActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun onDestroy() {
        cancelLoadingListener()
        super.onDestroy()
    }

    private fun cancelLoadingListener() {
        progressAnimator?.removeAllListeners()
        progressAnimator?.cancel()
        progressAnimator = null
    }

    override fun onResume() {
        super.onResume()
        if (progressAnimator?.isPaused == true) {
            progressAnimator?.resume()
        }
    }

    override fun onPause() {
        progressAnimator?.pause()
        super.onPause()
    }

    override fun initViews() {
        if (spManager.getShowOnBoarding() && NetworkUtil.isOnline) {
            if (spManager.canShowAds()) {
                NativeAdmobUtils.loadNativeLanguage()
            }
        }

        runProgress()
    }

    private fun runProgress() {
        showBanner()
        if (spManager.canShowAds()) {
            loadShowOpenAds(successAction = {
                gotoMainScreen()
            }, failAction = {
                loadShowInter(successAction = {
                    gotoMainScreen()
                }, failAction = {
                    gotoMainScreen()
                })
            })
        } else {
            gotoMainScreen()
        }
    }

    private fun loadShowInter(successAction : (() -> Unit)? = null, failAction : (() -> Unit)? = null) {
        val interAdmob = InterAdmob(this@SplashActivity, BuildConfig.inter_splash)
        interAdmob.load(object : BaseAdmob.OnAdmobLoadListener {
            override fun onLoad() {
                interAdmob.showInterstitial(
                    this@SplashActivity,
                    object : BaseAdmob.OnAdmobShowListener {
                        override fun onShow() {
                            successAction?.invoke()
                        }

                        override fun onError(e: String?) {
                            failAction?.invoke()
                        }
                    })
            }

            override fun onError(e: String?) {
                failAction?.invoke()
            }
        })
    }

    private fun loadShowOpenAds(successAction : (() -> Unit)? = null, failAction : (() -> Unit)? = null) {
        val interAdmob = InterAdmob(this, BuildConfig.inter_splash_high)
        interAdmob.load(object : BaseAdmob.OnAdmobLoadListener {
            override fun onLoad() {
                interAdmob.showInterstitial(this@SplashActivity, object :
                    BaseAdmob.OnAdmobShowListener {
                    override fun onShow() {
                        successAction?.invoke()
                    }

                    override fun onError(e: String?) {
                        failAction?.invoke()
                    }

                })
            }

            override fun onError(e: String?) {
                failAction?.invoke()
            }
        })
    }

    private fun showBanner() {
        if(spManager.canShowAds()){
            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.NONE)
            bannerAdmob.showBanner(this@SplashActivity, BuildConfig.banner_splash, viewBinding.banner)
        }else{
            viewBinding.banner.gone()
        }
    }

    private fun gotoMainScreen() {
        cancelLoadingListener()
        if (spManager.getShowOnBoarding()) {
            LanguageActivity.start(this, true)
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}