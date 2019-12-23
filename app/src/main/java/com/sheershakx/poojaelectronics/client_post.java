package com.sheershakx.poojaelectronics;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Locale;
import java.util.UUID;

public class client_post extends AppCompatActivity {
    Button post;
    TextView uid, date;
    Spinner itemtype;
    EditText spec, serialno, sizeno, model;
    RadioGroup radioGroup;

    String radiotext;

    String itemgroup;
    ArrayList<String> ItemGroup=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_post);

        new getitemgroup().execute();


        //typecasting
        post = findViewById(R.id.post_btn_client);
        uid = findViewById(R.id.UID);
        date = findViewById(R.id.date_clientpost);

        itemtype = findViewById(R.id.itemtype_clientpost);
        spec = findViewById(R.id.specification_clientpost);

        serialno = findViewById(R.id.serialno_clientpost);
        sizeno = findViewById(R.id.size_clientpost);
        model = findViewById(R.id.model_clientpost);


        radioGroup = findViewById(R.id.radiogroup_client);


        LocalDateTime currdate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currdate = LocalDateTime.now();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String Date = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(currdate);
            date.setText(Date);

        }

        //generating randopm UID
        String randomid = UUID.randomUUID().toString();
        String trimmedrandiomuid = randomid.substring(0, 7);
        uid.setText(trimmedrandiomuid);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                radiotext = radioButton.getText().toString().trim();
            }
        });

        //post item button action
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Uid = uid.getText().toString();
                String currdate = date.getText().toString();
                String Itemtype = itemtype.getSelectedItem().toString().trim();
                String Serialno = serialno.getText().toString().trim();
                String Sizeno = sizeno.getText().toString().trim();
                String Modelno = model.getText().toString().trim();
                String specification = spec.getText().toString();


                if (!TextUtils.isEmpty(Uid) && !TextUtils.isEmpty(currdate) && !TextUtils.isEmpty(specification) && !TextUtils.isEmpty(radiotext) && !TextUtils.isEmpty(Itemtype)) {

                        new postproblem().execute(Uid, currdate, specification, radiotext, Itemtype,Serialno,Sizeno,Modelno);
                } else
                    Toast.makeText(client_post.this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();

            }
        });
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
                        URLEncoder.encode("clientid", "UTF-8") + "=" + URLEncoder.encode(login.userid, "UTF-8");


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
}
