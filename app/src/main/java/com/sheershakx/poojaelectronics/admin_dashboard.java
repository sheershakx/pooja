package com.sheershakx.poojaelectronics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class admin_dashboard extends AppCompatActivity {
    Button createmechanic, createclient;

    Button clientreq, allreport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        //typecasting
        createmechanic = findViewById(R.id.createmechanic_admin);
        createclient = findViewById(R.id.createclient_admin);
        clientreq = findViewById(R.id.client_request);
        allreport = findViewById(R.id.allreport_admin);

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
    }

}
