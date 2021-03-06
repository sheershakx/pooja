package com.sheershakx.poojaelectronics;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.UUID;

public class client_post extends AppCompatActivity {
    Button post;
    TextView uid, date;
    Spinner itemtype, clientname;
    CheckBox checkBox;
    EditText spec, serialno, sizeno, model, problem;
    RadioGroup radioGroup;
    Button addnew;
    LinearLayout expandableLayout;
    String radiotext;
    TextView groupId;

    String itemgroup, id, Clientname;
    ArrayList<String> ItemGroup = new ArrayList<String>();
    ArrayList<String> CCID = new ArrayList<String>();
    ArrayList<String> ClientName = new ArrayList<String>();

    //
    String grpUID;


    ArrayList<String> ITEMTYPE = new ArrayList<String>();
    ArrayList<String> UID = new ArrayList<String>();
    ArrayList<String> CURRDATE = new ArrayList<String>();
    ArrayList<String> SERIALNO = new ArrayList<String>();
    ArrayList<String> SIZENO = new ArrayList<String>();
    ArrayList<String> MODEL = new ArrayList<String>();
    ArrayList<String> PROBLEM = new ArrayList<String>();
    ArrayList<String> SPEC = new ArrayList<String>();
    ArrayList<String> RADIOTEXT = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_post);


        //typecasting
        post = findViewById(R.id.post_btn_client);
        uid = findViewById(R.id.UID);
        date = findViewById(R.id.date_clientpost);

        itemtype = findViewById(R.id.itemtype_clientpost);
        clientname = findViewById(R.id.spinner_clientnames);
        // hiding clientname box for client
        clientname.setVisibility(View.GONE);
        spec = findViewById(R.id.specification_clientpost);
        problem = findViewById(R.id.problem_clientpost);

        serialno = findViewById(R.id.serialno_clientpost);
        sizeno = findViewById(R.id.size_clientpost);
        model = findViewById(R.id.model_clientpost);

        expandableLayout = findViewById(R.id.expandableLayout);
        addnew = findViewById(R.id.btn_addnew);

        radioGroup = findViewById(R.id.radiogroup_client);
        checkBox = findViewById(R.id.checkbox);

        groupId = findViewById(R.id.grpid);


        if (login.usertype != null && login.usertype.equals("0")) {
            clientname.setVisibility(View.VISIBLE);
        } else {
            clientname.setVisibility(View.GONE);
        }
//////////////////////
        new getitemgroup().execute();
        new getclientname().execute();
/////////////////////////////


        //generating random grp id
        String randomidGRP = UUID.randomUUID().toString();
        grpUID = randomidGRP.substring(0, 5);


        //generating randopm UID
        String randomid = UUID.randomUUID().toString();
        String trimmedrandiomuid = randomid.substring(0, 7);
        uid.setText(trimmedrandiomuid);
        date.setText(login.nepalidate);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                radiotext = radioButton.getText().toString().trim();
            }
        });

        //expand btn on click listener
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Uid = uid.getText().toString();
                String currdate = date.getText().toString();
                String Itemtype = itemtype.getSelectedItem().toString().trim();
                String Serialno = serialno.getText().toString().trim();
                String Sizeno = sizeno.getText().toString().trim();
                String Modelno = model.getText().toString().trim();
                String Problem = problem.getText().toString().trim();
                String specification = spec.getText().toString();

                if (!TextUtils.isEmpty(radiotext) && !TextUtils.isEmpty(Uid) && !TextUtils.isEmpty(Problem) && !TextUtils.isEmpty(currdate) && !TextUtils.isEmpty(specification) && !TextUtils.isEmpty(radiotext) && !TextUtils.isEmpty(Itemtype)) {

                    ITEMTYPE.add(Itemtype);
                    UID.add(Uid);
                    CURRDATE.add(currdate);
                    SERIALNO.add(Serialno);
                    SIZENO.add(Sizeno);
                    MODEL.add(Modelno);
                    PROBLEM.add(Problem);
                    SPEC.add(specification);
                    RADIOTEXT.add(radiotext);

                    displayintablebelow();
                    //generating random UID
                    String randomid = UUID.randomUUID().toString();
                    String trimmedrandiomuid = randomid.substring(0, 7);
                    uid.setText(trimmedrandiomuid);

                    serialno.setText("");
                    sizeno.setText("");
                    model.setText("");
                    spec.setText("");
                    problem.setText("");

                } else
                    Toast.makeText(client_post.this, "* are compulsory", Toast.LENGTH_SHORT).show();

            }

        });
        //post item button action
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = clientname.getSelectedItemPosition();
                Integer idtosend = Integer.parseInt(CCID.get(position));


                if (PROBLEM.size() >= 2) {

                    for (int i = 0; i < PROBLEM.size(); i++) {
                        if (login.usertype != null && login.usertype.equals("2")) {
                            new postproblem().execute(UID.get(i), CURRDATE.get(i), SPEC.get(i), RADIOTEXT.get(i), ITEMTYPE.get(i), SERIALNO.get(i), SIZENO.get(i), MODEL.get(i), PROBLEM.get(i), grpUID, login.userid);
                        } else if (login.usertype != null && login.usertype.equals("0")) {
                            new postproblem().execute(UID.get(i), CURRDATE.get(i), SPEC.get(i), RADIOTEXT.get(i), ITEMTYPE.get(i), SERIALNO.get(i), SIZENO.get(i), MODEL.get(i), PROBLEM.get(i), grpUID, Integer.toString(idtosend));

                        }
                    }
                }
                else if (PROBLEM.size() ==1) {

                    for (int i = 0; i < PROBLEM.size(); i++) {
                        if (login.usertype != null && login.usertype.equals("2")) {
                            new postproblem().execute(UID.get(i), CURRDATE.get(i), SPEC.get(i), RADIOTEXT.get(i), ITEMTYPE.get(i), SERIALNO.get(i), SIZENO.get(i), MODEL.get(i), PROBLEM.get(i), "0", login.userid);
                        } else if (login.usertype != null && login.usertype.equals("0")) {
                            new postproblem().execute(UID.get(i), CURRDATE.get(i), SPEC.get(i), RADIOTEXT.get(i), ITEMTYPE.get(i), SERIALNO.get(i), SIZENO.get(i), MODEL.get(i), PROBLEM.get(i), "0", Integer.toString(idtosend));

                        }
                    }
                }
                else Toast.makeText(client_post.this, "Please add at least 1 item to Save", Toast.LENGTH_SHORT).show();


            }

        });
    }

    public void displayintablebelow() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview_postclient);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new adapterPostTable(ITEMTYPE, SERIALNO, SIZENO, MODEL, PROBLEM, SPEC));

    }

    public class getclientname extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {
            // progressDialog = ProgressDialog.show(advance_search.this, "", "Loading your orders..", true);
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
                        Clientname = jsonObject.getString("name");


                        //array list

                        CCID.add(id);
                        ClientName.add(Clientname);

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
            //  progressDialog.dismiss();
            String[] mStringArray = new String[ClientName.size()];
            mStringArray = ClientName.toArray(mStringArray);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(client_post.this, android.R.layout.simple_spinner_item, mStringArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            clientname.setAdapter(adapter);

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

                    ItemGroup.add(itemgroup);


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
            String[] mStringArray = new String[ItemGroup.size()];
            mStringArray = ItemGroup.toArray(mStringArray);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(client_post.this, android.R.layout.simple_spinner_item, mStringArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            itemtype.setAdapter(adapter);

        }
    }

    public class postproblem extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {

            db_url = "http://peitahari.000webhostapp.com/client_post.php";

        }

        @Override
        protected String doInBackground(String... args) {

            String uid = args[0];
            String date = args[1];
            String spec = args[2];
            String radiotext = args[3];
            String itemtype = args[4];
            String serialno = args[5];
            String size = args[6];
            String model = args[7];
            String problem = args[8];
            String groupid = args[9];
            String userid = args[10];

            try {

                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&" +
                        URLEncoder.encode("itemtype", "UTF-8") + "=" + URLEncoder.encode(itemtype, "UTF-8") + "&" +
                        URLEncoder.encode("spec", "UTF-8") + "=" + URLEncoder.encode(spec, "UTF-8") + "&" +
                        URLEncoder.encode("cost", "UTF-8") + "=" + URLEncoder.encode(radiotext, "UTF-8") + "&" +
                        URLEncoder.encode("serialno", "UTF-8") + "=" + URLEncoder.encode(serialno, "UTF-8") + "&" +
                        URLEncoder.encode("size", "UTF-8") + "=" + URLEncoder.encode(size, "UTF-8") + "&" +
                        URLEncoder.encode("model", "UTF-8") + "=" + URLEncoder.encode(model, "UTF-8") + "&" +
                        URLEncoder.encode("clientProblem", "UTF-8") + "=" + URLEncoder.encode(problem, "UTF-8") + "&" +
                        URLEncoder.encode("groupid", "UTF-8") + "=" + URLEncoder.encode(groupid, "UTF-8") + "&" +
                        URLEncoder.encode("clientid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");


                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();

                return null;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(client_post.this, "Posted", Toast.LENGTH_SHORT).show();
            finish();

        }
    }
}
