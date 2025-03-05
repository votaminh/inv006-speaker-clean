package com.msc.speaker_cleaner.component.cleanerauto_inv

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msc.speaker_cleaner.component.cleanerauto_inv.auto.AutoThreadAudio
import com.msc.speaker_cleaner.component.cleanerauto_inv.auto.rv2
import com.msc.speaker_cleaner.domain.layer.SourceAudio
import com.msc.speaker_cleaner.domain.layer.StateAudio
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class AutoViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {

    private var va: ValueAnimator? = null
    private var autoThreadAudio: AutoThreadAudio? = null
    val stateAudio = MutableLiveData<StateAudio>()
    val sourceAudio = MutableLiveData(SourceAudio.FRONT)
    val percentCleaner = MutableLiveData<Int>()

    private val rv2: rv2 = rv2()

    fun start() {
        if(autoThreadAudio == null){
            autoThreadAudio =
                AutoThreadAudio(
                    context,
                    rv2
                )

            stateAudio.postValue(StateAudio.PLAYING)

            rv2.a = (170.0) * 5.0
            rv2.a(100)

            autoThreadAudio?.setVolume(0f);
            autoThreadAudio?.start()

            va = ValueAnimator.ofFloat(0f, 1f)
            va?.run {
                duration = 10000
                addUpdateListener((AnimatorUpdateListener { animation: ValueAnimator ->
                    val volume = animation.animatedValue as Float
                    autoThreadAudio?.setVolume(volume)
                    val percent = (volume * 100).toInt()
                    percentCleaner.postValue(percent)
                }))
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {
                    }

                    override fun onAnimationEnd(p0: Animator) {
                        autoThreadAudio?.stopAudio()
                        va?.cancel()
                    }

                    override fun onAnimationCancel(p0: Animator) {
                    }

                    override fun onAnimationRepeat(p0: Animator) {
                    }
                })
                start()
            }
        }else{
            stopAudio()
        }
    }

    fun stopAudio() {
        stateAudio.postValue(StateAudio.STOP)
        percentCleaner.postValue(0)
        autoThreadAudio?.stopAudio()
        autoThreadAudio = null
        va?.cancel()
    }

    fun useEarpiece() {
        autoThreadAudio?.useEarpiece()
        sourceAudio.postValue(SourceAudio.EAR)
    }

    fun useSpeaker() {
        autoThreadAudio?.useSpeaker()
        sourceAudio.postValue(SourceAudio.FRONT)
    }

    fun isAudioPlaying(): Boolean {
        return autoThreadAudio != null
    }
}