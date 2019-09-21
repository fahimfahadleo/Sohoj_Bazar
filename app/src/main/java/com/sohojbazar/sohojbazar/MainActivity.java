package com.sohojbazar.sohojbazar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText namefield,phonenumber;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        namefield=findViewById(R.id.namefield);
        phonenumber=findViewById(R.id.phonenumberfield);
        login=findViewById(R.id.loginbutton);



        //  startActivity(new Intent(this,HomePage.class));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String number = phonenumber.getText().toString().trim();
                String name=namefield.getText().toString();

                if (number.isEmpty() || number.length() < 10) {
                    phonenumber.setError("অনুগ্রহ করে সঠিক নাম্বার প্রদান করুন");
                    phonenumber.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(name)){
                    namefield.setError("অনুগ্রহ করে আপনার নাম লিখুন");
                }

                String phonenumber = "+88"+ number;


                Intent intent = new Intent(MainActivity.this, Auth.class);
                intent.putExtra("phonenumber", phonenumber);
                intent.putExtra("name",name);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, HomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }

    }
}
