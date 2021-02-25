package com.impostors.fuwud.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.impostors.fuwud.Adapter.RVMenuAdapter;
import com.impostors.fuwud.Model.Product;
import com.impostors.fuwud.R;




public class FragmentRestaurantMenu extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Activity context;
    RecyclerView recyclerView;
    private RVMenuAdapter adapter;
    private Button buttonInsert;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel_menu, container, false);

        context = getActivity();
        buttonInsert = view.findViewById(R.id.buttonInsert);
        recyclerView = view.findViewById(R.id.rvmenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("restaurants");


        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alert View layout bağlama
                View editTextAlert = getLayoutInflater().inflate(R.layout.insertion_alert, null);

                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setMessage("Yeni Ürünün İsmi");
                ad.setTitle("Yeni Ürün Ekle");
                //Alert view a girilen değerleri Edittext cinsinden alma
                final EditText InsertedProductName = editTextAlert.findViewById(R.id.editTextReply);
                final EditText InsertedPrice = editTextAlert.findViewById(R.id.editTextTextPrice);

                //alertview view setleme
                ad.setView(editTextAlert);

                ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Edittext cinsinden alınan verileri String olarak tekrar almak
                        final String productName = InsertedProductName.getText().toString();
                        //bu kontrol için
                        final String productPriceCheck = InsertedPrice.getText().toString();

                        //Boş mu kontrolü
                        if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(productPriceCheck)) {
                            Toast.makeText(context, "Gerekli Alanları Doldurunuz", Toast.LENGTH_LONG).show();
                        } else {
                            //Boş değilse insert yapar
                            final Query query = databaseReference.orderByKey().equalTo(firebaseUser.getUid());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    final double productPrice = Double.parseDouble(InsertedPrice.getText().toString());
                                    //create, push
                                    final Product product = new Product(productName, productPrice);
                                    product.setRestaurant_id(firebaseUser.getUid());
                                    query.getRef().child(firebaseUser.getUid()).child("products").push().setValue(product);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //alert için yarat göster
                ad.create().show();


            }
        });
        //adapter bağlantısı, FirebaseRecyclerOptions özel yapısı
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(FirebaseDatabase.getInstance().getReference("restaurants").child(firebaseUser.getUid()).child("products"), Product.class)
                .build();

        adapter = new RVMenuAdapter(options);
        recyclerView.setAdapter(adapter);
        return view;

    }
    //adapter için start, stop
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }



}

