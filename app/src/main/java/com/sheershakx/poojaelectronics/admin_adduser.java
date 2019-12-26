package com.sheershakx.poojaelectronics;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class admin_adduser extends AppCompatActivity {
    String usertype;
    String usertypeINT;
    RadioButton usertyperadio;

    EditText name, address, mobile;
    Button createuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_adduser);

        //typecasting

        usertyperadio = findViewById(R.id.usertype_createuser);

        name = findViewById(R.id.name_createuser);
        address = findViewById(R.id.address_createuser);
        mobile = findViewById(R.id.mobile_createuser);

        createuser = findViewById(R.id.createuser_createuser);

        getUserTypeIntent();

        createuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Address = address.getText().toString();
                String Mobile = mobile.getText().toString();
                if (usertype.equals("Mechanic")) {
                    usertypeINT = "1";
                } else if (usertype.equals("Client")) {
                    usertypeINT = "2";
                }


                if (!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Mobile)) {
                    new createuser().execute(Name, Address, Mobile, usertypeINT);
                }
                else Toast.makeText(admin_adduser.this, "Fields can't be blank", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getUserTypeIntent() {
        usertype = getIntent().getStringExtra("usertype");
       usertyperadio.setText(usertype);
        usertyperadio.setChecked(true);
    }


    public class createuser extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {

            db_url = "http://peitahari.000webhostapp.com/createuser.php";

        }

        @Override
        protected String doInBackground(String... args) {

            String name = args[0];
            String address = args[1];
            String mobile = args[2];
            String type = args[3];


            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("usertype", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&" +
                        URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8");


                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();

                return null;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(admin_adduser.this, "User Created", Toast.LENGTH_SHORT).show();
            name.setText("");
            address.setText("");
            mobile.setText("");
        }
    }
}
