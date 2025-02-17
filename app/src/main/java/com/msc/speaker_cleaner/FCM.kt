package com.msc.speaker_cleaner

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FCM : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.i("gsdgdsg", "onNewToken: " + token)
        super.onNewToken(token)
    }
}