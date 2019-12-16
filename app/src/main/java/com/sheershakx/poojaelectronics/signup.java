package com.sheershakx.poojaelectronics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {
    Spinner usertype_spin;
    EditText name, address, firmname, mobile, password, confirmpassword;
    Button register;
    TextView redirectlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //typecasting
        usertype_spin = findViewById(R.id.usertype_spinner);
        //edittext
        name = findViewById(R.id.fullname);
        address = findViewById(R.id.address);
        firmname = findViewById(R.id.firmname);
        mobile = findViewById(R.id.mobile_r);
        password = findViewById(R.id.password_R);
        confirmpassword = findViewById(R.id.confirmpassword_R);
        //button
        register = findViewById(R.id.register_btn);
        //textview
        redirectlogin = findViewById(R.id.loginredirect);

        //setting values to spinner
        String[] list = {"---User Type---", "Mechanic", "Client"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usertype_spin.setAdapter(adapter);

        //registration process
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Address = name.getText().toString();
                String Firmname = firmname.getText().toString();
                String Mobile = mobile.getText().toString();
                String Password = password.getText().toString();
                String Confirmpassword = confirmpassword.getText().toString();
                String Usertype = usertype_spin.getSelectedItem().toString();

                if (!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Mobile) && !TextUtils.isEmpty(Password) && !TextUtils.isEmpty(Confirmpassword)) {
               if (Usertype.equals("---User Type---")) {
                   Toast.makeText(signup.this, "Select user type", Toast.LENGTH_SHORT).show();
               }
               else if (!Password.equals(Confirmpassword)){
                   Toast.makeText(signup.this, "Password didn't matched", Toast.LENGTH_SHORT).show();
               }
               else {
                   //call api procedure here
               }
                }
                else Toast.makeText(signup.this, "Some fields cannot be empty", Toast.LENGTH_SHORT).show();


            }
        });

        //redirect login
        redirectlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        });


    }
}
