package com.impostors.fuwud.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.util.ArrayList;
import java.util.List;


public class RVBasketAdapter extends FirebaseRecyclerAdapter<Product,RVBasketAdapter.RVBasketHolder> {
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private double totalPrice=0;
    ConstraintLayout constraintLayout;
    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public RVBasketAdapter(@NonNull FirebaseRecyclerOptions<Product> options, ConstraintLayout constraintLayout, Context context) {

            super(options);
            this.constraintLayout=constraintLayout;
            this.context=context;

            }

    @Override
    protected void onBindViewHolder(@NonNull RVBasketHolder rvBasketHolder, int i, @NonNull Product product) {
        rvBasketHolder.textViewProductNameB.setText(product.getName());
        rvBasketHolder.textViewProductPriceB.setText(product.getBuyPrice()+"");
        rvBasketHolder.textViewProductNumber.setText(product.getCount()+"");





    }



    @NonNull
    @Override
    public RVBasketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_basket, parent, false);
        return new RVBasketAdapter.RVBasketHolder(itemView);

    }


    public class RVBasketHolder extends RecyclerView.ViewHolder {

        public TextView textViewProductNameB;
        public TextView textViewProductPriceB;
        public ImageButton imageButtonDeleteFromBasket;
        public TextView textViewProductNumber;


        //Constructor
        public RVBasketHolder(final View view) {
            super(view);

            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();
            firebaseDatabase=FirebaseDatabase.getInstance();
            databaseReference=firebaseDatabase.getReference();

            textViewProductPriceB = view.findViewById(R.id.Date1);
            textViewProductNameB = view.findViewById(R.id.textViewProductNameB);
            textViewProductNumber=view.findViewById(R.id.textViewProductNumber);
            imageButtonDeleteFromBasket =  view.findViewById(R.id.imageButtonDeleteFromBasket);
            imageButtonDeleteFromBasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRef(getAdapterPosition()).removeValue();
                    Query queryForR = databaseReference.child("users").child(currentUser.getUid()).child("currentBasket");
                    final List<Product> ürünler=new ArrayList<>();
                    queryForR.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot d: snapshot.getChildren()){
                                Product product = d.getValue(Product.class);
                                ürünler.add(product);
                            }
                            if (ürünler.isEmpty()){
                                View tasarim = LayoutInflater.from(context).inflate(R.layout.no_basket_page,null,false);
                                constraintLayout.addView(tasarim);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }
    }

}

