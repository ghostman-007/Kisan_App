package com.ghostman.kisanapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    public static final String MY_SHARED_PREFERENCE = "shared_Preference";
    private static String TAG = VerifyPhoneActivity.class.getSimpleName();
    private PlacesAutoCompleteAdapter mAdapter;
    HandlerThread mHandlerThread;
    Handler mThreadHandler;

    AppLocationService appLocationService;


    private String verificationID;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private EditText etCode;
    private Button btVerify, btAddress;
    private EditText etName;
    private AutoCompleteTextView actvLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAdapter = new PlacesAutoCompleteAdapter(getApplicationContext(), R.layout.dropdown_menu_popup_item);

        String phoneNumber = getIntent().getStringExtra("PHONENUMBER");
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.pb_AVP);
        etCode = findViewById(R.id.et_code_AVP);
        btVerify = findViewById(R.id.button_verify_AVP);
        btAddress = findViewById(R.id.bt_get_address_avp);
        etName = findViewById(R.id.et_name_avp);
        actvLocation = findViewById(R.id.actv_location_avp);
        sendVerificationCode(phoneNumber);

        // TODO: Add all required STATES
        String[] STATES = new String[] {"Assam", "Delhi", "Haryana", "Uttar Pradesh" };
        ArrayAdapter<String> state_adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, STATES);
        actvLocation.setAdapter(state_adapter);

        /*
        appLocationService = new AppLocationService(getApplicationContext());

        if (mThreadHandler == null) {
            // Initialize and start the HandlerThread
            // which is basically a Thread with a Looper
            // attached (hence a MessageQueue)
            mHandlerThread = new HandlerThread(TAG, android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mHandlerThread.start();

            // Initialize the Handler
            mThreadHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        final ArrayList<String> results = mAdapter.resultList;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (results != null && results.size() > 0) {
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    mAdapter.notifyDataSetInvalidated();
                                }
                            }
                        });
                    }
                }
            };
        }

        actvLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                String description = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }
        });

        actvLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                final String value = charSequence.toString();

                // Remove all callbacks and messages
                mThreadHandler.removeCallbacksAndMessages(null);

                // Now add a new one
                mThreadHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // Background thread

                        mAdapter.resultList = mAdapter.mPlaceAPI.autocomplete(value);
                        // Footer
                        if (mAdapter.resultList.size() > 0)
                            mAdapter.resultList.add("footer");

                        // Post to Main Thread
                        mThreadHandler.sendEmptyMessage(1);
                    }
                }, 10);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //doAfterTextChanged();
            }
        });

        btAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location location = appLocationService
                        .getLocation(LocationManager.GPS_PROVIDER);

                //you can hard-code the lat & long if you have issues with getting it
                //remove the below if-condition and use the following couple of lines
                double latitude = 37.422005;
                double longitude = -122.084095;

                //if (location != null) {
                    //double latitude = location.getLatitude();
                    //double longitude = location.getLongitude();
                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(latitude, longitude,
                            getApplicationContext(), new GeocoderHandler());
                //} else {
                //    showSettingsAlert();
                //}
            }
        });

         */


        findViewById(R.id.button_verify_AVP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etCode.getText().toString().trim();
                if(code.isEmpty() || code.length() < 6) {
                    etCode.setError("Enter Code");
                    etCode.requestFocus();
                    return;
                }

                btVerify.setEnabled(true);
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
                            if(etName.getText().toString().trim().isEmpty()) {
                                etName.setError("Name Required");
                                etName.requestFocus();
                                return;
                            }

                            if(actvLocation.getText().toString().trim().isEmpty()) {
                                actvLocation.setError("Location Required");
                                actvLocation.requestFocus();
                                return;
                            }

                            SharedPreferences.Editor editor = getSharedPreferences(MY_SHARED_PREFERENCE, MODE_PRIVATE).edit();
                            editor.putString("name", etName.getText().toString());
                            editor.putString("location", actvLocation.getText().toString());
                            editor.apply();

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
                btVerify.setEnabled(true);
                //verifyCode(code);
            } else {
                //Toast.makeText(VerifyPhoneActivity.this, "Inform Developer Immediately...", Toast.LENGTH_LONG).show();
                 etCode.setText("Verified");

                if(etName.getText().toString().trim().isEmpty()) {
                    etName.setError("Name Required");
                    etName.requestFocus();
                    return;
                }

                if(actvLocation.getText().toString().trim().isEmpty()) {
                    actvLocation.setError("Location Required");
                    actvLocation.requestFocus();
                    return;
                }

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

     /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mThreadHandler != null) {
            mThreadHandler.removeCallbacksAndMessages(null);
            mHandlerThread.quit();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                VerifyPhoneActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        VerifyPhoneActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            actvLocation.setText(locationAddress);
        }
    }

     */
}
