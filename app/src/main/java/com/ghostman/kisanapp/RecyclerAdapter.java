package com.ghostman.kisanapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Crop> arrayList;
    private Context context;

    RecyclerAdapter(ArrayList<Crop> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCrop;
        TextView tvCrop;
        TextView tvStock;
        TextView tvPrice;
        TextView tvGovtPrice;

        ViewHolder(View itemView) {
            super(itemView);
            this.tvCrop = itemView.findViewById(R.id.tv_crop_fb);
            this.tvStock = itemView.findViewById(R.id.tv_cropStock_fb);
            this.tvPrice = itemView.findViewById(R.id.tv_price_fb);
            this.tvGovtPrice = itemView.findViewById(R.id.tv_govtPrice_fb);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(arrayList.get(holder.getAdapterPosition()).getCrop());
                String message = "Seller Number : " + arrayList.get(holder.getAdapterPosition()).getPhone();
                builder.setMessage(message);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvCrop.setText(arrayList.get(position).getCrop());
        holder.tvStock.setText(String.format(Locale.getDefault(),"%s", arrayList.get(position).getStock()));
        holder.tvPrice.setText(String.format(Locale.getDefault(), "₹%s", arrayList.get(position).getPrice().toString()));
        holder.tvGovtPrice.setText(String.format(Locale.getDefault(), "₹%s", arrayList.get(position).getGovtPrice().toString()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
