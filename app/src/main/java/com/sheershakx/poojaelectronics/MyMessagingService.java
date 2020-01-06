package com.sheershakx.poojaelectronics;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotifications(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    public  void showNotifications(String title,String message){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"mynotifications")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.notify_icon)
                .setAutoCancel(true)
                .setContentText(message);

        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder.build());


    }


}
