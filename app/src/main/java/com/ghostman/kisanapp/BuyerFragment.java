package com.ghostman.kisanapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuyerFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;

    public BuyerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_buyer, container, false);


        final AutoCompleteTextView actvState = rootView.findViewById(R.id.dropdown_state);
        final AutoCompleteTextView actvCrop = rootView.findViewById(R.id.dropdown_crop);

        recyclerView = rootView.findViewById(R.id.rv_cropList_fb);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FirebaseFirestore.getInstance().collection("StatesCrops")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> arrayListStates = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                                arrayListStates.add(documentSnapshot.getId());

                        ArrayAdapter<String> state_adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.dropdown_menu_popup_item, arrayListStates);
                        actvState.setAdapter(state_adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(rootView.getContext(), "Error Retrieving Data... " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        actvState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                actvCrop.setText("");
                final ArrayList<Crop> arrayListCROPS = new ArrayList<>();
                final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(arrayListCROPS, rootView.getContext());
                recyclerView.setAdapter(recyclerAdapter);

                final ArrayList<String> arrayListCrops = new ArrayList<>();
                FirebaseFirestore.getInstance().collection("StatesCrops").document(actvState.getText().toString())
                        .collection("Crops")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                                    arrayListCrops.add(documentSnapshot.getId());

                                ArrayAdapter<String> crop_adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.dropdown_menu_popup_item, arrayListCrops);
                                actvCrop.setAdapter(crop_adapter);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(rootView.getContext(), "Error Retrieving Crops... " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        actvCrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(actvState.getText().toString().isEmpty()) {
                    actvCrop.setText("");
                    actvState.setError("Select State");
                    actvState.requestFocus();
                    return;
                }

                final ArrayList<String> arrayListPhones = new ArrayList<>();
                final ArrayList<Crop> arrayListCROPS = new ArrayList<>();
                final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(arrayListCROPS, rootView.getContext());
                recyclerView.setAdapter(recyclerAdapter);
                FirebaseFirestore.getInstance().collection("StatesCrops").document(actvState.getText().toString())
                        .collection("Crops").document(actvCrop.getText().toString())
                        .collection("Phone")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                                    arrayListPhones.add(documentSnapshot.getId());
                                for (String i : arrayListPhones) {
                                    FirebaseFirestore.getInstance().collection("StatesCrops").document(actvState.getText().toString())
                                            .collection("Crops").document(actvCrop.getText().toString())
                                            .collection("Phone").document(i)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document != null) {
                                                            Crop c = new Crop();
                                                            c.setCrop(document.getString("crop"));
                                                            c.setStock(document.getLong("quantity"));
                                                            c.setPrice(document.getLong("price"));
                                                            c.setGovtPrice(document.getLong("govt_price"));
                                                            c.setPhone(document.getString("phone"));
                                                            arrayListCROPS.add(c);

                                                            Toast.makeText(getContext(), " " + document.getString("crop") +
                                                                    document.getLong("price") +
                                                                    document.getLong("govt_price") +
                                                                    document.getLong("quantity") +
                                                                    document.getString("phone"), Toast.LENGTH_SHORT).show();

                                                            recyclerAdapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Error Retrieving Data...", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
            }
        });
        return rootView;
    }
}