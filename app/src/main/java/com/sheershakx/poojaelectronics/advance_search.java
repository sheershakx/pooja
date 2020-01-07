package com.sheershakx.poojaelectronics;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class advance_search extends AppCompatActivity {
    String cid,uid,Date,itemType;
    ArrayList<String> CID=new ArrayList<String>();
    ArrayList<String> UID=new ArrayList<String>();
    ArrayList<String> DATE=new ArrayList<String>();
    ArrayList<String> ITEMTYPE=new ArrayList<String>();
    ProgressDialog progressDialog;

    String id, clientname;
    ArrayList<String> ID = new ArrayList<String>();
    ArrayList<String> ClientName = new ArrayList<String>();

    Spinner searchtype, searchclient;
    Button searchbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);
        //typecasting

        searchtype = findViewById(R.id.search_type);
        searchclient = findViewById(R.id.search_client);
        searchbtn = findViewById(R.id.search_searchbtn);

        new getclientname().execute();


        String typearray[] = {"Accepted", "Rejected", "Forwarded to mechanic", "Returned"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(advance_search.this, android.R.layout.simple_spinner_item, typearray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchtype.setAdapter(adapter);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CID.clear();
                UID.clear();
                ITEMTYPE.clear();
                DATE.clear();

                String typeid=null;
                String type = searchtype.getSelectedItem().toString();
                Integer position = searchclient.getSelectedItemPosition();
                Integer idtosend = Integer.parseInt(ID.get(position));
                if (type != null && type.equals("Accepted")) {
                    typeid="1"; //in api check for userstatus=1
                }
                if (type != null && type.equals("Rejected")) {
                    typeid="4"; //check userstatus=4
                }
                if (type != null && type.equals("Forwarded to mechanic")) {
                    typeid="0";
                }
                if (type != null && type.equals("Returned")) {
                    typeid="3"; //userstatus=3(received by client)
                }
                new advancedSearch().execute(typeid,Integer.toString(idtosend));
            }
        });
    }

    public class getclientname extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(advance_search.this, "", "Loading your orders..", true);
            db_url = "http://peitahari.000webhostapp.com/getclientname.php";

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
                        clientname = jsonObject.getString("name");


                        //array list

                        ID.add(id);
                        ClientName.add(clientname);

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
            progressDialog.dismiss();
            String[] mStringArray = new String[ClientName.size()];
            mStringArray = ClientName.toArray(mStringArray);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(advance_search.this, android.R.layout.simple_spinner_item, mStringArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            searchclient.setAdapter(adapter);

        }
    }

    public class advancedSearch extends AsyncTask<String, String, String> {
        String db_url;

        @Override
        protected void onPreExecute() {
            db_url = "http://peitahari.000webhostapp.com/advancedSearch.php";
            progressDialog = ProgressDialog.show(advance_search.this, "", "Loading orders..", true);

        }

        @Override
        protected String doInBackground(String... args) {
            String type,clientid;
            type = args[0];
            clientid = args[1];

            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&" +
                        URLEncoder.encode("clientid", "UTF-8") + "=" + URLEncoder.encode(clientid, "UTF-8");

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
                        cid = jsonObject.getString("clientid");
                        uid = jsonObject.getString("uid");
                        itemType = jsonObject.getString("itemtype");
                        Date = jsonObject.getString("date");


                        //array list

                        CID.add(cid);
                        UID.add(uid);
                        ITEMTYPE.add(itemType);
                        DATE.add(Date);

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
            RecyclerView recyclerView = findViewById(R.id.recyclerview_advancesearch);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(new adapterSearch(CID,UID,DATE,ITEMTYPE));

        }
    }
}
