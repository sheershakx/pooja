package com.sheershakx.poojaelectronics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class mechanic_dashboard extends AppCompatActivity {
    Button pending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_dashboard);

        pending = findViewById(R.id.pending_mechanic);
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), mechanic_list_details.class));
                Toast.makeText(mechanic_dashboard.this, "1 page is hidden due to development process", Toast.LENGTH_LONG).show();
            }
        });
    }
}
