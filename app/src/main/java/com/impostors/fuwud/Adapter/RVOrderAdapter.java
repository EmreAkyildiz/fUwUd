package com.impostors.fuwud.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.ValueEventListener;
import com.impostors.fuwud.Model.PrevOrder;

import com.impostors.fuwud.R;

public class RVOrderAdapter extends FirebaseRecyclerAdapter<PrevOrder, RVOrderAdapter.viewholder> {

    public RVOrderAdapter(@NonNull FirebaseRecyclerOptions<PrevOrder> options) {
        super(options);


    }

    @Override
    protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull PrevOrder prevOrder) {



        viewholder.Date1.setText(String.valueOf(prevOrder.getDate()));
        viewholder.productPrice1.setText(String.valueOf(prevOrder.getPrice())+" TL");
        viewholder.restaurantName1.setText(String.valueOf(prevOrder.getRestaurant()));


    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_cart,parent,false);
        return new viewholder(view);
    }

    class viewholder extends RecyclerView.ViewHolder{
        TextView Date1,productPrice1,restaurantName1;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            Date1= itemView.findViewById(R.id.Date1);
            productPrice1 = itemView.findViewById(R.id.price1);
            restaurantName1 = itemView.findViewById((R.id.restt));

        }
    }
}
