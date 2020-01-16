package com.sheershakx.poojaelectronics;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class mechanic_dashboard extends AppCompatActivity {
    Button pending;

    String uid, itemtype, date, status;
    ArrayList<String> UID = new ArrayList<String>();
    ArrayList<String> ITEMTYPE = new ArrayList<String>();
    ArrayList<String> DATE = new ArrayList<String>();
    ArrayList<String> STATUS = new ArrayList<String>();

    TextView mechanicnameview;

    TextView currdateshow, pendingq, received, delivered;
    String pend, rec, del;

    @Override
    public void onBackPressed() {
        new acceptDialog_admin().show(getSupportFragmentManager(),"quit?");
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_dashboard);
        firebasenotificationregister();

        Toolbar toolbar = findViewById(R.id.mechanic_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //typecasting
        mechanicnameview = findViewById(R.id.mechanic_name_view);

        currdateshow = findViewById(R.id.todaydate_mechanicdash);
        pendingq = findViewById(R.id.pending_mechanicdash);
        received = findViewById(R.id.received_mechanicdash);
        delivered = findViewById(R.id.delivered_mechanicdash);

        mechanicnameview.setText(login.username);

        pending = findViewById(R.id.pending_mechanic);
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), mechanic_pending_list.class));

            }
        });
        String date = (String) android.text.format.DateFormat.
                format("yyyy-MM-dd", Calendar.getInstance().getTime());
        currdateshow.setText(date);

        new getcount().execute();
        new mechanicdashboard().execute();
    }
    public void firebasenotificationregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("mynotifications", "mynotifications", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("mechanic")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_forgotpass:
                startActivity(new Intent(getApplicationContext(), changepassword.class));
                return true;
            case R.id.menu_aboutus:
                Toast.makeText(this, "about us", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class mechanicdashboard extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            // progressDialog= ProgressDialog.show(getApplicationContext(), "", "Loading your orders..", true);
            db_url = "http://peitahari.000webhostapp.com/mechanic_dashboard.php";

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
                String data_string = URLEncoder.encode("mechanicid", "UTF-8") + "=" + URLEncoder.encode(login.userid, "UTF-8");

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

                String json;

                InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
                int size = stream.available();
                byte[] buffer1 = new byte[size];
                stream.read(buffer1);
                stream.close();

                json = new String(buffer1, "UTF-8");
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i <= jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("id") != null) {
                        uid = jsonObject.getString("uid");
                        itemtype = jsonObject.getString("itemtype");
                        date = jsonObject.getString("delivereddate");
                        status = jsonObject.getString("status");


                        //array list

                        UID.add(uid);
                        ITEMTYPE.add(itemtype);
                        DATE.add(date);
                        STATUS.add(status);
                    }
                }


                return null;


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
            //  progressDialog.dismiss();
            RecyclerView recyclerView = findViewById(R.id.recycler_mechanic_delivered);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(new adapterMechanicDelivered(UID, ITEMTYPE, DATE, STATUS));

        }
    }

    public class getcount extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            // progressDialog= ProgressDialog.show(getApplicationContext(), "", "Loading your orders..", true);
            db_url = "http://peitahari.000webhostapp.com/qqq.php";

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
                String data_string = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(login.userid, "UTF-8");

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

                String json;

                InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
                int size = stream.available();
                byte[] buffer1 = new byte[size];
                stream.read(buffer1);
                stream.close();

                json = new String(buffer1, "UTF-8");
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i <= jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("status") != null && jsonObject.getString("status").equals("0")) {

                        pend = jsonObject.getString("COUNT(*)");


                    }
                    if (jsonObject.getString("status") != null && jsonObject.getString("status").equals("1")) {

                        rec = jsonObject.getString("COUNT(*)");


                    }
                    if (jsonObject.getString("status") != null &&jsonObject.getString("status").equals("2") || jsonObject.getString("status").equals("3")) {

                        del = jsonObject.getString("COUNT(*)");


                    }
                }


                return null;


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
            if (pend != null) {
                pendingq.setText(pend);
            }
            if (rec != null) {
                received.setText(rec);
            }
            if (del != null) {
                delivered.setText(del);
            }

        }
    }
}
