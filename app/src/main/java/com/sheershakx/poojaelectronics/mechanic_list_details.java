package com.sheershakx.poojaelectronics;

import android.app.ProgressDialog;
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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class mechanic_list_details extends AppCompatActivity {
    String uid;
    ProgressDialog progressDialog;
    String date, itemtype, status, cost, spec, serialno, size, model, clientproblem, remark;
    Button receivebtn, completedbtn, rejectbtn;
    EditText rejectreason;
    TextView Date, Uid, Itemtype, Status, Cost, Spec, Serialno, Size, Model, ClientProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_list_details);

        //typecasting
        Date = findViewById(R.id.date_mechanicdetail);
        Uid = findViewById(R.id.uid_mechanicdetail);
        Itemtype = findViewById(R.id.itemtype_mechanicdetail);
        Status = findViewById(R.id.status_mechanicdetail);
        Cost = findViewById(R.id.cost_mechanicdetail);
        Spec = findViewById(R.id.spec_mechanicdetail);
        Serialno = findViewById(R.id.serialno_mechanicdetail);
        Size = findViewById(R.id.size_mechanicdetail);
        Model = findViewById(R.id.model_mechanicdetail);
        rejectreason = findViewById(R.id.reject_reason_mech);


        receivebtn = findViewById(R.id.receivebtn_mechanic);
        completedbtn = findViewById(R.id.completedbtn_mechanic);
        rejectbtn = findViewById(R.id.rejectbtn_mechanic);
        ClientProblem = findViewById(R.id.clientproblem_mechanicdetail);

        receivebtn.setVisibility(View.GONE);
        rejectreason.setVisibility(View.GONE);
        rejectbtn.setVisibility(View.GONE);
        completedbtn.setVisibility(View.GONE);
        getincomingintent();

        rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectreason.setVisibility(View.VISIBLE);
                receivebtn.setVisibility(View.GONE);
                completedbtn.setVisibility(View.GONE);
                rejectbtn.setText("Enter the reason and submit here");

                String Rejectreason = rejectreason.getText().toString();
                if (rejectreason != null && !TextUtils.isEmpty(Rejectreason)) {
                   new updatestatusrejectmech().execute(Rejectreason);
                } else
                    Toast.makeText(getApplicationContext(), "Reason for rejection is compulsory", Toast.LENGTH_SHORT).show();
            }
        });

        receivebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new update_status_mechanic().execute("http://peitahari.000webhostapp.com/update_mechanic_rec.php");
                receivebtn.setEnabled(false);
            }
        });

        completedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mechanic_list_details.this, mechanic_deliver_action.class);
                intent.putExtra("uid", uid);
                mechanic_list_details.this.startActivity(intent);
                completedbtn.setEnabled(false);
            }
        });
    }

    private void getincomingintent() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        new mechanicpendingdetails().execute();

    }

    public class mechanicpendingdetails extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(mechanic_list_details.this, "", "Loading details..", true);

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
                status = jsonObject.getString("status");
                serialno = jsonObject.getString("serialno");
                size = jsonObject.getString("size");
                model = jsonObject.getString("model");
                clientproblem = jsonObject.getString("clientproblem");
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
            ClientProblem.setText(clientproblem);
            if (status != null && status.equals("0")) {
                Status.setText("Pending");
                receivebtn.setVisibility(View.VISIBLE);
                rejectbtn.setVisibility(View.VISIBLE);

            }
            if (status != null && status.equals("1")) {
                Status.setText("Accepted");
                completedbtn.setVisibility(View.VISIBLE);
            }
            if (status != null && status.equals("2")) {
                Status.setText("Returned");
            }
            if (status != null && status.equals("3")) {
                Status.setText("Delivered");
            }
            if (status != null && status.equals("4")) {
                Status.setText("Rejected for reason :" + remark);
            }


        }
    }

    public class update_status_mechanic extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... args) {

            String api_url;
            api_url = args[0];
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
                URL url = new URL(api_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&" +
                        URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(login.userid, "UTF-8");
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
            Toast.makeText(mechanic_list_details.this, "Status Changed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), mechanic_pending_list.class));
            finish();

        }
    }

    public class updatestatusrejectmech extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            db_url = "http://peitahari.000webhostapp.com/updatestatusrejectmechanic.php";

        }

        @Override
        protected String doInBackground(String... args) {
            String reason;
            reason = args[0];
            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8") + "&" +
                        URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(reason, "UTF-8");

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
            finish();


        }
    }

}
