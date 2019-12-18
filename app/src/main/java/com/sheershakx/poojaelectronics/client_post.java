package com.sheershakx.poojaelectronics;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class client_post extends AppCompatActivity {
    Button post;
    TextView uid, date;
    Spinner itemtype;
    EditText spec;
    RadioGroup radioGroup;

    String radiotext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_post);

        //typecasting
        post = findViewById(R.id.post_btn_client);
        uid = findViewById(R.id.UID);
        date = findViewById(R.id.date_clientpost);

        itemtype = findViewById(R.id.itemtype_clientpost);
        spec = findViewById(R.id.specification_clientpost);

        radioGroup = findViewById(R.id.radiogroup_client);

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
                String Uid=uid.getText().toString();
                String currdate=date.getText().toString();
                String Itemtype=itemtype.getSelectedItem().toString().trim();
                String specification=spec.getText().toString();
                
                if (!TextUtils.isEmpty(Uid) && !TextUtils.isEmpty(currdate) && !TextUtils.isEmpty(Itemtype) && !TextUtils.isEmpty(specification) && !TextUtils.isEmpty(radiotext)){
                    //call post api here
                } else Toast.makeText(client_post.this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();
                
            }
        });
    }
}
