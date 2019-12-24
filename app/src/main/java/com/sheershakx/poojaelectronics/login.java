package com.sheershakx.poojaelectronics;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

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

public class login extends AppCompatActivity {
    EditText mobile, password;

    Button loginbtn;

    public static String usertype;
    public static String userid;
    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //type casting
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.login);


        //signup process
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Mobile = mobile.getText().toString();
                String Password = password.getText().toString();

                if (!TextUtils.isEmpty(Mobile) && !TextUtils.isEmpty(Password)) {
                    new dologin().execute(Mobile, Password);

                } else
                    Toast.makeText(login.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public class dologin extends AsyncTask<String, String, String> {
        String db_url;

        @Override
        protected void onPreExecute() {

            db_url = "http://peitahari.000webhostapp.com/login.php";

        }

        @Override
        protected String doInBackground(String... params) {
            String mobile, password;
            mobile = params[0];
            password = params[1];

            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuffer buffer = new StringBuffer();
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String data = stringBuilder.toString().trim();

                JSONObject jsonObject = new JSONObject(data);
                usertype = jsonObject.getString("usertype");
                userid = jsonObject.getString("userid");
                username = jsonObject.getString("username");
//
                return data;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            if (s != null && s.contains("Successful")) {
                Toast.makeText(login.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                if (usertype != null && usertype.equals("0")) {

                    startActivity(new Intent(getApplicationContext(), admin_dashboard.class));
                    finish();

                } else if (usertype != null && usertype.equals("1")) {

                    startActivity(new Intent(getApplicationContext(), mechanic_dashboard.class));
                    finish();

                } else if (usertype != null && usertype.equals("2")) {

                    startActivity(new Intent(getApplicationContext(), client_dashboard.class));
                    finish();
                }
                else Toast.makeText(login.this, "User TYpe not found", Toast.LENGTH_SHORT).show();


            } else
                Toast.makeText(login.this, "Sorry,Login failed", Toast.LENGTH_SHORT).show();


        }
    }
}
