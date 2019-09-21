package com.sohojbazar.sohojbazar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Auth extends AppCompatActivity {

    private String verificationid;
    private String name;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText codefield;
    private Button continuebutton;
    private TextView textView;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        codefield = findViewById(R.id.codefield);
        continuebutton=findViewById(R.id.continuebutton);
        textView=findViewById(R.id.textfield);
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        String phonenumber = getIntent().getStringExtra("phonenumber");
        String name=getIntent().getStringExtra("name");
        sendVerificationCode(phonenumber);

        textView.setText("আপনার মোবাইল নম্বরে("+phonenumber+") একটি 6 সঙ্খার ভেরিফিকেশান কোড গিয়েছে। অনুগ্রহ করে সেটি লিখুন।");

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = codefield.getText().toString().trim();

                if ((code.isEmpty() || code.length() < 6)){

                    codefield.setError("Enter code...");
                    codefield.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });
    }

    private void verifyCode(String code){
        Log.e("code",code);
        Log.e("verification code",verificationid);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                            String id=currentFirebaseUser.getUid();
                            String name=getIntent().getStringExtra("name");
                            String phonenumber=getIntent().getStringExtra("phonenumber");

                            adduser au=new adduser(id,phonenumber,name);
                            databaseReference.child(id).setValue(au);

                            Intent intent = new Intent(Auth.this, HomePage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(Auth.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("failed",task.getException().getMessage());
                        }
                    }

                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Auth.this, e.getMessage(),Toast.LENGTH_LONG).show();
            Log.e("failed",e.getMessage());
        }
    };

    class adduser{
        String userId;
        String userPhonenumber;
        String username;
        public adduser(){

        }

        public adduser(String userId, String userPhonenumber, String username) {
            this.userId = userId;
            this.userPhonenumber = userPhonenumber;
            this.username = username;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserPhonenumber() {
            return userPhonenumber;
        }

        public String getUsername() {
            return username;
        }
    }
}
