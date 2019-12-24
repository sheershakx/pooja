package com.sheershakx.poojaelectronics;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class changepassword extends AppCompatActivity {

    EditText newpass, confirmpass;
    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        //typecasting
        newpass = findViewById(R.id.newpass);
        confirmpass = findViewById(R.id.newpass_confirm);
        change = findViewById(R.id.change_pass_btn);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pass = newpass.getText().toString();
                String CPass = confirmpass.getText().toString();
                if (!TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(CPass)) {
                    if (Pass.equals(CPass)) {
                        new changepass().execute(Pass);
                    } else
                        Toast.makeText(changepassword.this, "Password don't match", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(changepassword.this, "Please enter new password", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class changepass extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {

            db_url = "http://peitahari.000webhostapp.com/changepassword.php";

        }

        @Override
        protected String doInBackground(String... args) {

            String pass = args[0];


            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("newpassword", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8") + "&" +
                        URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(login.userid, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
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
            Toast.makeText(changepassword.this, "Password changed. Please login again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }
    }
}
