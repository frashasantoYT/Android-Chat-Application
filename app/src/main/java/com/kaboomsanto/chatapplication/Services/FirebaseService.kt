package com.kaboomsanto.chatapplication.Services

import android.app.NotificationManager
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kaboomsanto.chatapplication.ui.MainActivity

class FirebaseService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


    }

}
