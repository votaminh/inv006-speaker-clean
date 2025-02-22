package com.msc.speaker_cleaner.component.cleanerauto

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.flash.light.base.fragment.BaseFragment
import com.msc.speaker_cleaner.R
import com.msc.speaker_cleaner.component.main.MainActivity
import com.msc.speaker_cleaner.component.testspeaker.activity.TestSpeakerActivity
import com.msc.speaker_cleaner.databinding.FragmentAutoCleanerBinding
import com.msc.speaker_cleaner.domain.layer.SourceAudio
import com.msc.speaker_cleaner.domain.layer.StateAudio
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AutoCleanerFragment : BaseFragment<FragmentAutoCleanerBinding>() {
    private val viewModel: AutoViewModel by viewModels()

    var timeReward = 0L

    override fun provideViewBinding(container: ViewGroup?): FragmentAutoCleanerBinding {
        return FragmentAutoCleanerBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        viewBinding.run {
            btnPlayAndPause.setOnClickListener {
                if(viewModel.isAudioPlaying()){
                    viewModel.stopAudio()
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

            btnFront.setOnClickListener {
                viewModel.useSpeaker()
            }

            btnEar.setOnClickListener {
                viewModel.useEarpiece()
            }


            btnFront.isSelected = true
        }
    }

    override fun initObserver() {
        super.initObserver()

        viewModel.run {
            stateAudio.observe(requireActivity()) {
                when (it) {
                    StateAudio.PLAYING -> {
                        viewBinding.imvPlayAndPause.setImageResource(R.drawable.ic_pause006)
                        viewBinding.tvPlayAndPause.setText(R.string.pause)
                        viewBinding.tvContAuto.visibility = View.VISIBLE
                        viewBinding.lnBtnFrontEar.visibility = View.GONE
                    }

                    StateAudio.STOP -> {
                        viewBinding.imvPlayAndPause.setImageResource(R.drawable.ic_play006)
                        viewBinding.tvPlayAndPause.setText(R.string.play)
                        viewBinding.tvContAuto.visibility = View.GONE
                        viewBinding.lnBtnFrontEar.visibility = View.GONE
                    }
                }
            }

            percentCleaner.observe(requireActivity()) {
                viewBinding.tvPercent.text = "$it %"
                viewBinding.arcView.setProgress(it.toFloat())

                if (it == 100) {
                    activity?.let {
                        (activity as MainActivity).showInter{
//                            TestSpeakerActivity.start(requireActivity())
                            kotlin.runCatching {
                                viewModel.stopAudio()
                            }
                        }
                    }
                }
            }

            sourceAudio.observe(requireActivity()) {
                when (it) {
                    SourceAudio.FRONT -> {
                        viewBinding.run {
                            btnFront.isSelected = true
                            btnEar.isSelected = false
                        }
                    }

                    SourceAudio.EAR -> {
                        viewBinding.run {
                            btnFront.isSelected = false
                            btnEar.isSelected = true
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(System.currentTimeMillis() - timeReward < 1000){
            return
        }

        kotlin.runCatching {
            viewModel.stopAudio()
        }
    }
}