package com.impostors.fuwud.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.impostors.fuwud.Model.Restaurant;
import com.impostors.fuwud.R;

public class RegistrationRestaurantActivity extends AppCompatActivity {

    private EditText editTextRestaurantEmail, editTextRestaurantPassword, editTextRestaurantName, editTextBusinessPhoneNumber;
    private Button buttonSelectLocation, buttonSignUpRestaurant;
    private Spinner spinnerCuisine;
    private String cuisine;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_registration);

        init();
        getIntentExtras();

        buttonSignUpRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpClicked();
            }
        });
        buttonSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocationClicked();
            }
        });


    }

    public void init() {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        spinnerCuisine = findViewById(R.id.spinnerCuisine);
        editTextRestaurantName = findViewById(R.id.editTextRestaurantName);
        editTextRestaurantEmail = findViewById(R.id.editTextRestaurantEmail);
        editTextRestaurantPassword = findViewById(R.id.editTextRestaurantPassword);
        editTextBusinessPhoneNumber = findViewById(R.id.editTextBusinessPhoneNumber);
        buttonSelectLocation = findViewById(R.id.buttonSelectLocation);
        buttonSignUpRestaurant = findViewById(R.id.buttonSignUpRestaurant);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.mutfaklar  , R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCuisine.setAdapter(adapter);
    }

    public void signUpClicked() {
        final String email = editTextRestaurantEmail.getText().toString();
        final String password = editTextRestaurantPassword.getText().toString();
        final String name = editTextRestaurantName.getText().toString();
        final String phoneNumber = editTextBusinessPhoneNumber.getText().toString();
        final Double longtitude = getIntent().getDoubleExtra("longitude", -1);
        final Double latitude = getIntent().getDoubleExtra("latitude", -1);
        cuisine = spinnerCuisine.getSelectedItem().toString();


        progressDialog = new ProgressDialog(RegistrationRestaurantActivity.this);
        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegistrationRestaurantActivity.this, "Tüm Alanlar Gereklidir", Toast.LENGTH_LONG).show();
        } else if (password.length() < 6) {
            Toast.makeText(RegistrationRestaurantActivity.this, "Şifre en az 6 karakter olmalı!", Toast.LENGTH_LONG).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegistrationRestaurantActivity.this, "Geçerli bir mail girmediniz!", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setMessage("Lütfen Bekleyin...");
            progressDialog.show();


            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegistrationRestaurantActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseUser = auth.getCurrentUser();
                                String restaurant_id = firebaseUser.getUid();

                                databaseReference = FirebaseDatabase.getInstance().getReference();

                                Restaurant restaurant = new Restaurant(name, email, phoneNumber, restaurant_id, longtitude, latitude, cuisine);
                                databaseReference.child("restaurants").child(restaurant_id).setValue(restaurant);

                                progressDialog.dismiss();
                                Intent to_main_intent = new Intent(RegistrationRestaurantActivity.this, RestaurantPanelActivity.class);
                                startActivity(to_main_intent);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationRestaurantActivity.this, "Girdiğiniz Email ve Şifre ile giriş yapılamamaktadır.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


        }


    }

    private void selectLocationClicked() {
        Intent to_map_intent = new Intent(RegistrationRestaurantActivity.this, RestaurantMapActivity.class);
        to_map_intent.putExtra("restaurantEmail", editTextRestaurantEmail.getText().toString());
        to_map_intent.putExtra("restaurantName", editTextRestaurantName.getText().toString());
        to_map_intent.putExtra("restaurantBusinessPhoneNumber", editTextBusinessPhoneNumber.getText().toString());
        startActivity(to_map_intent);
        finish();
    }

    private void getIntentExtras() {
        editTextRestaurantEmail.setText(getIntent().getStringExtra("restaurantEmail"));
        editTextRestaurantName.setText(getIntent().getStringExtra("restaurantName"));
        editTextBusinessPhoneNumber.setText(getIntent().getStringExtra("restaurantBusinessPhoneNumber"));


        Double longitude = getIntent().getDoubleExtra("longitude", 0);
        Double latitude = getIntent().getDoubleExtra("latitude", 0);
        if (latitude != 0 && longitude != 0) {
            buttonSelectLocation.setText(latitude + " " + longitude);
        }

    }



}


