package com.ghostman.kisanapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecyclerAdapterSeller extends RecyclerView.Adapter<RecyclerAdapterSeller.ViewHolder> {

    private static final String MY_SHARED_PREFERENCE = "shared_Preference";
    private Context context;
    private ArrayList<Crop> cropArrayList;

    RecyclerAdapterSeller(ArrayList<Crop> cropArrayList, Context context) {
        this.cropArrayList = cropArrayList;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCrop;
        TextView tvCrop;
        TextView tvPrice;
        TextView tvGovtPrice;
        TextView tvQuantity;
        CardView cvView;

        ViewHolder(View view) {
            super(view);
            this.ivCrop = view.findViewById(R.id.iv_crop_lsc);
            this.tvCrop = view.findViewById(R.id.tv_crop_lsc);
            this.tvPrice = view.findViewById(R.id.tv_price_lsc);
            this.tvGovtPrice = view.findViewById(R.id.tv_govtPrice_lsc);
            this.tvQuantity = view.findViewById(R.id.tv_quantity_lsc);
            this.cvView = view.findViewById(R.id.cv_crops_lsc);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_seller_crops, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvCrop.setText(cropArrayList.get(position).getCrop());
        holder.tvPrice.setText(String.format(Locale.getDefault(),"%s",cropArrayList.get(position).getPrice().toString()));
        holder.tvGovtPrice.setText(String.format(Locale.getDefault(),"%s",cropArrayList.get(position).getGovtPrice().toString()));
        holder.tvQuantity.setText(String.format(Locale.getDefault(), "%s", cropArrayList.get(position).getStock().toString()));

        holder.cvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update_delete);

                Button update = dialog.findViewById(R.id.b_update_dud);
                Button delete = dialog.findViewById(R.id.b_delete_dud);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        SharedPreferences prefs = context.getSharedPreferences(MY_SHARED_PREFERENCE, MODE_PRIVATE);
                        String stringName = prefs.getString("name", "Unknown");
                        final String state = prefs.getString("location", "Unknown");
                        FirebaseFirestore.getInstance().collection("StatesCrops").document(state)
                                .collection("Crops").document(cropArrayList.get(position).getCrop())
                                .collection("Phone").document(cropArrayList.get(position).getPhone())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseFirestore.getInstance().collection("StatesPhones").document(state)
                                                .collection("Phones").document(cropArrayList.get(position).getPhone())
                                                .collection("Crops").document(cropArrayList.get(position).getCrop())
                                                .delete();

                                        cropArrayList.clear();
                                        Toast.makeText(context, "Data successfully deleted!",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });

                        final String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
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
                                                                    cropArrayList.add(c);
                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                        getItemCount();
                        notifyDataSetChanged();
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        final Map<String, Object> crop = new HashMap<>();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("");
                        alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
                        alertDialogBuilder.setCancelable(false);

                        LayoutInflater layoutInflater = LayoutInflater.from(context);

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

                                SharedPreferences prefs = context.getSharedPreferences(MY_SHARED_PREFERENCE, MODE_PRIVATE);
                                String stringName = prefs.getString("name", "Unknown");
                                final String state = prefs.getString("location", "Unknown");
                                FirebaseFirestore.getInstance().collection("StatesCrops").document(state)
                                        .collection("Crops").document(cropArrayList.get(position).getCrop())
                                        .collection("Phone").document(cropArrayList.get(position).getPhone())
                                        .update(crop)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseFirestore.getInstance().collection("StatesPhones").document(state)
                                                        .collection("Phones").document(cropArrayList.get(position).getPhone())
                                                        .collection("Crops").document(cropArrayList.get(position).getCrop())
                                                        .update(crop);
                                                Toast.makeText(context, "Data successfully updated!",Toast.LENGTH_SHORT).show();
                                                cropArrayList.clear();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating crop", e);
                                            }
                                        });

                                final String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
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
                                                                            cropArrayList.add(c);
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

                                alertDialog.cancel();
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.cancel();
                            }
                        });
                    }
                });
                getItemCount();
                notifyDataSetChanged();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cropArrayList.size();
    }
}
