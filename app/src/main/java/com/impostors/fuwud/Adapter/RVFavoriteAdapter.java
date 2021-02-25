package com.impostors.fuwud.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.impostors.fuwud.Activity.OtherFavoritesActivity;
import com.impostors.fuwud.Activity.RestaurantDetailActivity;
import com.impostors.fuwud.Model.Product;
import com.impostors.fuwud.Model.Restaurant;
import com.impostors.fuwud.R;

import java.util.ArrayList;
import java.util.List;

public class RVFavoriteAdapter extends FirebaseRecyclerAdapter<Restaurant,RVFavoriteAdapter.ViewHolderForFavorites> {

    Context mContext;
    Activity activity;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    ConstraintLayout constraintLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public RVFavoriteAdapter(@NonNull FirebaseRecyclerOptions<Restaurant> options, Context mContext, Activity activity,ConstraintLayout constraintLayout) {
        super(options);
        this.activity=activity;
        this.mContext=mContext;
        this.constraintLayout=constraintLayout;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolderForFavorites myviewHolder, int i, @NonNull Restaurant restaurant) {
        myviewHolder.restaurantName.setText(restaurant.getRestaurantName());
        myviewHolder.restaurantCuisine.setText(restaurant.getCuisine());
        myviewHolder.restaurantPhone.setText(restaurant.getPhoneNumber());
    }

    @NonNull
    @Override
    public ViewHolderForFavorites onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_favorite,parent,false);
        return new ViewHolderForFavorites(view);
    }

    class ViewHolderForFavorites extends RecyclerView.ViewHolder{
            TextView restaurantName,restaurantPhone,restaurantCuisine;
            ImageButton deleteFavorite,buttonGoToRestaurantRV;


        public ViewHolderForFavorites(@NonNull View itemView) {
            super(itemView);
            deleteFavorite =itemView.findViewById(R.id.buttonDeleteFavorite);
            restaurantCuisine=itemView.findViewById(R.id.textViewSearchedCuisine);
            restaurantName=itemView.findViewById(R.id.textViewSearchedName);
            restaurantPhone=itemView.findViewById(R.id.textViewSearchedPhoneNumber);
            buttonGoToRestaurantRV = itemView.findViewById(R.id.buttonGoToRestaurantRV);
            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();
            firebaseDatabase=FirebaseDatabase.getInstance();
            databaseReference=firebaseDatabase.getReference();
            deleteFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRef(getAdapterPosition()).removeValue();
                    Query queryForR = databaseReference.child("users").child(currentUser.getUid()).child("favorites");
                    final List<Restaurant> favoriler=new ArrayList<>();
                    queryForR.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot d: snapshot.getChildren()){
                                Restaurant restaurant = d.getValue(Restaurant.class);
                                favoriler.add(restaurant);
                            }
                            if (favoriler.isEmpty()){
                                View tasarim = LayoutInflater.from(activity).inflate(R.layout.no_favorites_page,null,false);
                                constraintLayout.addView(tasarim);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            buttonGoToRestaurantRV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                    intent.putExtra("restaurant_id",getItem(getAdapterPosition()).getRestaurant_id());
                    mContext.startActivity(intent);
                }
            });
        }


    }




}
