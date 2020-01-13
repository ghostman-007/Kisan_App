package com.ghostman.kisanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private String verificationID;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private EditText etCode;
    private Button btVerify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        String phoneNumber = getIntent().getStringExtra("PHONENUMBER");
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.pb_AVP);
        etCode = findViewById(R.id.et_code_AVP);
        btVerify = findViewById(R.id.button_verify_AVP);

        sendVerificationCode(phoneNumber);

        findViewById(R.id.button_verify_AVP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = etCode.getText().toString().trim();
                if(code.isEmpty() || code.length() < 6) {
                    etCode.setError("Enter Code");
                    etCode.requestFocus();
                    return;
                }
                btVerify.setEnabled(false);
                verifyCode(code);
            }
        });
    }
    
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }else {
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            btVerify.setEnabled(true);
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
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
            Toast.makeText(VerifyPhoneActivity.this, "CODE SENT...", Toast.LENGTH_SHORT).show();
             verificationID = s;
         }

         @Override
         public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(VerifyPhoneActivity.this, "ON VERIFICATION COMPLETED...", Toast.LENGTH_SHORT).show();
            String code = phoneAuthCredential.getSmsCode();
            if(code != null) {
                etCode.setText(code);
                btVerify.setEnabled(false);
                verifyCode(code);
            } else {
                Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
         }

         @Override
         public void onVerificationFailed(FirebaseException e) {
             Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
             btVerify.setEnabled(true);
         }
     };
}
