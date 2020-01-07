package com.sheershakx.poojaelectronics;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class mechanicDelivered extends AppCompatActivity {
    ProgressDialog progressDialog;
    Spinner mechdelv;
    String mstatus = null;
    String clientid, uid, itemtype, date, name, status;
    ArrayList<String> ClientID = new ArrayList<String>();
    ArrayList<String> UID = new ArrayList<String>();
    ArrayList<String> Date = new ArrayList<String>();
    ArrayList<String> ItemType = new ArrayList<String>();
    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<String> Status = new ArrayList<String>();
    ArrayList<String> MStatus = new ArrayList<String>();

    String id, mechanicname;
    ArrayList<String> ID = new ArrayList<String>();
    ArrayList<String> Mechanicname = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_delivered);

        mechdelv = findViewById(R.id.spinner_mechdelv);
        new getmechanicname().execute();

        mechdelv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClientID.clear();
                UID.clear();
                ItemType.clear();
                Date.clear();
                Name.clear();
                Status.clear();

                Integer idtosend = Integer.parseInt(ID.get(position));
                new mdelivered().execute(Integer.toString(idtosend));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class mdelivered extends AsyncTask<String, String, String> {
        String db_url;

        @Override
        protected void onPreExecute() {
            db_url = "http://peitahari.000webhostapp.com/mdelivered.php";
            progressDialog = ProgressDialog.show(mechanicDelivered.this, "", "Loading orders..", true);

        }

        @Override
        protected String doInBackground(String... args) {
            String idd;
            idd = args[0];

            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("mechanicid", "UTF-8") + "=" + URLEncoder.encode(idd, "UTF-8");

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
                        status = jsonObject.getString("adminstatus");
                        mstatus = "a";

                        //array list

                        ClientID.add(clientid);
                        UID.add(uid);
                        ItemType.add(itemtype);
                        Date.add(date);
                        Name.add(name);
                        Status.add(status);
                        MStatus.add(mstatus);

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
            progressDialog.dismiss();
            RecyclerView recyclerView = findViewById(R.id.recycler_mdelivered);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(new adapterAdminReport(ClientID, UID, ItemType, Date, Name, Status, MStatus));

        }
    }

    public class getmechanicname extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            // progressDialog= ProgressDialog.show(getApplicationContext(), "", "Loading your orders..", true);
            db_url = "http://peitahari.000webhostapp.com/getmechanicname.php";

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
//                String data_string = URLEncoder.encode("mechanicid", "UTF-8") + "=" + URLEncoder.encode(idd, "UTF-8");
//
//                bufferedWriter.write(data_string);
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
                        id = jsonObject.getString("id");
                        mechanicname = jsonObject.getString("name");


                        //array list

                        ID.add(id);
                        Mechanicname.add(mechanicname);

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
            setspinnerdata();


        }

        private void setspinnerdata() {
            String[] mStringArray = new String[Mechanicname.size()];
            mStringArray = Mechanicname.toArray(mStringArray);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mechanicDelivered.this, android.R.layout.simple_spinner_item, mStringArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mechdelv.setAdapter(adapter);

        }
    }

}
