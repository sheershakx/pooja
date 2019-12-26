package com.sheershakx.poojaelectronics;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class change_userstatus extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... args) {
        String id = args[0];
        String status = args[1];
        try {

            URL url = new URL("http://peitahari.000webhostapp.com/change_active.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data_string = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                    URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");
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


            return null;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
