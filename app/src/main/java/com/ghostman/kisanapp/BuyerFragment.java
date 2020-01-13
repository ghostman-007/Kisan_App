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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuyerFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private Crop crop;

    public BuyerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_buyer, container, false);

        final TextInputLayout tilState = rootView.findViewById(R.id.til_state_fb);
        final TextInputLayout tilCrop = rootView.findViewById(R.id.til_crop_fb);
        final AutoCompleteTextView actvState = rootView.findViewById(R.id.dropdown_state);
        final AutoCompleteTextView actvCrop = rootView.findViewById(R.id.dropdown_crop);

        recyclerView = rootView.findViewById(R.id.rv_cropList_fb);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // FOR TESTING PURPOSE ---- START
        final ArrayList<Crop> arrayList = new ArrayList<>();
        Crop c = new Crop();
        c.setCrop("Barley");
        c.setStock(100);
        c.setPrice(20);
        c.setGovtPrice(25);
        arrayList.add(c);
        Crop c1 = new Crop();
        c1.setCrop("Bulgur");
        c1.setStock(50);
        c1.setPrice(20);
        c1.setGovtPrice(25);
        arrayList.add(c1);
        Crop c2 = new Crop();
        c2.setCrop("Kasha");
        c2.setStock(500);
        c2.setPrice(20);
        c2.setGovtPrice(25);
        arrayList.add(c2);
        Crop c3 = new Crop();
        c3.setCrop("Farrc");
        c3.setStock(500);
        c3.setPrice(50);
        c3.setGovtPrice(50);
        arrayList.add(c3);
        Crop c4 = new Crop();
        c4.setCrop("Quinoa");
        c4.setStock(200);
        c4.setPrice(30);
        c4.setGovtPrice(25);
        arrayList.add(c4);
        // TESTING ---- END

        String[] CROP = new String[] {"Barley", "Bulgur", "Farro", "Kasha", "Quinoa", "Wheat Berries" };
        final ArrayAdapter<String> crop_adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.dropdown_menu_popup_item, CROP);
        actvCrop.setAdapter(crop_adapter);

        database.collection("states")
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

        actvCrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(actvState.getText().toString().isEmpty()) {
                    actvState.requestFocus();
                    return;
                }

                Toast.makeText(rootView.getContext(),tilCrop.getEditText().getText().toString(),Toast.LENGTH_SHORT).show();

                database.collection("state").document(tilState.getEditText().getText().toString())
                        .collection("phone")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Toast.makeText(rootView.getContext(), tilState.getEditText().getText().toString(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(rootView.getContext(), database.collection("states").document(actvState.getText().toString())
                                                .collection("phone").document(document.getId())
                                                .collection("crops").document().getPath(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Toast.makeText(rootView.getContext(), "SUCCESS : " + tilState.getEditText().getText().toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(rootView.getContext(), database.collection("states").document(actvState.getText().toString())
                                            .collection("phone").document(documentSnapshot.getId())
                                            .collection("crops").document().getPath(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(rootView.getContext(), "Error retrieving data...", Toast.LENGTH_SHORT).show();
                    }
                });

                recyclerView.setAdapter(new RecyclerAdapter(arrayList, rootView.getContext()));
            }
        });

        actvState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                database.collection(tilState.getEditText().getText().toString())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    crop = documentSnapshot.toObject(Crop.class);
                                    //ArrayList<Boolean> temp = crop.getGrains();
                                }
                            }
                        });
                recyclerView.setAdapter(new RecyclerAdapter(arrayList, rootView.getContext()));
            }
        });

        return rootView;
    }
}