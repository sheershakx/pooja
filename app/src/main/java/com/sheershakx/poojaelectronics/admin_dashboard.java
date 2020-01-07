package com.sheershakx.poojaelectronics;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class admin_dashboard extends AppCompatActivity {
    Button createmechanic, createclient;

    Button clientreq, allreport, mdelivered, completed;

    @Override
    public void onBackPressed() {
        new acceptDialog_admin().show(getSupportFragmentManager(),"quit?");
       return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        firebasenotificationregister();
        //typecasting
        createmechanic = findViewById(R.id.createmechanic_admin);
        createclient = findViewById(R.id.createclient_admin);
        clientreq = findViewById(R.id.client_request);
        allreport = findViewById(R.id.allreport_admin);
        completed = findViewById(R.id.completed_admin);
        mdelivered = findViewById(R.id.mdelivered_report);

        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        createmechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_dashboard.this, admin_adduser.class);
                intent.putExtra("usertype", "Mechanic");
                admin_dashboard.this.startActivity(intent);

            }
        });

        createclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_dashboard.this, admin_adduser.class);
                intent.putExtra("usertype", "Client");
                admin_dashboard.this.startActivity(intent);
            }
        });

        clientreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), admin_pending_list.class));
            }
        });

        allreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), admin_report_list.class));
            }
        });

        mdelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), mechanicDelivered.class));

            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), completedAll.class));
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
        FirebaseMessaging.getInstance().subscribeToTopic("admin")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
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
            case R.id.menu_additem:
                startActivity(new Intent(getApplicationContext(), addItemGroup.class));
                return true;
            case R.id.menu_userlist:
                startActivity(new Intent(getApplicationContext(), userlist.class));
                return true;
            case R.id.menu_search:
                startActivity(new Intent(getApplicationContext(), advance_search.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
