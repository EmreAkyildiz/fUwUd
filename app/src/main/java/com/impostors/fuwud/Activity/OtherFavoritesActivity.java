package com.impostors.fuwud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.impostors.fuwud.Adapter.RVFavoriteAdapter;
import com.impostors.fuwud.Model.Product;
import com.impostors.fuwud.Model.Restaurant;
import com.impostors.fuwud.R;

import java.util.ArrayList;
import java.util.List;

public class OtherFavoritesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RVFavoriteAdapter adapter;
    ConstraintLayout content_favorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_favorites);
        FirebaseAuth auth;
        final FirebaseUser currentUser;
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        content_favorites=findViewById(R.id.content_favorites);

        Query queryForR = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("favorites");
        final List<Restaurant> favoriler=new ArrayList<>();
        queryForR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d: snapshot.getChildren()){
                    Restaurant restaurant = d.getValue(Restaurant.class);
                    favoriler.add(restaurant);
                }
                if (favoriler.isEmpty()){
                    View tasarim = LayoutInflater.from(OtherFavoritesActivity.this).inflate(R.layout.no_favorites_page,null,false);
                    content_favorites.addView(tasarim);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.RVOtherFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Restaurant> options = new FirebaseRecyclerOptions.Builder<Restaurant>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("favorites"), Restaurant.class)
                .build();

        adapter = new RVFavoriteAdapter(options, this, this,content_favorites);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

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