package com.impostors.fuwud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.impostors.fuwud.R;

import java.util.Date;

public class PaymentActivity extends AppCompatActivity {
    TextView textViewPaymentPrice;
    EditText editTextCartCode,editTextCartValidDate;
    Button buttonPaymentNext;
    String restaurantId;
    Spinner spinnerDeliveryDate, spinnerPaymentMethod;


    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    static String USER_NAME = "fuwudapp@gmail.com";
    static String PASSWORD = "Batuhanemremicikmert123.";
    static String HOST = "smtp.gmail.com";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();
        textViewPaymentPrice.setText(getIntent().getDoubleExtra("price",0)+"");
        restaurantId=getIntent().getStringExtra("restaurantId");
        buttonPaymentNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!spinnerDeliveryDate.getSelectedItem().toString().equals("")&&!spinnerPaymentMethod.getSelectedItem().toString().equals("")&&getIntent().getDoubleExtra("price",0)!=0
                &&restaurantId!=null){
                    Toast.makeText(getApplicationContext(),"Ödeme isteğin restauranta yönlendirildi.",Toast.LENGTH_LONG).show();
                    PrevOrder prevOrder=new PrevOrder();
                    prevOrder.setCompleted(false);
                    prevOrder.setRestaurant(getIntent().getStringExtra("restaurantName"));
                    prevOrder.setOwnerUid(currentUser.getUid());
                    prevOrder.setPrice(getIntent().getDoubleExtra("price",0));
                    prevOrder.setDate(new Date());
                    setOrdersAndDelete(prevOrder);

                    //TODO restaurant için prevOrderden complete kısmını true veya false yapması için istek yarat


                }

            }
        });

        ArrayAdapter<String> adapterDeliveryDate= new ArrayAdapter<>(
                this,
                R.layout.spinner_deliverydate,
                getResources().getStringArray(R.array.teslimtarihi)
        );
        adapterDeliveryDate.setDropDownViewResource(R.layout.spinner_dropdown_delivery);
        spinnerDeliveryDate.setAdapter(adapterDeliveryDate);

        spinnerDeliveryDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem= spinnerDeliveryDate.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapterPaymentMethod= new ArrayAdapter<>(
                this,
                R.layout.spinner_paymentmethod,
                getResources().getStringArray(R.array.ödemeşekli)
        );
        adapterPaymentMethod.setDropDownViewResource(R.layout.spinner_dropdown_payment);
        spinnerPaymentMethod.setAdapter(adapterPaymentMethod);

        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem= spinnerPaymentMethod.getItemAtPosition(i).toString();
                if(selectedItem.equals("İleri Tarihe")){

                }
                Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void setOrdersAndDelete(final PrevOrder prevOrder){  //TODO ABİSİ bunu silicem sonra buraya listener şeklinde olmayan productları prevOrdere EKLEYECEM
        Query query=databaseReference.child("users").child(currentUser.getUid()).child("currentBasket");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d:snapshot.getChildren()){
                    prevOrder.getProducts().put(d.getKey(),d.getValue(Product.class));

                }
                databaseReference.child("restaurants").child(restaurantId).child("orders").push().setValue(prevOrder);
                Intent payment_main_intent = new Intent(PaymentActivity.this, MainPageActivity.class);
                startActivity(payment_main_intent);
                finish();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("users").child(currentUser.getUid()).child("currentBasket").removeValue();

    }

    private void init(){
        textViewPaymentPrice=findViewById(R.id.textViewPaymentPrice);
        /*editTextCartCode=findViewById(R.id.editTextCartCode);
        editTextCartValidDate=findViewById(R.id.editTextCartValidDate);*/
        buttonPaymentNext=findViewById(R.id.buttonPaymentNext);
        spinnerDeliveryDate=findViewById(R.id.spinnerDeliveryDate);
        spinnerPaymentMethod=findViewById(R.id.spinnerPaymentMethod);



        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();


    }
}