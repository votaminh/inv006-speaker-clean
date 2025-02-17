package com.msc.speaker_cleaner.component.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.util.Log
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.viewpager2.widget.ViewPager2
import com.flash.light.base.fragment.BaseFragment
import com.msc.speaker_cleaner.R
import com.msc.speaker_cleaner.component.blower.BlowerActivity
import com.msc.speaker_cleaner.component.main.MainActivity
import com.msc.speaker_cleaner.databinding.FragmentHomeBinding
import com.msc.speaker_cleaner.domain.layer.BannerModel
import com.msc.speaker_cleaner.interfaces.OnTabChangeListener
import com.msc.speaker_cleaner.utils.Constant.CURRENT_VOLUME
import java.util.Timer
import kotlin.concurrent.schedule


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private var vpAdapter = ViewPagerAdapter()
    private lateinit var audioManager: AudioManager
    private var onTabChangeListener: OnTabChangeListener? = null
    private var volumeReceiver : BroadcastReceiver? = null

    override fun provideViewBinding(container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTabChangeListener) {
            onTabChangeListener = context
        } else {
            throw RuntimeException("$context must implement OnTabChangeListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onTabChangeListener = null
    }

    override fun initViews() {
        super.initViews()

        audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        vpAdapter.setData(
            listOf(
                BannerModel(
                    R.mipmap.img_banner_home,
                    R.string.txt_banner_1
                ),
                BannerModel(
                    R.mipmap.img_banner_home2,
                    R.string.txt_banner_2
                )
            )
        )

        viewBinding.apply {
            vpBannerHome.adapter = vpAdapter

            vpBannerHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Timer().schedule(3000) {
                        if (position == 1) {
                            vpBannerHome.currentItem = 0
                        } else {
                            vpBannerHome.currentItem = position + 1
                        }
                    }
                }
            })

            sbVolume.max = maxVolume
            sbVolume.progress = currentVolume
            CURRENT_VOLUME = currentVolume

            sbVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                        CURRENT_VOLUME = progress
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            btnAutoClean.setOnClickListener {
                activity?.let {
                    (activity as MainActivity).showInter{
                        onTabChangeListener?.onTabChange(1)
                    }
                }
            }
            btnManualClean.setOnClickListener {
                activity?.let {
                    (activity as MainActivity).showInter{
                        onTabChangeListener?.onTabChange(2)
                    }
                }
            }
            btnVibrateClean.setOnClickListener {
                activity?.let {
                    (activity as MainActivity).showInter{
                        onTabChangeListener?.onTabChange(3)
                    }
                }
            }
            btnBlower.setOnClickListener {
                activity?.let {
                    (activity as MainActivity).showInter{
                        BlowerActivity.start(it)
                    }
                }
            }
        }
    }

    private fun registerVolumeReceiver() {
        volumeReceiver = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                updateSbVolume()
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        activity?.registerReceiver(volumeReceiver, intentFilter)
    }

    private fun updateSbVolume() {
        kotlin.runCatching {
            val audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            viewBinding.sbVolume.progress = currentVolume
        }
    }

    override fun onResume() {
        super.onResume()

        kotlin.runCatching {
            registerVolumeReceiver()
            updateSbVolume()
        }
    }

    override fun onPause() {
        activity?.unregisterReceiver(volumeReceiver)
        super.onPause()
    }
}