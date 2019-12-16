package com.sheershakx.poojaelectronics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {
    EditText mobile,password;
    TextView forgotpass;
    Button loginbtn,registerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //type casting
        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.password);
        forgotpass=findViewById(R.id.forgotpass);
        loginbtn=findViewById(R.id.login);
        registerbtn=findViewById(R.id.register);

        //signup process
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Mobile=mobile.getText().toString();
                String Password=password.getText().toString();

                if (!TextUtils.isEmpty(Mobile) && !TextUtils.isEmpty(Password)){
                   ///call login api procedure here
                } else Toast.makeText(login.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
            }
        });

        //register redirect
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),signup.class));
                finish();
            }
        });

    }
}
