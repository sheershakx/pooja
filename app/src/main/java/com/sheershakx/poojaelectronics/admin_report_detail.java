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

public class admin_report_detail extends AppCompatActivity {
    ProgressDialog progressDialog;
    String date, itemtype, spec, cost, status,mechanicstatus,adminstatus;
    String uid;

    TextView Uid, Itemtype, Date, Spec, Cost, Status;

    Button receivedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report_detail);

        receivedBtn = findViewById(R.id.received_clientdetail);
        Uid = findViewById(R.id.uid_reportdetail);
        Itemtype = findViewById(R.id.itemtype_reportdetail);
        Date = findViewById(R.id.date_reportdetail);
        Spec = findViewById(R.id.spec_reportdetail);
        Cost = findViewById(R.id.cost_reportdetail);
        Status = findViewById(R.id.status_reportdetail);

        receivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update user status and admin status into received thorugh calling api
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
            progressDialog = ProgressDialog.show(admin_report_detail.this, "", "Loading orders..", true);

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
                mechanicstatus = jsonObject.getString("mechanicstatus");
                adminstatus = jsonObject.getString("adminstatus");


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
            if (status != null && status.equals("0")) {
                Status.setText("Pending");

            }
            if (status != null && status.equals("1")) {
                Status.setText("Approved");

            }
            if (status != null && status.equals("2")) {
                Status.setText("Delivered");
            }
            if (status != null && status.equals("3")) {
                Status.setText("Received");

            }


        }
    }
}
