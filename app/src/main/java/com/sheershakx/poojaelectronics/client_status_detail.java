package com.sheershakx.poojaelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class client_status_detail extends AppCompatActivity {
    ProgressDialog progressDialog;
    String date, itemtype, spec, cost, status, serialno, size, model;
    String uid;

    TextView Uid, Itemtype, Date, Spec, Cost, Status, Serialno, Size, Model;

    Button receivedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_status_detail);

        receivedBtn = findViewById(R.id.received_clientdetail);
        Uid = findViewById(R.id.uid_clientdetail);
        Itemtype = findViewById(R.id.itemtype_clientdetail);
        Date = findViewById(R.id.date_clientdetail);
        Spec = findViewById(R.id.spec_clientdetail);
        Cost = findViewById(R.id.cost_clientdetail);
        Status = findViewById(R.id.status_clientdetail);

        Serialno = findViewById(R.id.serialno_clientdetail);
        Size = findViewById(R.id.size_clientdetail);
        Model = findViewById(R.id.model_clientdetail);

        //hiding received button until deliverd status
        receivedBtn.setVisibility(View.GONE);
        receivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new updatestatus().execute();
               receivedBtn.setEnabled(false);
            }
        });

        getincomingintent();
    }

    private void getincomingintent() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        new clientStatusDetail().execute();
    }

    public class clientStatusDetail extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(client_status_detail.this, "", "Loading orders..", true);

            db_url = "http://peitahari.000webhostapp.com/client_status_detail.php";

        }

        @Override
        protected String doInBackground(String... args) {

            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8");
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


                date = jsonObject.getString("date");
                itemtype = jsonObject.getString("itemtype");
                spec = jsonObject.getString("spec");
                cost = jsonObject.getString("cost");
                status = jsonObject.getString("status");
                serialno = jsonObject.getString("serialno");
                size = jsonObject.getString("size");
                model = jsonObject.getString("model");


            } catch (ProtocolException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Uid.setText(uid);
            Itemtype.setText(itemtype);
            Date.setText(date);
            Cost.setText(cost);
            Spec.setText(spec);
            Serialno.setText(serialno);
            Size.setText(size);
            Model.setText(model);
            if (status != null && status.equals("0")) {
                Status.setText("Pending");

            }
            if (status != null && status.equals("1")) {
                Status.setText("Approved");

            }
            if (status != null && status.equals("2")) {
                Status.setText("Delivered");
                receivedBtn.setVisibility(View.VISIBLE);

            }
            if (status != null && status.equals("3")) {
                Status.setText("Received");

            }


        }
    }
    public class updatestatus extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            db_url = "http://peitahari.000webhostapp.com/updatestatus.php";

        }

        @Override
        protected String doInBackground(String... args) {

            String date = null;

            LocalDateTime currdate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                currdate = LocalDateTime.now();
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String Date = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(currdate);
                date = Date;
            }

            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");

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
            receivedBtn.setEnabled(true);
            finish();

        }
    }
}
