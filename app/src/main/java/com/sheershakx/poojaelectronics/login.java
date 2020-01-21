package com.sheershakx.poojaelectronics;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

public class login extends AppCompatActivity {
    EditText mobile, password;

    Button loginbtn;
    public static final String CHANNEL_1_ID = "channel1";
    public static String usertype;
    public static String userid;
    public static String username;
    public static String notify;
    public static String nepalidate;

    NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      //  createNotificationChannels();
        notificationManager = NotificationManagerCompat.from(this);
        //type casting
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.login);

        getNepaliDate();

        //signup process
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // sendOnChannel1();
                String Mobile = mobile.getText().toString();
                String Password = password.getText().toString();

                if (!TextUtils.isEmpty(Mobile) && !TextUtils.isEmpty(Password)) {
                    new dologin().execute(Mobile, Password);

                } else
                    Toast.makeText(login.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //function to convert today's english date into nepali date

    private void getNepaliDate() {
        String year = (String) android.text.format.DateFormat.
                format("yyyy", Calendar.getInstance().getTime());

        String month = (String) android.text.format.DateFormat.
                format("MM", Calendar.getInstance().getTime());

        String date = (String) android.text.format.DateFormat.
                format("dd", Calendar.getInstance().getTime());

        com.sheershakx.poojaelectronics.date datex=new date();                           //date converter calling
      String[] recvdate=  datex.conversion(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(date));
       nepalidate=recvdate[0]+"-"+recvdate[1]+"-"+recvdate[2];
    }

    private void createNotificationChannels() {     //disabled becauase already used in other activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }


    }


    public void sendOnChannel1() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
               .setSmallIcon(R.drawable.poojaicon)
                .setContentTitle("New Notification")
                .setContentText("This is a new notifiation message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);


    }


    public class dologin extends AsyncTask<String, String, String> {
        String db_url;

        @Override
        protected void onPreExecute() {

            db_url = "http://peitahari.000webhostapp.com/login.php";

        }

        @Override
        protected String doInBackground(String... params) {
            String mobile, password;
            mobile = params[0];
            password = params[1];

            try {
                URL url = new URL(db_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
                usertype = jsonObject.getString("usertype");
                userid = jsonObject.getString("userid");
                username = jsonObject.getString("username");
                if (jsonObject.getString("notify") != null) {
                    notify = jsonObject.getString("notify");
                }
//
                return data;


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
            if (s != null && s.contains("Successful")) {
                Toast.makeText(login.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                if (usertype != null && usertype.equals("0")) {

                    startActivity(new Intent(getApplicationContext(), admin_dashboard.class));
                    finish();

                } else if (usertype != null && usertype.equals("1")) {

                    startActivity(new Intent(getApplicationContext(), mechanic_dashboard.class));
                    finish();

                } else if (usertype != null && usertype.equals("2")) {

                    startActivity(new Intent(getApplicationContext(), client_dashboard.class));
                    finish();
                } else Toast.makeText(login.this, "User TYpe not found", Toast.LENGTH_SHORT).show();


            } else
                Toast.makeText(login.this, "Sorry,Login failed", Toast.LENGTH_SHORT).show();


        }
    }
}
