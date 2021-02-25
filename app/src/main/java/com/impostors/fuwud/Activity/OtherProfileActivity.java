package com.impostors.fuwud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.impostors.fuwud.Model.Restaurant;
import com.impostors.fuwud.Model.User;
import com.impostors.fuwud.R;

import java.util.HashMap;
import java.util.Map;

public class OtherProfileActivity extends AppCompatActivity {

    private EditText editTextProfileName, editTextProfileSurname, editTextProfileBirth, editTextProfileEmail;
    private Button buttonChangePassword, buttonProfileUpdate;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        init();

        setDatePicker(editTextProfileBirth);
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChangePasswordPage();
            }
        });

        buttonProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInformation();
            }
        });
    }

    public void init() {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        editTextProfileName = findViewById(R.id.editTextProfileName);
        editTextProfileSurname = findViewById(R.id.editTextProfileSurname);
        editTextProfileBirth = findViewById(R.id.editTextProfileBirth);
        editTextProfileEmail = findViewById(R.id.editTextProfileEmail);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonProfileUpdate = findViewById(R.id.buttonProfileUpdate);

        Query queryForProfile = databaseReference.child("users").orderByKey().equalTo(currentUser.getUid());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        String name = user.getName();
                        String surname = user.getSurname();
                        String email = user.getEmail();
                        String birthday = user.getBirthday();

                        editTextProfileEmail.setText(email);
                        editTextProfileSurname.setText(surname);
                        editTextProfileName.setText(name);
                        editTextProfileBirth.setText(birthday);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        queryForProfile.addListenerForSingleValueEvent(valueEventListener);
    }


    public void goToChangePasswordPage() {
        Intent intent = new Intent(OtherProfileActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
        finish();
    }

    private void setDatePicker(final TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();

                Bundle b = new Bundle();
                b.putInt("customStyle", R.style.CustomDatePickerDialog);
                newFragment.setArguments(b);

                newFragment.setDatePickerListener(new DatePickerFragment.DatePickerListener() {
                    @Override
                    public void onDatePicked(String date) {
                        textView.setText(date);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    public void updateUserInformation() {
        Map<String,Object> updateInfo = new HashMap<>();
        updateInfo.put("name", editTextProfileName.getText().toString());
        updateInfo.put("surname", editTextProfileSurname.getText().toString());
        updateInfo.put("email", editTextProfileEmail.getText().toString());
        databaseReference.child("users").child(currentUser.getUid()).updateChildren(updateInfo);

        databaseReference.child("users").child(currentUser.getUid()).child("birthday").setValue(editTextProfileBirth.getText().toString());
    }
}