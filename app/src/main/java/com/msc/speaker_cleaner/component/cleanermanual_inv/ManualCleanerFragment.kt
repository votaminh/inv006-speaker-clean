package com.msc.speaker_cleaner.component.cleanermanual_inv

import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import com.flash.light.base.fragment.BaseFragment
import com.msc.speaker_cleaner.R
import com.msc.speaker_cleaner.component.cleanerauto_inv.auto.AutoThreadAudio
import com.msc.speaker_cleaner.component.main.MainActivity
import com.msc.speaker_cleaner.component.testspeaker.activity.TestSpeakerActivity
import com.msc.speaker_cleaner.databinding.FragmentManualCleanerBinding
import com.msc.speaker_cleaner.domain.layer.SourceAudio
import com.msc.speaker_cleaner.domain.layer.StateAudio
import com.msc.speaker_cleaner.utils.AppEx.range
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManualCleanerFragment : BaseFragment<FragmentManualCleanerBinding>() {

    private val viewModel: ManualViewModel by viewModels()
    private var canOpenTest = false
    var timeReward = 0L
    override fun provideViewBinding(container: ViewGroup?): FragmentManualCleanerBinding {
        return FragmentManualCleanerBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        lifecycle.addObserver(viewModel)

        viewBinding.apply {
            btnPlayAndPause.setOnClickListener {
                if(viewModel.isAudioPlaying()){
                    viewModel.cancelAudio()
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

            sbVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    viewModel.setFrequency(
                        p1.range(
                            AutoThreadAudio.MIN_FREQUENCY.toFloat(),
                            AutoThreadAudio.MAX_FREQUENCY.toFloat()
                        )
                    )
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })

            btnFront.setOnClickListener {
                viewModel.setFront()
            }
            btnEar.setOnClickListener {
                viewModel.setEar()
            }

            sbVolume.progress = 30
        }
    }

    override fun initObserver() {
        super.initObserver()

        viewModel.run {
            stateAudio.observe(requireActivity()) {
                when (it) {
                    StateAudio.PLAYING -> {
                        viewBinding.imvPlayAndPause.setImageResource(R.drawable.ic_pause006)
                        viewBinding.tvPlayAndPause.text = getString(R.string.pause)
                        viewBinding.tvIntro.text =
                            getString(R.string.run_23_message)
                        viewBinding.lnBtnFontEar.visibility = View.GONE
                        canOpenTest = true
                    }

                    StateAudio.STOP -> {
                        viewBinding.imvPlayAndPause.setImageResource(R.drawable.ic_play006)
                        viewBinding.tvPlayAndPause.text = getString(R.string.play)
                        viewBinding.tvIntro.text = getString(R.string.speaker)
                        viewBinding.lnBtnFontEar.visibility = View.VISIBLE

                        if(canOpenTest){
                            canOpenTest = false
                            activity?.let {
                                (activity as MainActivity).showInter{
                                    TestSpeakerActivity.start(requireActivity())
                                }
                            }
                        }
                    }
                }
            }

            frequencyAudioLive.observe(requireActivity()) {
                viewBinding.tvHZ.text = "$it HZ"
            }

            sourceAudioLive.observe(requireActivity()) {
                when (it) {
                    SourceAudio.FRONT -> {
                        viewBinding.run {
                            btnFront.setBackgroundResource(R.drawable.bg_sellect_button006)
                            btnEar.setBackgroundResource(R.color.transparent)
                            btnFront.setTextColor(requireActivity().getColor(R.color.black))
                            btnEar.setTextColor(requireActivity().getColor(R.color.white))
                        }
                    }

                    SourceAudio.EAR -> {
                        viewBinding.run {
                            btnFront.setBackgroundResource(R.color.transparent)
                            btnEar.setBackgroundResource(R.drawable.bg_sellect_button006)
                            btnFront.setTextColor(requireActivity().getColor(R.color.white))
                            btnEar.setTextColor(requireActivity().getColor(R.color.black))
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
            viewModel.cancelAudio()
            canOpenTest = false
        }
    }
}