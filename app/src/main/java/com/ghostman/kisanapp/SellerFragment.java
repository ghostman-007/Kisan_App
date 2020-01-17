package com.ghostman.kisanapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SellerFragment extends Fragment {

    private static final String MY_SHARED_PREFERENCE = "shared_Preference";

    private View rootView;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private Map<String, Object> crop = new HashMap<>();

    public SellerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_seller, container, false);

        final AutoCompleteTextView actv_state = (rootView).findViewById(R.id.actv_state_fs);
        final AutoCompleteTextView actv_crops = (rootView).findViewById(R.id.actv_dropdown_crops_fs);
        final TextView tvCropsDetails = (rootView).findViewById(R.id.tv_crop_details_fs);

        Button bt_save = (rootView).findViewById(R.id.bt_save_fs);

        // TODO: Add all required STATES
        String[] STATES = new String[] {"Assam", "Delhi", "Haryana", "Uttar Pradesh" };
        ArrayAdapter<String> state_adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.dropdown_menu_popup_item, STATES);
        actv_state.setAdapter(state_adapter);

        // TODO: Add all required CROPS
        String[] CROPS = new String[] {"Rice", "Wheat", "Pulses", "Jowar", "Tomato", "Potato", "Onion", "Bitter Gourd"};
        ArrayAdapter<String> cropsAdapter = new ArrayAdapter<>(rootView.getContext(), R.layout.dropdown_menu_popup_item, CROPS);
        actv_crops.setAdapter(cropsAdapter);

        actv_crops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), " " +  adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
                alertDialogBuilder.setTitle("");
                alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
                alertDialogBuilder.setCancelable(false);

                LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());

                View popupInputDialogView = layoutInflater.inflate(R.layout.popup_input_dialog, null);

                final EditText price = popupInputDialogView.findViewById(R.id.price_pid);
                final EditText govtPrice = popupInputDialogView.findViewById(R.id.govtPrice_pid);
                final EditText quantity = popupInputDialogView.findViewById(R.id.quantity_pid);
                Button cancel = popupInputDialogView.findViewById(R.id.bt_cancel_pid);
                Button done = popupInputDialogView.findViewById(R.id.bt_done_pid);

                alertDialogBuilder.setView(popupInputDialogView);

                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                // When user click the save user data button in the popup dialog.
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        crop.put("price", Integer.parseInt(price.getText().toString()));
                        crop.put("govt_price", Integer.parseInt(govtPrice.getText().toString()));
                        crop.put("quantity", Integer.parseInt(quantity.getText().toString()));

                        String temp = "Crop : " + actv_crops.getText().toString() +
                                "\nQuantity : " + crop.get("quantity") + " kg" +
                                "\nPrice : ₹" + crop.get("price") + " per kg" +
                                "\nGovernment Price : ₹" + crop.get("govt_price") + " per kg";

                        tvCropsDetails.setText(temp);

                        alertDialog.cancel();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        actv_crops.setText("");
                        alertDialog.cancel();
                    }
                });
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (et_name.getText().toString().isEmpty()) {
                    et_name.setError("Required Field");
                    et_name.requestFocus();
                    return;
                }

                if (et_phone.getText().toString().isEmpty() || et_phone.getText().toString().length() < 10) {
                    et_phone.setError("Required Field");
                    et_phone.requestFocus();
                    return;
                }*/

                if (actv_state.getText().toString().isEmpty()) {
                    actv_state.setError("Select State");
                    actv_state.requestFocus();
                    return;
                }

                if(actv_crops.getText().toString().isEmpty()) {
                    actv_crops.setError("Select Crop");
                    actv_crops.requestFocus();
                    return;
                }

                SharedPreferences prefs = rootView.getContext().getSharedPreferences(MY_SHARED_PREFERENCE, MODE_PRIVATE);
                String stringName = prefs.getString("name", "Unknown");
                String stringLocation = prefs.getString("location", "Unknown");

                final String stringState = actv_state.getText().toString();
                final String stringCrop = actv_crops.getText().toString();
                final String stringPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

                final Map<String, Object> stateMap = new HashMap<>();
                stateMap.put("state", stringState);
                final Map<String, Object> phoneMap = new HashMap<>();
                phoneMap.put("phone", stringPhone);
                phoneMap.put("name", stringName);
                final Map<String, Object> cropMap = new HashMap<>();
                cropMap.put("crop", stringCrop);

                crop.put("crop", stringCrop);
                crop.put("phone", stringPhone);

                firebaseFirestore.collection("StatesPhones").document(stringState).set(stateMap);
                firebaseFirestore.collection("StatesPhones").document(stringState)
                        .collection("Phones").document(stringPhone).set(phoneMap);
                firebaseFirestore.collection("StatesPhones").document(stringState)
                        .collection("Phones").document(stringPhone)
                        .collection("Crops").document(stringCrop)
                        .set(crop)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firebaseFirestore.collection("StatesCrops").document(stringState).set(stateMap);
                                firebaseFirestore.collection("StatesCrops").document(stringState)
                                        .collection("Crops").document(stringCrop).set(cropMap);
                                firebaseFirestore.collection("StatesCrops").document(stringState)
                                        .collection("Crops").document(stringCrop)
                                        .collection("Phone").document(stringPhone)
                                        .set(crop);

                                Toast.makeText(getContext(), "Data Saved successfully...", Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return rootView;
    }
}
