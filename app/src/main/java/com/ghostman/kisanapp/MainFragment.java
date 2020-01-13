package com.ghostman.kisanapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainFragment extends Fragment {

    private View rootView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Inflate the layout for this fragment
        Button btSeller = rootView.findViewById(R.id.bt_seller);
        Button btBuyer = rootView.findViewById(R.id.bt_buyer);

        btSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, new SellerFragment()).addToBackStack("TAG_SELLER");
                fragmentTransaction.commit();
            }
        });

        btBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, new BuyerFragment()).addToBackStack("TAG_BUYER");
                fragmentTransaction.commit();
            }
        });

        return rootView;
    }


}
