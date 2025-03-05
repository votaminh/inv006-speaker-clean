package com.msc.speaker_cleaner.component.cleanerauto_inv.auto;

public final class rv2 {
    public double a = 440.0d;
    public AutoThreadAudio b;
    public boolean c;

    public final void a(int i) {
        float f = ((float) i) / 100.0f;
        AutoThreadAudio ThreadAudio = this.b;
        if (ThreadAudio != null) {
            ThreadAudio.audioTrack.setVolume(f);
        }
    }
}
