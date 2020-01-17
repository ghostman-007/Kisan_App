package com.ghostman.kisanapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainFragment extends Fragment {

    private View rootView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        FloatingActionButton fab = (rootView).findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Inflate the layout for this fragment
        Button btSeller = rootView.findViewById(R.id.bt_seller);
        Button btBuyer = rootView.findViewById(R.id.bt_buyer);
        Button btTransportation = rootView.findViewById(R.id.bt_transport);
        Button btOfficer = rootView.findViewById(R.id.bt_officer);

        btSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), FarmerActivity.class));
                }
            }
        });

        btBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction =
                            fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, new BuyerFragment()).addToBackStack("TAG_BUYER");
                    fragmentTransaction.commit();
                }
            }
        });

        btTransportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Under Construction...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        btOfficer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Under Construction...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        return rootView;
    }


}
