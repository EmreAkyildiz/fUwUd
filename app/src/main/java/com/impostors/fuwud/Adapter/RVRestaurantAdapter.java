package com.impostors.fuwud.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.impostors.fuwud.Model.Restaurant;
import com.impostors.fuwud.R;

import java.util.List;

public class RVRestaurantAdapter extends RecyclerView.Adapter<RVRestaurantAdapter.ViewHolderForRestaurant> {
    private List<Restaurant> listOfRestaurants;
    private Context mContext;
    private Activity activity;
    private boolean flag = false;




    public RVRestaurantAdapter(Context mContext, List<Restaurant> listOfRestaurants, Activity activity) {
        this.mContext = mContext;
        this.listOfRestaurants = listOfRestaurants;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolderForRestaurant onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_restaurant_layout, parent, false);

        return new RVRestaurantAdapter.ViewHolderForRestaurant(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForRestaurant holder, int position) {
        final Restaurant restaurant = listOfRestaurants.get(position);
        holder.restaurantName.setText(restaurant.getRestaurantName());
        holder.cuisine.setText(restaurant.getCuisine());
        holder.phoneNumber.setText(restaurant.getPhoneNumber());


    }

    @Override
    public int getItemCount() {
        return listOfRestaurants.size();
    }

    public void setListOfRestaurants(List<Restaurant> listOfRestaurants) {
        this.listOfRestaurants = listOfRestaurants;
    }

    /*
    @Override
    protected void onBindViewHolder(@NonNull ViewHolderForRestaurant viewHolderForRestaurant, int i, @NonNull Restaurant restaurant) {
        viewHolderForRestaurant.restaurantName.setText(restaurant.getRestaurantName());
        viewHolderForRestaurant.email.setText(restaurant.getEmail());
        viewHolderForRestaurant.phoneNumber.setText(restaurant.getPhoneNumber());
    }

    @NonNull
    @Override
    public ViewHolderForRestaurant onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restaurant_layout,parent,false);
        return new ViewHolderForRestaurant(view);
    }*/

    class ViewHolderForRestaurant extends RecyclerView.ViewHolder {
        TextView cuisine, phoneNumber, restaurantName;
        private ImageButton buttonGoToRestaurantRV;
        private ImageButton addToFavorites;

        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;


        public ViewHolderForRestaurant(@NonNull final View itemView) {
            super(itemView);
            cuisine = itemView.findViewById(R.id.cuisineRV);
            phoneNumber = itemView.findViewById(R.id.phoneNumberRV);
            restaurantName = itemView.findViewById(R.id.restaurantNameRV);
            buttonGoToRestaurantRV = itemView.findViewById(R.id.buttonGoToRestaurantRV);
            addToFavorites = itemView.findViewById(R.id.addToFav);


            buttonGoToRestaurantRV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                    intent.putExtra("restaurant_id", listOfRestaurants.get(getLayoutPosition()).getRestaurant_id());
                    intent.putExtra("restaurantName",listOfRestaurants.get(getLayoutPosition()).getRestaurantName());
                    intent.putExtra("restaurantPhone",listOfRestaurants.get(getLayoutPosition()).getPhoneNumber());
                    intent.putExtra("restaurantEmail",listOfRestaurants.get(getLayoutPosition()).getEmail());
                    intent.putExtra("restaurantLatitude",listOfRestaurants.get(getLayoutPosition()).getLatitude());
                    intent.putExtra("restaurantLongitude",listOfRestaurants.get(getLayoutPosition()).getLongitude());

                    setListOfRestaurants(null);
                    mContext.startActivity(intent);
                    activity.finish();
                }
            });

            addToFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference();
                    Restaurant favoriteTemp = listOfRestaurants.get(getAdapterPosition());
                  //  check();
                    Log.e("msg", String.valueOf(flag));
                    FirebaseAuth auth;
                    final FirebaseUser currentUser;
                    auth = FirebaseAuth.getInstance();
                    currentUser = auth.getCurrentUser();
                    boolean isPressed=false;

                    if(isPressed){
                        v.setBackgroundResource(R.drawable.icon_star_empty);
                    }else{
                        v.setBackgroundResource(R.drawable.icon_star_filled);
                    }
                    isPressed = !isPressed; // reverse


                  //  if(!check()) {
                    databaseReference.child("users").child(currentUser.getUid()).child("favorites").push().setValue(favoriteTemp);
                    flag=false;
                        Toast.makeText(mContext, "Restoran favorilere eklendi", Toast.LENGTH_SHORT).show();
                    //}
                   // else Toast.makeText(mContext,"restoran zaten ekli",Toast.LENGTH_LONG).show();
                }
            });

        }
   /*     public boolean check() {
            FirebaseAuth auth;
            final FirebaseUser currentUser;
            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();
            final Restaurant favoriteTemp = listOfRestaurants.get(getAdapterPosition());
            Query query =databaseReference.child("users").child(currentUser.getUid()).child("favorites");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot d:snapshot.getChildren()){
                        Restaurant r= d.getValue(Restaurant.class);
                         if(r.getRestaurant_id().equals(favoriteTemp.getRestaurant_id())){
                             flag=true;
                         }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            return flag;
        }*/

    }
}


