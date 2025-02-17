package com.msc.speaker_cleaner.component.permission

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.msc.m_utils.external.Ex.invisible
import com.msc.speaker_cleaner.admob.NameRemoteAdmob
import com.msc.speaker_cleaner.base.activity.BaseActivity
import com.msc.speaker_cleaner.component.main.MainActivity
import com.msc.speaker_cleaner.databinding.ActivityPermissonBinding
import com.msc.speaker_cleaner.utils.NativeAdmobUtils
import com.msc.speaker_cleaner.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionActivity : BaseActivity<ActivityPermissonBinding>() {

    @Inject
    lateinit var spManager: SpManager

    private val stateWriteSetting = MutableLiveData(false)
    private val stateNotification = MutableLiveData(false)
    private val stateMedia = MutableLiveData(false)

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, PermissionActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityPermissonBinding {
        return ActivityPermissonBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        viewBinding.run {

            toolbar.imvBack.invisible()

            llWriteSetting.setOnClickListener {
//                PermissionUtils.requestWriteSetting(this@PermissionActivity, 342)
                sw1.isChecked = !sw1.isChecked
                checkState()
            }
            llNotification.setOnClickListener {
//                PermissionUtils.requestNotificationPermission(this@PermissionActivity, 533)
                sw3.isChecked = !sw3.isChecked
                checkState()
            }
            llReadMedia.setOnClickListener {
//                PermissionUtils.requestStorage(this@PermissionActivity, 522)
                sw2.isChecked = !sw2.isChecked
                checkState()
            }

            tvNext.setOnClickListener {
                MainActivity.start(this@PermissionActivity)
                finish()
            }
        }
        checkState()
    }

    private fun checkState() {
        viewBinding.run {
            if(sw1.isChecked || sw2.isChecked || sw3.isChecked){
                viewBinding.tvNext.visibility = View.VISIBLE
            }else{
                viewBinding.tvNext.invisible()
            }
        }
    }

    override fun initObserver() {
        super.initObserver()

        stateMedia.observe(this){
            viewBinding.llReadMedia.visibility = if(it) View.GONE else View.VISIBLE
            checkShowNextBtn()
        }
        stateWriteSetting.observe(this){
            viewBinding.llWriteSetting.visibility = if(it) View.GONE else View.VISIBLE
            checkShowNextBtn()
        }
        stateNotification.observe(this){
            viewBinding.llNotification.visibility = if(it) View.GONE else View.VISIBLE
            checkShowNextBtn()
        }

        if(!spManager.getBoolean(NameRemoteAdmob.NATIVE_FEATURE, true)){
            viewBinding.flAdplaceholder.visibility = View.GONE
        }

        NativeAdmobUtils.permissionNativeAdmob?.run {
            nativeAdLive.observe(this@PermissionActivity){
                if(available() && spManager.getBoolean(NameRemoteAdmob.NATIVE_FEATURE, true)){
                    showNative(viewBinding.flAdplaceholder, null)
                }else{
                    viewBinding.flAdplaceholder.visibility = View.GONE
                }
            }
        }
    }

    private fun checkShowNextBtn() {
//        if(stateMedia.value == true && stateNotification.value == true && stateWriteSetting.value == true){
//            viewBinding.tvNext.visibility = View.VISIBLE
//        }else{
//            viewBinding.tvNext.visibility = View.INVISIBLE
//        }
    }

    override fun onResume() {
        NativeAdmobUtils.permissionNativeAdmob?.reLoad()
        super.onResume()
        checkState()
    }
}