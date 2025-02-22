package com.msc.speaker_cleaner.component.cleanervibrate

import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.flash.light.base.fragment.BaseFragment
import com.msc.speaker_cleaner.R
import com.msc.speaker_cleaner.component.main.MainActivity
import com.msc.speaker_cleaner.component.testspeaker.activity.TestSpeakerActivity
import com.msc.speaker_cleaner.databinding.FragmentVibrateCleanerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VibrateCleanerFragment : BaseFragment<FragmentVibrateCleanerBinding>() {
    val viewModel: VibrateViewModel by viewModels()
    var timeReward = 0L
    override fun provideViewBinding(container: ViewGroup?): FragmentVibrateCleanerBinding {
        return FragmentVibrateCleanerBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        lifecycle.addObserver(viewModel)

        viewBinding.apply {
            btnPlayAndPause.setOnClickListener {
                if(viewModel.isRunning()){
                    viewModel.cancelVibrate()
                }else{

                    (activity as MainActivity).run {
                        showReward {
                            timeReward = System.currentTimeMillis()
                            runOnUiThread {
                                viewModel.start()
                            }
                        }
                    }
                }
            }
            btnNormal.setOnClickListener { viewModel.setInensity(IntensityVibrate.NORMAL) }
            btnStrong.setOnClickListener { viewModel.setInensity(IntensityVibrate.STRONG) }
        }
    }

    override fun initObserver() {
        super.initObserver()

        viewModel.run {
            stateLive.observe(requireActivity()) {
                when (it) {
                    StateVibrate.PLAYING -> {
                        viewBinding.imvPlayAndPause.setImageResource(R.drawable.ic_pause006)
                    }

                    StateVibrate.STOP -> {
                        viewBinding.imvPlayAndPause.setImageResource(R.drawable.ic_play006)
                    }
                }
            }

            percentLive.observe(requireActivity()) {
                viewBinding.tvPercent.text = "$it %"
                if (it == 100) {
                    TestSpeakerActivity.start(requireActivity())
                }
            }

            intensityLive.observe(requireActivity()) {
                when (it) {
                    IntensityVibrate.NORMAL -> {
                        viewBinding.btnNormal.setBackgroundResource(R.drawable.bg_sellect_button006)
                        viewBinding.btnStrong.setBackgroundResource(R.color.transparent)
                        viewBinding.btnNormal.setTextColor(requireActivity().getColor(R.color.black))
                        viewBinding.btnStrong.setTextColor(requireActivity().getColor(R.color.white))
                    }

                    IntensityVibrate.STRONG -> {
                        viewBinding.btnNormal.setBackgroundResource(R.color.transparent)
                        viewBinding.btnStrong.setBackgroundResource(R.drawable.bg_sellect_button006)
                        viewBinding.btnNormal.setTextColor(requireActivity().getColor(R.color.white))
                        viewBinding.btnStrong.setTextColor(requireActivity().getColor(R.color.black))
                    }
                }
            }
        }
    }
}