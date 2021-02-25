package com.impostors.fuwud.Activity;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.impostors.fuwud.Adapter.RVSearchAdapter;
import com.impostors.fuwud.Model.Restaurant;
import com.impostors.fuwud.R;

public class FragmentSearch extends Fragment {
    RecyclerView recyclerView;
    RVSearchAdapter searchAdapter;
    Button cuisineSearchBalık;
    Button cuisineSearchÇiğKöfte;
    Button cuisineSearchEvYemekleri;
    Button cuisineSearchTatlı;
    Button cuisineSearchPizza;
    Button cuisineSearchBurger;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recyclersearch);
        cuisineSearchBalık =view.findViewById(R.id.cuisineFish);
        cuisineSearchÇiğKöfte=view.findViewById(R.id.cuisineKöfte);
        cuisineSearchEvYemekleri=view.findViewById(R.id.cuisineDiet);
        cuisineSearchTatlı =view.findViewById(R.id.cuisineDonut);
        cuisineSearchPizza=view.findViewById(R.id.cuisinePizza);
        cuisineSearchBurger=view.findViewById(R.id.cuisineBurger);

        return view;
    }

    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();
        searchView.setBackgroundColor(Color.BLACK);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return false;
            }
        });

        cuisineSearchBalık.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisineSearch(cuisineSearchBalık);
                Toast.makeText(getContext(),"Balık restoranları gösteriliyor",Toast.LENGTH_SHORT).show();
            }
        });
        cuisineSearchBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisineSearch(cuisineSearchBurger);
                Toast.makeText(getContext(),"Burger restoranları gösteriliyor",Toast.LENGTH_SHORT).show();

            }
        });
        cuisineSearchEvYemekleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisineSearch(cuisineSearchEvYemekleri);
                Toast.makeText(getContext(),"Ev Yemekleri restoranları gösteriliyor",Toast.LENGTH_SHORT).show();

            }
        });
        cuisineSearchTatlı.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisineSearch(cuisineSearchTatlı);
                Toast.makeText(getContext(),"Tatlı restoranları gösteriliyor",Toast.LENGTH_SHORT).show();

            }
        });
        cuisineSearchPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisineSearch(cuisineSearchPizza);
                Toast.makeText(getContext(),"Pizza restoranları gösteriliyor",Toast.LENGTH_SHORT).show();

            }
        });
        cuisineSearchÇiğKöfte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisineSearch(cuisineSearchÇiğKöfte);
                Toast.makeText(getContext(),"Çiğ Köfte restoranları gösteriliyor",Toast.LENGTH_SHORT).show();

            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }

    private void processSearch(String search) {
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        FirebaseRecyclerOptions<Restaurant> options = new FirebaseRecyclerOptions.Builder<Restaurant>()
                .setQuery(databaseReference.child("restaurants").orderByChild("restaurantName").startAt(search).endAt(search + "\uf8ff"), Restaurant.class)
                .build();
        searchAdapter = new RVSearchAdapter(options, getContext(), getActivity());
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);




    }
    private void cuisineSearch(Button button){
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Restaurant> options = new FirebaseRecyclerOptions.Builder<Restaurant>()
                .setQuery(databaseReference.child("restaurants").orderByChild("cuisine").equalTo(button.getText().toString()), Restaurant.class)
                .build();
        searchAdapter = new RVSearchAdapter(options, getContext(), getActivity());
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);

    }

}
