package com.ghostman.kisanapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText et_number;
    private Button bt_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        setContentView(R.layout.activity_login);
        et_number = findViewById(R.id.et_number_login);
        bt_login = findViewById(R.id.bt_login_login);

        //et_number.setText("+91 ");
        //et_number.setSelection(4);

        findViewById(R.id.bt_login_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = et_number.getText().toString().trim();
                number = number.replace(" ","");

                if(number.isEmpty() || number.length() < 10) {
                    et_number.setError("Valid number is Required");
                    et_number.requestFocus();
                    return;
                }

                String phoneNumber = "+91" + number;

                Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("PHONENUMBER", phoneNumber);
                startActivity(intent);
            }

        });

        et_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_GO) {
                    bt_login.performClick();
                    return true;
                }
                return false;
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
