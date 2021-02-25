package com.impostors.fuwud.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.impostors.fuwud.Adapter.RVRestaurantOrderAdapter;
import com.impostors.fuwud.Model.PrevOrder;
import com.impostors.fuwud.R;

public class FragmentRestaurantOrders extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    private RVRestaurantOrderAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel_orders, container, false);
        init(view);


        Query query=databaseReference.child("restaurants").child(currentUser.getUid()).child("orders");
        FirebaseRecyclerOptions<PrevOrder> options = new FirebaseRecyclerOptions.Builder<PrevOrder>()
                .setQuery(query,(PrevOrder.class)).build();


        adapter = new RVRestaurantOrderAdapter(options);
        recyclerView.setAdapter(adapter);


        return view;
    }



    private void init(View view){
        recyclerView = view.findViewById(R.id.RecyclerViewRestaurantOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();


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
