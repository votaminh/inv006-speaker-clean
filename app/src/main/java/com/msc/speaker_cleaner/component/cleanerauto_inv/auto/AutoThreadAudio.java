package com.msc.speaker_cleaner.component.cleanerauto_inv.auto;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public final class AutoThreadAudio extends Thread {

    public static final int MIN_FREQUENCY = 0;
    public static final int MAX_FREQUENCY = 520;

    public static final /* synthetic */ int h = 0;
    public final int c = 45100; // Sample rate
    public boolean d; // Control flag for the thread's run state
    public final AudioTrack audioTrack; // AudioTrack instance for audio playback
    public final rv2 f; // Reference to an external object of type rv2
    public final boolean g; // A boolean flag to modify behavior
    private final AudioManager audioManager; // AudioManager for managing audio routing
    private double frequency = 520; // Default frequency (0 hz - 520hz)

    public AutoThreadAudio(Context context, rv2 rv2) {
        int i;
        this.f = rv2;
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int minBufferSize = AudioTrack.getMinBufferSize(45100, 12, 2);
        boolean z = rv2.c;
        this.g = z;
        if (z) {
            i = 0;
        } else {
            i = 3;
        }
        this.audioTrack = new AudioTrack(i, 45100, 12, 2, minBufferSize, 1);
    }

    public final void run() {
        double d2;
        double d3;
        super.run();
        this.d = true;
        short[] sArr = new short[760];
        AudioTrack audioTrack = this.audioTrack;
        audioTrack.play();
        double d4 = 0.0d;
        while (this.d) {
            for (int i = 0; i < 760; i += 2) {
                double d5 = 2.0 * Math.PI * d4;
                if (this.g) {
                    d3 = Math.sin(d5);
                    d2 = 12800.0d;
                } else {
                    d3 = Math.sin(d5);
                    d2 = 14500.0d;
                }
                short s = (short) ((int) (d3 * d2));
                sArr[i] = s;
                sArr[i + 1] = s;
                d4 += (this.frequency * 5.0d) / ((double) this.c);
            }
            audioTrack.write(sArr, 0, 760);
        }
        audioTrack.stop();
        audioTrack.release();
    }

    // Method to set volume
    public void setVolume(float volume) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            this.audioTrack.setVolume(volume);
        } else {
            this.audioTrack.setStereoVolume(volume, volume);
        }
    }

    // Method to stop audio
    public void stopAudio() {
        this.d = false;
    }

    // Method to switch to earpiece
    public void useEarpiece() {
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(false);
    }

    // Method to switch to speaker
    public void useSpeaker() {
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
    }

    // Method to set the volume for earpiece
    public void setEarpieceVolume(float volume) {
        useEarpiece();
        setVolume(volume);
    }

    // Method to set the volume for speaker
    public void setSpeakerVolume(float volume) {
        useSpeaker();
        setVolume(volume);
    }

    // Method to set frequency
    public void setFrequency(double frequency) {
        Log.i("ingsdg", "setFrequency: " + frequency);
        this.frequency = frequency;
    }
}
