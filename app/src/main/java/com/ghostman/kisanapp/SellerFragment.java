package com.ghostman.kisanapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SellerFragment extends Fragment {

    private View rootView;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    Map<String, Object> crop1 = new HashMap<>();
    Map<String, Object> crop2 = new HashMap<>();
    Map<String, Object> crop3 = new HashMap<>();
    Map<String, Object> crop4 = new HashMap<>();
    Map<String, Object> crop5 = new HashMap<>();
    Map<String, Object> crop6 = new HashMap<>();

    public SellerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_seller, container, false);

        final TextInputLayout til_state = (rootView).findViewById(R.id.til_state_fs);
        final TextInputLayout til_phone = (rootView).findViewById(R.id.til_phone_fs);
        final TextInputLayout til_name = (rootView).findViewById(R.id.til_name_fs);

        final AutoCompleteTextView actv_state = (rootView).findViewById(R.id.actv_state_fs);
        final EditText et_phone = (rootView).findViewById(R.id.et_phone_fs);
        final EditText et_name = (rootView).findViewById(R.id.et_name_fs);

        Button bt_save = (rootView).findViewById(R.id.bt_save_fs);

        final CheckBox cb1 = rootView.findViewById(R.id.checkBox);
        final CheckBox cb2 = rootView.findViewById(R.id.checkBox2);
        final CheckBox cb3 = rootView.findViewById(R.id.checkBox3);
        final CheckBox cb4 = rootView.findViewById(R.id.checkBox4);
        final CheckBox cb5 = rootView.findViewById(R.id.checkBox5);
        final CheckBox cb6 = rootView.findViewById(R.id.checkBox6);

        String[] STATES = new String[] {"assam", "delhi", "haryana", "uttar pradesh" };
        ArrayAdapter<String> state_adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.dropdown_menu_popup_item, STATES);
        actv_state.setAdapter(state_adapter);

        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb1.isChecked()) {
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
                            crop1.put("price", Integer.parseInt(price.getText().toString()));
                            crop1.put("govt_price", Integer.parseInt(govtPrice.getText().toString()));
                            crop1.put("quantity", Integer.parseInt(quantity.getText().toString()));

                            alertDialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cb1.setChecked(false);
                            alertDialog.cancel();
                        }
                    });
                }
            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb2.isChecked()) {
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
                            crop2.put("price", Integer.parseInt(price.getText().toString()));
                            crop2.put("govt_price", Integer.parseInt(govtPrice.getText().toString()));
                            crop2.put("quantity", Integer.parseInt(quantity.getText().toString()));

                            alertDialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cb2.setChecked(false);
                            alertDialog.cancel();
                        }
                    });
                }
            }
        });

        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb3.isChecked()) {
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
                            crop3.put("price", Integer.parseInt(price.getText().toString()));
                            crop3.put("govt_price", Integer.parseInt(govtPrice.getText().toString()));
                            crop3.put("quantity", Integer.parseInt(quantity.getText().toString()));

                            alertDialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cb3.setChecked(false);
                            alertDialog.cancel();
                        }
                    });
                }
            }
        });

        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb4.isChecked()) {
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
                            crop4.put("price", Integer.parseInt(price.getText().toString()));
                            crop4.put("govt_price", Integer.parseInt(govtPrice.getText().toString()));
                            crop4.put("quantity", Integer.parseInt(quantity.getText().toString()));

                            alertDialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cb4.setChecked(false);
                            alertDialog.cancel();
                        }
                    });
                }
            }
        });

        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb5.isChecked()) {
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
                            crop5.put("price", Integer.parseInt(price.getText().toString()));
                            crop5.put("govt_price", Integer.parseInt(govtPrice.getText().toString()));
                            crop5.put("quantity", Integer.parseInt(quantity.getText().toString()));

                            alertDialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cb5.setChecked(false);
                            alertDialog.cancel();
                        }
                    });
                }
            }
        });

        cb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb6.isChecked()) {
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
                            crop6.put("price", Integer.parseInt(price.getText().toString()));
                            crop6.put("govt_price", Integer.parseInt(govtPrice.getText().toString()));
                            crop6.put("quantity", Integer.parseInt(quantity.getText().toString()));

                            alertDialog.cancel();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cb6.setChecked(false);
                            alertDialog.cancel();
                        }
                    });
                }
            }
        });


        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_name.getText().toString().isEmpty()) {
                    et_name.setError("Required Field");
                    et_name.requestFocus();
                    return;
                }

                if (et_phone.getText().toString().isEmpty() || et_phone.getText().toString().length() < 10) {
                    et_phone.setError("Required Field");
                    et_phone.requestFocus();
                    return;
                }

                if (actv_state.getText().toString().isEmpty()) {
                    actv_state.setError("Required Field");
                    actv_state.requestFocus();
                    return;
                }

                Map<String, Object> name = new HashMap<>();
                name.put("name", et_name.getText().toString());
                final String phone = et_phone.getText().toString();
                final String state = actv_state.getText().toString();


                if(cb1.isChecked()) {
                    final Map<String, Object> stateMap = new HashMap<>();
                    stateMap.put("state", state);
                    final Map<String, Object> phoneMap = new HashMap<>();
                    phoneMap.put("phone", phone);
                    phoneMap.put("name", name);
                    firebaseFirestore.collection("states").document(state)
                            .collection("phone").document(phone)
                            .collection("crops").document("Barley")
                            .set(crop1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    firebaseFirestore.collection("states").document(state).set(stateMap);
                                    firebaseFirestore.collection("states").document(state)
                                            .collection("phone").document(phone).set(phoneMap);
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

                if(cb2.isChecked()) {
                    firebaseFirestore
                            .collection("states").document(state)
                            .collection("phone").document(phone)
                            .collection("crops").document("Bulgur")
                            .set(crop2)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
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

                if(cb3.isChecked()) {
                    firebaseFirestore
                            .collection("states").document(state)
                            .collection("phone").document(phone)
                            .collection("crops").document("Farro")
                            .set(crop3)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
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

                if(cb4.isChecked()) {
                    firebaseFirestore
                            .collection("states").document(state)
                            .collection("phone").document(phone)
                            .collection("crops").document("Kasha")
                            .set(crop4)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
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

                if(cb5.isChecked()) {
                    firebaseFirestore
                            .collection("states").document(state)
                            .collection("phone").document(phone)
                            .collection("crops").document("Quinoa")
                            .set(crop5)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
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

                if(cb6.isChecked()) {
                    firebaseFirestore
                            .collection("states").document(state)
                            .collection("phone").document(phone)
                            .collection("crops").document("Wheat Berries")
                            .set(crop6)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
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

            }
        });

        return rootView;
    }

}
