package com.sheershakx.poojaelectronics;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

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
    String cid, uid, Date, itemType, mechanicname, itemgroup;
    ArrayList<String> CID = new ArrayList<String>();
    ArrayList<String> UID = new ArrayList<String>();
    ArrayList<String> DATE = new ArrayList<String>();
    ArrayList<String> ITEMTYPE = new ArrayList<String>();
    ArrayList<String> MechanicName = new ArrayList<String>();
    ArrayList<String> Itemgroup = new ArrayList<String>();
    ProgressDialog progressDialog;

    String id, clientname;
    ArrayList<String> CCID = new ArrayList<String>();
    ArrayList<String> MID = new ArrayList<String>();
    ArrayList<String> ClientName = new ArrayList<String>();

    AutoCompleteTextView searchmechanic, searchclient;
    AutoCompleteTextView searchitem;
    Button searchbtn;

    String clientpos, mechanicpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);
        //typecasting

        searchmechanic = findViewById(R.id.search_mechanic);
        searchclient = findViewById(R.id.search_client);
        searchitem = findViewById(R.id.search_itemtype);
        searchbtn = findViewById(R.id.search_searchbtn);

        new getclientname().execute();
        new getmechanicname().execute();
        new getitemgroup().execute();


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CID.clear();
                UID.clear();
                ITEMTYPE.clear();
                DATE.clear();

                String selectedClient = searchclient.getText().toString().trim();
                String selectedMechanic = searchmechanic.getText().toString().trim();

                //finding corresponding array position for selected text in autocomplete textview
                for (int i = 0; i < CCID.size(); i++) {                     //for client
                    if (selectedClient.equals(ClientName.get(i))) {
                        clientpos = CCID.get(i);
                    }
                }


                for (int i = 0; i < MID.size(); i++) {                     //for mechanic
                    if (selectedMechanic.equals(MechanicName.get(i))) {
                        mechanicpos = MID.get(i);
                    }
                }

                //for client to client id conversion
                //    Integer position = searchclient.getSelectedItemPosition();
                //   Integer idtosend = Integer.parseInt(CCID.get(position));

                //for mechanic to mechanic id conversion
                //   Integer position_new = searchmechanic.getSelectedItemPosition();
                //   Integer idtosend_new = Integer.parseInt(MID.get(position_new));

                //get itemtype selected string and trim
                String selectedItem = searchitem.getText().toString().trim();
                if (clientpos != null && mechanicpos != null && selectedItem != null && !TextUtils.isEmpty(clientpos) && !TextUtils.isEmpty(mechanicpos)  && !TextUtils.isEmpty(selectedItem) ) {
                    new advancedSearch().execute(mechanicpos, clientpos, selectedItem);
                   // Toast.makeText(advance_search.this, clientpos+" && "+ mechanicpos, Toast.LENGTH_LONG).show();
                }
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

                        CCID.add(id);
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
            setspinnerdata_client();


        }

        private void setspinnerdata_client() {
            progressDialog.dismiss();
            String[] mStringArray = new String[ClientName.size()];
            mStringArray = ClientName.toArray(mStringArray);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(advance_search.this, android.R.layout.simple_spinner_item, mStringArray);
            searchclient.setAdapter(adapter);
            searchclient.setThreshold(1);
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
//                String data_string = URLEncoder.encode("mechanicid", "UTF-8") + "=" + URLEncoder.encode(login.userid, "UTF-8");
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

                        MID.add(id);
                        MechanicName.add(mechanicname);

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
            setspinnerdata_mechanic();


        }

        private void setspinnerdata_mechanic() {
            String[] mStringArray = new String[MechanicName.size()];
            mStringArray = MechanicName.toArray(mStringArray);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(advance_search.this, android.R.layout.simple_spinner_item, mStringArray);
            searchmechanic.setAdapter(adapter);
            searchmechanic.setThreshold(1);

        }
    }

    public class getitemgroup extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            // progressDialog= ProgressDialog.show(getApplicationContext(), "", "Loading your orders..", true);
            db_url = "http://peitahari.000webhostapp.com/getitemgroup.php";

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
//                String data_string = URLEncoder.encode("mechanicid", "UTF-8") + "=" + URLEncoder.encode(login.userid, "UTF-8");
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

                    itemgroup = jsonObject.getString("groupname");

                    Itemgroup.add(itemgroup);


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
            setspinnerdata_item();


        }

        private void setspinnerdata_item() {
            String[] mStringArray = new String[Itemgroup.size()];
            mStringArray = Itemgroup.toArray(mStringArray);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(advance_search.this, android.R.layout.simple_spinner_item, mStringArray);
            searchitem.setAdapter(adapter);
            searchitem.setThreshold(1);

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
            String mechanicid, clientid, itemname;
            mechanicid = args[0];
            clientid = args[1];
            itemname = args[2];

            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("mechanicid", "UTF-8") + "=" + URLEncoder.encode(mechanicid, "UTF-8") + "&" +
                        URLEncoder.encode("itemtype", "UTF-8") + "=" + URLEncoder.encode(itemname, "UTF-8") + "&" +
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
            recyclerView.setAdapter(new adapterSearch(CID, UID, DATE, ITEMTYPE));

        }
    }
}
