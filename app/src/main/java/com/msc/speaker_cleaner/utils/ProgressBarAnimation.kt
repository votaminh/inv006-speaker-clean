package com.msc.speaker_cleaner.utils

import android.view.animation.Animation
import android.view.animation.Transformation
import com.msc.speaker_cleaner.view.ArcProgressView

class ProgressBarAnimation(
    private val progressBar: ArcProgressView, private val from: Float,
    private val f23631to: Float
) : Animation() {
    public override fun applyTransformation(f: Float, transformation: Transformation) {
        super.applyTransformation(f, transformation)
        val f2 = this.from
        progressBar.setProgress(
            (f2 + ((this.f23631to - f2) * f)).toInt()
                .toFloat()
        )
    }
}
