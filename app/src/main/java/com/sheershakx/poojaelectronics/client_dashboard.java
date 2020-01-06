package com.sheershakx.poojaelectronics;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class client_dashboard extends AppCompatActivity {
    Button post, viewstatus;
    TextView nameview;
    public static final String CHANNEL_1_ID = "channel1";
    NotificationManagerCompat notificationManager;

    @Override
    public void onBackPressed() {
        new acceptDialog_admin().show(getSupportFragmentManager(), "quit?");
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);
        firebasenotificationregister();
        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannels();
        publish_notifications();
        Toolbar toolbar = findViewById(R.id.client_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        post = findViewById(R.id.post_client);
        viewstatus = findViewById(R.id.viewstatus_client);
        nameview = findViewById(R.id.nameview_client);
        nameview.setText(login.username);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), client_post.class));
            }
        });

        viewstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), client_status.class));

            }
        });
    }

    public void firebasenotificationregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("mynotifications", "mynotifications", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
            FirebaseMessaging.getInstance().subscribeToTopic("client")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }


    }


    public void sendOnChannel1(String title, String message) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notify_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);


    }

    private void publish_notifications() {
        if (login.notify != null && login.notify.equals("1")) {
            sendOnChannel1("Item Accepted", "Your item has been accepted");
        }
        if (login.notify != null && login.notify.equals("2")) {
            sendOnChannel1("Item Rejected", "Sorry,your item has been rejected ");
        }
        if (login.notify != null && login.notify.equals("3")) {
            sendOnChannel1("Item Delivered", "Your item has been delivered to you");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_forgotpass:
                startActivity(new Intent(getApplicationContext(), changepassword.class));
                return true;
            case R.id.menu_aboutus:
                Toast.makeText(this, "about us", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
