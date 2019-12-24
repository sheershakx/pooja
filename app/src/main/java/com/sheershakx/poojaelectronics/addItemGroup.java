package com.sheershakx.poojaelectronics;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class addItemGroup extends AppCompatActivity {
    EditText itemname;
    Button additem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_group);

        itemname = findViewById(R.id.itemname_addgroup);
        additem = findViewById(R.id.add_itemgroup);

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = itemname.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                   new addditemgroup().execute(name);
                   additem.setEnabled(false);
                } else Toast.makeText(addItemGroup.this, "Please enter item group name", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class addditemgroup extends AsyncTask<String, String, String> {
        String db_url;


        @Override
        protected void onPreExecute() {

            db_url = "http://peitahari.000webhostapp.com/insert_itemgroup.php";

        }

        @Override
        protected String doInBackground(String... args) {

            String itemname = args[0];


            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("itemgroup", "UTF-8") + "=" + URLEncoder.encode(itemname, "UTF-8");
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
           itemname.setText("");
           additem.setEnabled(true);
        }
    }
}
