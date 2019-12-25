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

public class admin_pending_details extends AppCompatActivity {
    String uid, clientid;
    ProgressDialog progressDialog;
    String date, itemtype, status,astatus, cost, spec, serialno, size, model,clientproblem;
    Button forwardbtn, acceptbtn;
    TextView Date, Uid, Itemtype, Status, Cost, Spec, Serialno, Size, Model,ClientProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pending_details);

        //typecasting
        Date = findViewById(R.id.date_admindetail);
        Uid = findViewById(R.id.uid_admindetail);
        Itemtype = findViewById(R.id.itemtype_admindetail);
        Status = findViewById(R.id.status_admindetail);
        Cost = findViewById(R.id.cost_admindetail);
        Spec = findViewById(R.id.spec_admindetail);
        Serialno = findViewById(R.id.serialno_admindetail);
        Size = findViewById(R.id.size_admindetail);
        Model = findViewById(R.id.model_admindetail);

        acceptbtn = findViewById(R.id.acceptbtn_admin);

        forwardbtn = findViewById(R.id.completedbtn_admin);

        ClientProblem=findViewById(R.id.clientproblem_admindetail);

        acceptbtn.setVisibility(View.GONE);
        forwardbtn.setVisibility(View.GONE);

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new updatestatusaccepted().execute();
            }
        });

        forwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_pending_details.this, admin_forward_mechanic.class);
                intent.putExtra("uid", uid);
                intent.putExtra("itemtype", itemtype);
                intent.putExtra("date", date);
                admin_pending_details.this.startActivity(intent);
            }
        });

        getincomingintent();


    }

    private void getincomingintent() {
        Intent intent = getIntent();
        if (intent.hasExtra("uid")) {
            uid = intent.getStringExtra("uid");
            clientid = intent.getStringExtra("clientid");
            status = intent.getStringExtra("status");
            astatus = intent.getStringExtra("astatus");
            new adminpendingdetails().execute();
        }
    }

    public class adminpendingdetails extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(admin_pending_details.this, "", "Loading orders..", true);

            db_url = "http://peitahari.000webhostapp.com/mechanic_pending_details.php";

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
                serialno = jsonObject.getString("serialno");
                size = jsonObject.getString("size");
                model = jsonObject.getString("model");
                clientproblem = jsonObject.getString("clientproblem");


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
            ClientProblem.setText(clientproblem);
            if (status != null && status.equals("1") && astatus!=null && astatus.equals("0")) {
                Status.setText("Pending(Forwarded to mechanic)");
            }
            if (astatus != null && status!=null && status.equals("0") && astatus.equals("9")) {
                Status.setText("Not accepted");
                acceptbtn.setVisibility(View.VISIBLE);
            }
            if (astatus != null && status!=null && status.equals("1") && astatus.equals("9")) {
                Status.setText("Request Accepted");
                forwardbtn.setVisibility(View.VISIBLE);
            }
            if (status != null && astatus.equals("1")) {
                Status.setText("Accepted(by mechanic)");
            }
            if (status != null && astatus.equals("2")) {
                Status.setText("Returned(by mechanic)");
            }


        }
    }

    public class updatestatusaccepted extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            db_url = "http://peitahari.000webhostapp.com/updatestatusaccepted.php";

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
           startActivity(new Intent(getApplicationContext(),admin_pending_list.class));
           finish();


        }
    }
}
