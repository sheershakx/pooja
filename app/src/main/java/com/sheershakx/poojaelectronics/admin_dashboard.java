package com.sheershakx.poojaelectronics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class admin_dashboard extends AppCompatActivity {
    Button createmechanic, createclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        //typecasting
        createmechanic = findViewById(R.id.createmechanic_admin);
        createclient = findViewById(R.id.createclient_admin);

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
    }

}
