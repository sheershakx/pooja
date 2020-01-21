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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class mechanic_deliver_action extends AppCompatActivity {
    EditText problem, cost;
    Button deliver;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_deliver_action);

        getIncomingIntent();

        problem = findViewById(R.id.problem_specification);
        cost = findViewById(R.id.cost_mechanic);
        deliver = findViewById(R.id.deliver_mechanic);


        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Problem = problem.getText().toString();
                String Cost = cost.getText().toString();
                if (!TextUtils.isEmpty(Problem)) {
                    if (TextUtils.isEmpty(Cost)) {
                        new deliversolved().execute(Problem, "000");
                        deliver.setEnabled(false);
                    } else {
                        new deliversolved().execute(Problem, Cost);
                        deliver.setEnabled(false);
                    }
                } else
                    Toast.makeText(mechanic_deliver_action.this, "Please give some problem details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getIncomingIntent() {
        uid = getIntent().getStringExtra("uid");
    }

    public class deliversolved extends AsyncTask<String, String, String> {
        String api_url;

        @Override
        protected void onPreExecute() {
            api_url = "http://peitahari.000webhostapp.com/update_mechanic_delv.php";

        }

        @Override
        protected String doInBackground(String... args) {

            String problem, cost;
            problem = args[0];
            cost = args[1];



            try {
                URL url = new URL(api_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(login.nepalidate, "UTF-8") + "&" +
                        URLEncoder.encode("problem", "UTF-8") + "=" + URLEncoder.encode(problem, "UTF-8") + "&" +
                        URLEncoder.encode("cost", "UTF-8") + "=" + URLEncoder.encode(cost, "UTF-8");


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


            } catch (ProtocolException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(mechanic_deliver_action.this, "Item Delivered", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(mechanic_deliver_action.this, mechanic_dashboard.class);
            finish();
            overridePendingTransition(0, 0);
            startActivity(i);
            overridePendingTransition(0, 0);


        }
    }
}
