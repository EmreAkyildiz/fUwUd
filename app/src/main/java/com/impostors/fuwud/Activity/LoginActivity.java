package com.impostors.fuwud.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.impostors.fuwud.R;


public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister, buttonRegisterRestaurant;
    private ProgressDialog progressDialog;
    private TextView textViewForgotPassword;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();

        if (auth.getCurrentUser() != null) {
            onAuthSuccess(auth.getCurrentUser());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInClicked();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpClicked();
            }
        });
        buttonRegisterRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_restaurant_intent = new Intent(LoginActivity.this, RegistrationRestaurantActivity.class);
                startActivity(register_restaurant_intent);
            }
        });
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register_restaurant_intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(register_restaurant_intent);
            }
        });
    }

    public void init() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegisterRestaurant = findViewById(R.id.buttonRegisterRestaurant);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
    }

    public void signInClicked() {
        final String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email Alanı Boş Olamaz!");
            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Şifre Alanı Boş Olamaz!");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Şifre Alanı Boş Olamaz!");
            return;
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onAuthSuccess(task.getResult().getUser());
                } else {
                    Toast.makeText(LoginActivity.this, "Email veya Şifre Hatalı!", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });
    }

    private void onAuthSuccess(FirebaseUser user) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Giriş Yapılıyor...");
        progressDialog.setCancelable(false);
        currentUser=auth.getCurrentUser();
        Query queryForCheckRestaurant=databaseReference.child("restaurants").orderByKey().equalTo(currentUser.getUid());
        queryForCheckRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Intent to_main_intent = new Intent(LoginActivity.this, RestaurantPanelActivity.class);
                        progressDialog.show();
                        startActivity(to_main_intent);
                        finish();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query queryForCheckUser=databaseReference.child("users").orderByKey().equalTo(currentUser.getUid());
        queryForCheckUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Intent to_main_intent = new Intent(LoginActivity.this, MainPageActivity.class);
                        progressDialog.show();
                        startActivity(to_main_intent);
                        finish();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void signUpClicked() {
        Intent register_page_intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(register_page_intent);
    }
}