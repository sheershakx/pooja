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
import java.util.Calendar;
import java.util.Locale;

public class admin_report_detail extends AppCompatActivity {
    ProgressDialog progressDialog;
    String date, itemtype, spec, cost, mechanicstatus, adminstatus, serialno, size, model, m_approve, m_deliver, c_deliver, c_received, problem, mech_cost, Clientproblem,remark;
    String uid;

    TextView Uid, Itemtype, Date, Spec, Cost, Status, Serialno, Size, Model, problemsolved, mechcost, madate, mddate, cddate, crdate, clientproblem;

    Button receivedBtn;
    Button recv_mech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report_detail);

        receivedBtn = findViewById(R.id.received_reportdetail);
        Uid = findViewById(R.id.uid_reportdetail);
        Itemtype = findViewById(R.id.itemtype_reportdetail);
        Date = findViewById(R.id.date_reportdetail);
        Spec = findViewById(R.id.spec_reportdetail);
        Cost = findViewById(R.id.cost_reportdetail);
        Status = findViewById(R.id.status_reportdetail);
        Serialno = findViewById(R.id.serialno_reportdetail);
        Size = findViewById(R.id.size_reportdetail);
        Model = findViewById(R.id.model_reportdetail);
        problemsolved = findViewById(R.id.problem_reportdetail);
        mechcost = findViewById(R.id.mechcost_reportdetail);
        mddate = findViewById(R.id.mddate_reportdetail);
        madate = findViewById(R.id.madate_reportdetail);
        cddate = findViewById(R.id.cddate_reportdetail);
        crdate = findViewById(R.id.crdate_reportdetail);

        recv_mech = findViewById(R.id.recv_mech_reportdetail);

        clientproblem = findViewById(R.id.clientproblem_reportdetail);

        recv_mech.setVisibility(View.GONE);

        recv_mech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new updatestatusmech().execute();
                recv_mech.setEnabled(false);
            }
        });

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
            progressDialog = ProgressDialog.show(admin_report_detail.this, "", "Loading orders..", true);

            db_url = "http://peitahari.000webhostapp.com/admin_report_details.php";

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
                mechanicstatus = jsonObject.getString("mechanicstatus");
                adminstatus = jsonObject.getString("adminstatus");
                serialno = jsonObject.getString("serialno");
                size = jsonObject.getString("size");
                model = jsonObject.getString("model");
                problem = jsonObject.getString("problem");
                mech_cost = jsonObject.getString("mech_cost");
                Clientproblem = jsonObject.getString("clientproblem");

                m_approve = jsonObject.getString("m_approvedate");
                m_deliver = jsonObject.getString("m_deliverdate");
                c_deliver = jsonObject.getString("c_deliverdate");
                c_received = jsonObject.getString("c_receiveddate");
                remark = jsonObject.getString("remark");


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
            problemsolved.setText(problem);
            mechcost.setText(mech_cost);
            madate.setText(m_approve);
            mddate.setText(m_deliver);
            cddate.setText(c_deliver);
            crdate.setText(c_received);
            clientproblem.setText(Clientproblem);
            if (adminstatus != null && adminstatus.equals("0")) {
                Status.setText("Pending (Sent to Mechanic)");

            }
            if (adminstatus != null && adminstatus.equals("1")) {
                Status.setText("Received (by Mechanic)");

            }
            if (adminstatus != null && adminstatus.equals("2")) {
                Status.setText("Delivered(by Mechanic)");
                recv_mech.setVisibility(View.VISIBLE);
                // receivedBtn.setVisibility(View.VISIBLE);
            }
            if (adminstatus != null && adminstatus.equals("3")) {
                Status.setText("Pending(Delivered to client");

            }
            if (adminstatus != null && adminstatus.equals("4")) {
                Status.setText("Received(by Client)");

            }
            if (adminstatus != null && adminstatus.equals("5")) {
                Status.setText("Received(from mechanic)");
                receivedBtn.setVisibility(View.VISIBLE);
            }
            if (mechanicstatus != null && adminstatus != null&& adminstatus.equals("2") && mechanicstatus.equals("4")) {
                Status.setText("Rejected(from mechanic) due to :"+remark);
               recv_mech.setVisibility(View.VISIBLE);
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

            String date = (String) android.text.format.DateFormat.
                    format("yyyy-MM-dd", Calendar.getInstance().getTime());

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

    public class updatestatusmech extends AsyncTask<String, String, String> {
        String db_url;

        @Override
        protected void onPreExecute() {
            db_url = "http://peitahari.000webhostapp.com/updatestatusmech.php";

        }

        @Override
        protected String doInBackground(String... args) {

            String date = (String) android.text.format.DateFormat.
                    format("yyyy-MM-dd",Calendar.getInstance().getTime());

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
            recv_mech.setEnabled(true);
            finish();

        }
    }
}
