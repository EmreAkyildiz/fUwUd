package com.impostors.fuwud.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.impostors.fuwud.Model.Product;

import com.impostors.fuwud.R;

import java.util.HashMap;
import java.util.Map;

public class RVMenuAdapter extends FirebaseRecyclerAdapter<Product, RVMenuAdapter.cardMenuItemHolder> {
    private Context context;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }


    public RVMenuAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {

        super(options);
    }

    @NonNull
    @Override
    public cardMenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //card bağlantısı
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_menu_layout, parent, false);
        return new cardMenuItemHolder(itemView);
    }


    @Override
    protected void onBindViewHolder(@NonNull cardMenuItemHolder cardMenuItemHolder, final int i, @NonNull Product product) {
        //current productın name ini price ını setleme
        cardMenuItemHolder.productName.setText(product.getName());
        cardMenuItemHolder.productPrice.setText(String.valueOf(product.getBuyPrice()));


    }

    public class cardMenuItemHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView productPrice;
        public ImageButton buttonEdit;
        public ImageButton buttonDelete;
        public CardView cardView;

        //Constructor
        public cardMenuItemHolder(final View view) {
            super(view);
            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            cardView = view.findViewById(R.id.CardView);
            productPrice = (TextView) view.findViewById(R.id.productPrice);
            productName = (TextView) view.findViewById(R.id.productName);
            buttonEdit = (ImageButton) view.findViewById(R.id.buttonEdit);
            buttonDelete = (ImageButton) view.findViewById(R.id.buttonDelete);


            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Alert dialog oluşturma
                    AlertDialog.Builder ad = new AlertDialog.Builder(v.getRootView().getContext());
                    //layout bağlama
                    View editTextAlert = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.update_alert, null);
                    //Girilen değerleri edittext olarak al
                    final EditText updateNameEdittext = editTextAlert.findViewById(R.id.editTextUpdateName);
                    final EditText updatePriceEdittext = editTextAlert.findViewById(R.id.editTextUpdatePrice);
                    //ad settings
                    ad.setMessage("Edit");
                    ad.setTitle("Edit Product");
                    ad.setView(editTextAlert);

                    ad.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //firebase bağlantı
                            FirebaseDatabase firebaseDatabase;
                            DatabaseReference databaseReference;
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference();

                            //edittext olarak alınan değerleri stringe çevir
                            final String updatedName = updateNameEdittext.getText().toString();
                            final String updatedPrice = updatePriceEdittext.getText().toString();
                            //basılan satırın nameini databasedeki name rowundan kontrol eden query
                            Query query = databaseReference.child("restaurants")
                                    .child(firebaseUser.getUid()).child("products").orderByChild("name").equalTo(getItem(getAdapterPosition()).getName());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                                        //Update
                                        Map<String, Object> updateInfo = new HashMap<>();
                                        updateInfo.put("name", updatedName);
                                        appleSnapshot.getRef().updateChildren(updateInfo);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                    ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }

                    });
                    ad.create().show();
                }

            });


            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRef(getAdapterPosition()).removeValue();
                }
            });


        }


    }
}