package com.msc.speaker_cleaner.component.cleanermanual_inv

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.msc.speaker_cleaner.component.cleanerauto_inv.auto.AutoThreadAudio
import com.msc.speaker_cleaner.domain.layer.StateAudio
import com.msc.speaker_cleaner.component.cleanerauto_inv.auto.rv2
import com.msc.speaker_cleaner.domain.layer.SourceAudio
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ManualViewModel @Inject constructor(@ApplicationContext val context: Context) :
    ViewModel(), LifecycleObserver {

    private var autoThreadAudio: AutoThreadAudio? = null
    private val rv2: rv2 =
        rv2()

    val stateAudio = MutableLiveData<StateAudio>()
    val frequencyAudioLive = MutableLiveData<Int>()
    val sourceAudioLive = MutableLiveData(SourceAudio.FRONT)

    fun start(){
        if(autoThreadAudio == null){
            autoThreadAudio =
                AutoThreadAudio(
                    context,
                    rv2
                )

            rv2.a = (170.0) * 5.0
            rv2.a(100)

            autoThreadAudio?.run{
                setVolume(100f)
                val frequency = frequencyAudioLive.value
                frequency?.let {
                    setFrequency(it.toDouble())
                }
                if(sourceAudioLive.value == SourceAudio.EAR){
                    useEarpiece()
                }else{
                    useSpeaker()
                }
                start()
            }
            stateAudio.postValue(StateAudio.PLAYING)
        }else{
            cancelAudio()
        }
    }

    fun cancelAudio() {
        autoThreadAudio?.stopAudio()
        autoThreadAudio = null
        stateAudio.postValue(StateAudio.STOP)
    }

    fun setFrequency(frequency: Float) {
        autoThreadAudio?.setFrequency(frequency.toDouble())
        frequencyAudioLive.postValue(frequency.toInt())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onActivityPause() {
        cancelAudio()
    }

    fun setFront() {
        sourceAudioLive.postValue(SourceAudio.FRONT)
        autoThreadAudio?.useSpeaker()
    }

    fun setEar(){
        sourceAudioLive.postValue(SourceAudio.EAR)
        autoThreadAudio?.useEarpiece()
    }

    fun isAudioPlaying(): Boolean {
        return autoThreadAudio != null
    }
}