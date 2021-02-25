package com.impostors.fuwud.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.impostors.fuwud.Model.PrevOrder;
import com.impostors.fuwud.Model.Product;
import com.impostors.fuwud.Model.Restaurant;
import com.impostors.fuwud.Model.User;
import com.impostors.fuwud.R;

import java.util.HashMap;
import java.util.Map;

public class RVRestaurantOrderAdapter extends FirebaseRecyclerAdapter<PrevOrder,RVRestaurantOrderAdapter.cardMenuItemHolder> {


    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public RVRestaurantOrderAdapter(@NonNull FirebaseRecyclerOptions<PrevOrder> options) {

        super(options);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();


    }



    @Override
    protected void onBindViewHolder(@NonNull final cardMenuItemHolder cardMenuItemHolder, int i, @NonNull PrevOrder prevOrder) {

        cardMenuItemHolder.textViewProducts.setText(prevOrder.getProducts().values().toString());
        if(prevOrder.isCompleted()){
            cardMenuItemHolder.textViewStatus.setText("ONAYLANDI");
        }
        else{
            cardMenuItemHolder.textViewStatus.setText("ONAYLANMADI");
        }


        Query queryforUserAddress=databaseReference.child("users").equalTo(prevOrder.getOwnerUid());
        queryforUserAddress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d:snapshot.getChildren()){
                    User user=d.getValue(User.class);
                    cardMenuItemHolder.textViewAddress.setText("longitude  "+user.getLongitude()+"--  latitude  "+user.getLatitude());
                    Log.e("aaaaaaa","longitude  "+user.getLongitude()+"--  latitude  "+user.getLatitude());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cardMenuItemHolder.textViewPrice.setText(prevOrder.getPrice()+" TL");





    }

    @NonNull
    @Override
    public cardMenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restaurant_order, parent, false);
        return new RVRestaurantOrderAdapter.cardMenuItemHolder(itemView);
    }


    public class cardMenuItemHolder extends RecyclerView.ViewHolder {

        public TextView textViewProducts,textViewPrice,textViewAddress,textViewStatus;
        public ImageButton imageButtonRCheckOrder,imageButtonRDeleteOrder;


        //Constructor
        public cardMenuItemHolder(final View view) {
            super(view);
            textViewProducts =  view.findViewById(R.id.textViewProducts);
            textViewPrice = view.findViewById(R.id.textViewPrice);
            textViewAddress = view.findViewById(R.id.textViewAddress);
            textViewStatus=view.findViewById(R.id.textViewStatus);
            imageButtonRCheckOrder =  view.findViewById(R.id.imageButtonRCheckOrder);
            imageButtonRDeleteOrder = view.findViewById(R.id.imageButtonRDeleteOrder);

            imageButtonRDeleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRef(getAdapterPosition()).removeValue();
                }
            });

            imageButtonRCheckOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textViewStatus.setText("ONAYLANDI");
                    getItem(getAdapterPosition()).setCompleted(true);
                    PrevOrder a=getItem(getAdapterPosition());

                    databaseReference.child("users").child(getItem(getAdapterPosition()).getOwnerUid()).child("completedOrders").push().setValue(a);
                    getRef(getAdapterPosition()).removeValue();
                    


                    //TODO MERT ABİSİ BURAYI YAPICAKSIN MAİL FALAN
                }

            });




        }


    }



}
