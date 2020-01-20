package com.sheershakx.poojaelectronics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;

public class multiple_pending_view extends AppCompatActivity {
    ProgressDialog progressDialog;
    String  groupid;

    String clientid,uid,itemtype,date,name,status,astatus,mstatus;
    ArrayList<String> ClientID=new ArrayList<String>();
    ArrayList<String> UID=new ArrayList<String>();
    ArrayList<String> Date=new ArrayList<String>();
    ArrayList<String> ItemType=new ArrayList<String>();
    ArrayList<String> Name=new ArrayList<String>();
    ArrayList<String> Status=new ArrayList<String>();
    ArrayList<String> Astatus=new ArrayList<String>();
    ArrayList<String> Mstatus=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_pending_view);

        getincomingintent();
    }

    private void getincomingintent() {
        Intent intent = getIntent();
        if (intent.hasExtra("groupid")) {
            groupid = intent.getStringExtra("groupid");

            new getfilteredpost().execute();
        }
    }

    public class getfilteredpost extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            progressDialog= ProgressDialog.show(multiple_pending_view.this,"","Loading orders..",true);
            db_url = "http://peitahari.000webhostapp.com/admin_pending_multiple.php";

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
                String data_string = URLEncoder.encode("groupid", "UTF-8") + "=" + URLEncoder.encode(groupid, "UTF-8");
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
                    if (jsonObject.getString("uid") != null) {
                        clientid = jsonObject.getString("clientid");
                        uid = jsonObject.getString("uid");
                        itemtype = jsonObject.getString("itemtype");
                        date = jsonObject.getString("date");
                        name = jsonObject.getString("name");
                        status = jsonObject.getString("userstatus");
                        astatus = jsonObject.getString("adminstatus");
                        mstatus = jsonObject.getString("mechanicstatus");




                        //array list

                        ClientID.add(clientid);
                        UID.add(uid);
                        ItemType.add(itemtype);
                        Date.add(date);
                        Name.add(name);
                        Status.add(status);
                        Astatus.add(astatus);
                        Mstatus.add(mstatus);


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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            RecyclerView recyclerView = findViewById(R.id.recyclerview_multipleView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(new adapterAdminPending_multiple(ClientID, UID, ItemType,Date,Name,Status,Astatus,Mstatus));
            progressDialog.dismiss();


        }
    }
}
