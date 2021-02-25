package com.impostors.fuwud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.impostors.fuwud.Adapter.RVRestaurantAdapter;
import com.impostors.fuwud.Model.Restaurant;
import com.impostors.fuwud.R;

import java.util.ArrayList;
import java.util.List;

public class ListedRestaurantActivity extends AppCompatActivity {

    RecyclerView recyclerViewRestaurants;
    RVRestaurantAdapter adapterRestaurant;
    private ArrayList<Restaurant> listOfRestaurants;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    Double currentLongitude,currentLatitude;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_restaurant);
        init();

        adapterRestaurant=new RVRestaurantAdapter(this,listOfRestaurants,this);
    }

    private void init(){
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        currentLongitude=getIntent().getDoubleExtra("longitude",0.0);
        currentLatitude=getIntent().getDoubleExtra("latitude",0.0);

        listOfRestaurants=new ArrayList();


        recyclerViewRestaurants=findViewById(R.id.RecyclerViewRestaurants);
        recyclerViewRestaurants.setLayoutManager(new LinearLayoutManager(this));

         Query queryForRestaurantAddresses =databaseReference.child("restaurants").orderByKey();


         ValueEventListener listener= new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.exists()) {
                     for (DataSnapshot issue : snapshot.getChildren()) {
                         Restaurant restaurant=issue.getValue(Restaurant.class);
                         String id=issue.getKey();
                         restaurant.setRestaurant_id(id);

                         if((restaurant.getLatitude()>currentLatitude-0.5&&restaurant.getLatitude()<currentLatitude+0.5)&&
                                 (restaurant.getLongitude()>currentLongitude-0.5&&restaurant.getLongitude()<currentLongitude+0.5)) {


                             listOfRestaurants.add(restaurant);
                             adapterRestaurant.setListOfRestaurants(listOfRestaurants);
                             recyclerViewRestaurants.setAdapter(adapterRestaurant);
                         }
                     }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         };
         queryForRestaurantAddresses.addListenerForSingleValueEvent(listener);

    }

}