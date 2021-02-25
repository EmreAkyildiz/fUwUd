package com.impostors.fuwud.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
import com.impostors.fuwud.Activity.LoginActivity;
import com.impostors.fuwud.Model.Product;
import com.impostors.fuwud.Model.User;
import com.impostors.fuwud.R;

public class RVRDMenuAdapter extends FirebaseRecyclerAdapter<Product,RVRDMenuAdapter.RDcardMenuItemHolder> {
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private String restaurant_id;
    private Boolean flag;
    Context context;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public RVRDMenuAdapter(@NonNull FirebaseRecyclerOptions<Product> options,String restaurant_id, Context context) {

        super(options);
        this.restaurant_id=restaurant_id;
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RDcardMenuItemHolder rDcardMenuItemHolder, int i, @NonNull Product product) {
        //her itemi setleme
        rDcardMenuItemHolder.textViewProductRDName.setText(product.getName());
        rDcardMenuItemHolder.textViewProductRDPrice.setText(product.getBuyPrice()+"");

    }

    @NonNull
    @Override
    public RVRDMenuAdapter.RDcardMenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //card bağlantısı
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restaurant_menu_item, parent, false);
        return new RVRDMenuAdapter.RDcardMenuItemHolder(itemView);
    }

    public class RDcardMenuItemHolder extends RecyclerView.ViewHolder {

        public TextView textViewProductRDName, textViewProductRDPrice;
        public ImageButton imageButtonRDBuy;
        public CardView cardView;
        int count=1;

        //Constructor
        public RDcardMenuItemHolder(final View view) {
            super(view);

            cardView = view.findViewById(R.id.CardView);
            textViewProductRDPrice = (TextView) view.findViewById(R.id.textViewProductRDPrice);
            textViewProductRDName = (TextView) view.findViewById(R.id.textViewProductRDName);
            imageButtonRDBuy = (ImageButton) view.findViewById(R.id.imageButtonRDBuy);
            imageButtonRDBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.product_dialog_box);
                    dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_box_background));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.animationDialog;

                    Button buttonDialogCancel= dialog.findViewById(R.id.buttonDialogCancel);
                    Button buttonDialogAddToBasket = dialog.findViewById(R.id.buttonDialogAddToBasket);
                    final TextView textViewDialogProductNumber = dialog.findViewById(R.id.textViewDialogProductNumber);
                    ImageButton imageButtonDialogRemove = dialog.findViewById(R.id.imageButtonDialogRemove);
                    ImageButton imageButtonDialogAdd = dialog.findViewById(R.id.imageButtonDialogAdd);

                    imageButtonDialogAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (count >= 1){
                                count++;
                                textViewDialogProductNumber.setText(String.valueOf(count));
                            }
                        }
                    });

                    imageButtonDialogRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (count > 1){
                                count--;
                                textViewDialogProductNumber.setText(String.valueOf(count));
                            }
                        }
                    });

                    dialog.show();

                    buttonDialogAddToBasket.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addToBasket(getItem(getAdapterPosition()),count);

                            dialog.dismiss();
                        }
                    });

                    buttonDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    private void addToBasket(final Product product, final int count){
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        flag = true;
        Query queryForCheck=databaseReference.child("users").child(currentUser.getUid()).child("currentBasket");
        queryForCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d:snapshot.getChildren()){
                    Product p=d.getValue(Product.class);

                    if(!p.getRestaurant_id().equals(restaurant_id)){
                        flag =false;
                    }

                }
                if(flag) {
                    product.setCount(count); // modified
                    databaseReference.child("users").child(currentUser.getUid()).child("currentBasket").push().setValue(product);
                    Toast.makeText(context, "Ürün Sepete Eklendi", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(context, "Sadece 1 restauranttan sipariş verebilirsin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
