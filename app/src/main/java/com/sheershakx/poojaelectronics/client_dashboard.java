package com.sheershakx.poojaelectronics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class client_dashboard extends AppCompatActivity {
    Button post,viewstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);

        post=findViewById(R.id.post_client);
        viewstatus=findViewById(R.id.viewstatus_client);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),client_post.class));
            }
        });

        viewstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),client_status_detail.class));
                Toast.makeText(client_dashboard.this, "Since there is no data ,1 page is hidden here", Toast.LENGTH_LONG).show();
            }
        });
    }
}
