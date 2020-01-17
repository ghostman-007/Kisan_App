package com.ghostman.kisanapp;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentSellerCrops extends Fragment {

    private static final String MY_SHARED_PREFERENCE = "shared_Preference";

    private View rootView;
    private RecyclerView recyclerView;

    public FragmentSellerCrops() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_seller_crops, container, false);

        recyclerView = rootView.findViewById(R.id.rv_seller_fsc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final ArrayList<Crop> arrayListCROPS = new ArrayList<>();
        final RecyclerAdapterSeller recyclerAdapterSeller = new RecyclerAdapterSeller(arrayListCROPS, rootView.getContext());
        recyclerView.setAdapter(recyclerAdapterSeller);

        final String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        SharedPreferences prefs = rootView.getContext().getSharedPreferences(MY_SHARED_PREFERENCE, MODE_PRIVATE);
        String stringName = prefs.getString("name", "Unknown");
        final String state = prefs.getString("location", "Unknown");

        Toast.makeText(getActivity(), " " + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
        final List<String> arrayListStates = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("StatesPhones").document(state)
                .collection("Phones").document(phone)
                .collection("Crops")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                            arrayListStates.add(documentSnapshot.getId());
                        for(String i : arrayListStates) {
                            FirebaseFirestore.getInstance().collection("StatesPhones").document(state)
                                    .collection("Phones").document(phone)
                                    .collection("Crops").document(i)
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

                                                    recyclerAdapterSeller.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "No Data Present or network problem", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}
