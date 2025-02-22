package com.msc.speaker_cleaner.component.main

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.msc.m_utils.external.BaseAdmob
import com.msc.speaker_cleaner.BuildConfig
import com.msc.speaker_cleaner.R
import com.msc.speaker_cleaner.admob.BannerAdmob
import com.msc.speaker_cleaner.admob.CollapsiblePositionType
import com.msc.speaker_cleaner.admob.InterAdmob
import com.msc.speaker_cleaner.admob.NameRemoteAdmob
import com.msc.speaker_cleaner.admob.RewardAdmob
import com.msc.speaker_cleaner.base.activity.BaseActivity
import com.msc.speaker_cleaner.component.setting.SettingActivity
import com.msc.speaker_cleaner.databinding.ActivityMainBinding
import com.msc.speaker_cleaner.interfaces.OnTabChangeListener
import com.msc.speaker_cleaner.utils.SpManager
import com.msc.speaker_cleaner.utils.UtilRate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), OnTabChangeListener {
    private lateinit var vpMainAdapter: VPMainAdapter
    private lateinit var buttons: List<AppCompatButton>
    private var prePosition = -1
    private var latestInterShow: Long = 0
    private var firstRequest = true
    private var interAdmob : InterAdmob? = null
    private var rewardAdmob : RewardAdmob? = null

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        vpMainAdapter = VPMainAdapter(this)
        viewBinding.apply {
            buttons = listOf(btnHome, btnAutoCleaner, btnManualCleaner, btnVibrateCleaner)

            vpMain.adapter = vpMainAdapter

            vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateButtonStates(position)

                    if(position == prePosition){
                        return
                    }

                    prePosition = position

                    stopAll()

                    when (position) {
                        0 -> {
                            tvTitleMain.text = getText(R.string.speaker_cleaner)
                            btnSetting.visibility = View.VISIBLE
                        }

                        1 -> {
                            tvTitleMain.text = getText(R.string.auto_cleaner)
                            btnSetting.visibility = View.GONE
                        }

                        2 -> {
                            tvTitleMain.text = getText(R.string.manual_cleaner)
                            btnSetting.visibility = View.GONE
                        }

                        3 -> {
                            tvTitleMain.text = getText(R.string.vibrate_cleaner)
                            btnSetting.visibility = View.GONE
                        }
                    }
                }
            })

            buttons.forEachIndexed { index, button ->
                button.setOnClickListener {
                    vpMain.currentItem = index
                }
            }

            btnSetting.setOnClickListener { SettingActivity.start(this@MainActivity) }
        }

        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(!UtilRate.showDialogRate(this@MainActivity)){
                    finish()
                }
            }
        })

        loadInter()
        loadReward()
        showBanner()
    }

    private fun loadReward() {
        if(SpManager.getInstance(this).getBoolean(NameRemoteAdmob.REWARD_FEATURE, true)){
            rewardAdmob = RewardAdmob(this, BuildConfig.reward_feature)
            rewardAdmob?.load(null)
        }
    }

    fun showReward(nextAction: (() -> Unit)?){
        if(SpManager.getInstance(this).getBoolean(NameRemoteAdmob.REWARD_FEATURE, true) && rewardAdmob != null){
            rewardAdmob?.show(this@MainActivity, object : BaseAdmob.OnAdmobShowListener{
                override fun onShow() {
                    nextAction?.invoke()
                    rewardAdmob?.load(null)
                }

                override fun onError(e: String?) {
                    nextAction?.invoke()
                    rewardAdmob?.load(null)
                }
            })
        }else{
            nextAction?.invoke()
            rewardAdmob?.load(null)
        }
    }

    private fun showBanner() {
        if(SpManager.getInstance(this).getBoolean(NameRemoteAdmob.BANNER_COLAPSE, true)){
            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.BOTTOM)
            bannerAdmob.showBanner(this@MainActivity, BuildConfig.banner_collap, viewBinding.banner)
        }else{
            viewBinding.banner.visibility = View.GONE
        }
    }

    private fun loadInter() {
        if(SpManager.getInstance(this@MainActivity).getBoolean(NameRemoteAdmob.INTER_HOME, true)){
            interAdmob = InterAdmob(this, BuildConfig.inter_home)
            interAdmob?.load(null)
        }
    }

    fun showInter(nextAction : (() -> Unit)? = null){
        if(firstRequest){
            firstRequest = false
            nextAction?.invoke()
            return
        }

        if(latestInterShow == 0L){
            latestInterShow = System.currentTimeMillis()
        }else if(System.currentTimeMillis() - latestInterShow < 30000){
            nextAction?.invoke()
            return
        }

        latestInterShow = System.currentTimeMillis()

        if(interAdmob == null){
            nextAction?.invoke()
        }else{
            interAdmob?.showInterstitial(this, object : BaseAdmob.OnAdmobShowListener{
                override fun onShow() {
                    nextAction?.invoke()
                    loadInter()
                }

                override fun onError(e: String?) {
                    nextAction?.invoke()
                    loadInter()
                }
            })
        }
    }

    private fun stopAll() {
        vpMainAdapter.listFragment.forEach {
            it.onResume()
        }
    }

    private fun updateButtonStates(position: Int) {
        buttons.forEachIndexed { index, button ->
            val drawable = button.compoundDrawablesRelative[1]
            if (index == position) {
                button.setTextColor(getColor(R.color.blue_main_006))
                drawable.setTint(ContextCompat.getColor(this, R.color.blue_main_006))
            } else {
                button.setTextColor(getColor(R.color.text1))
                drawable.setTint(ContextCompat.getColor(this, R.color.text1))
            }
        }
    }

    override fun onBack() {
        if(!UtilRate.showDialogRate(this@MainActivity)){
            finish()
        }
    }

    override fun onTabChange(index: Int) {
        viewBinding.vpMain.currentItem = index
    }

    override fun onResume() {
        super.onResume()

        vpMainAdapter.listFragment.forEach {
            it.onResume()
        }
    }
}