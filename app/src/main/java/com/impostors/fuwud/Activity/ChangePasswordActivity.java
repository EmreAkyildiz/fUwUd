package com.impostors.fuwud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.impostors.fuwud.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText editTextCurrentPassword, editTextNewPassword, editTextNewPasswordAgain;
    private Button buttonChangePasswordUpdate;

    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        init();

        buttonChangePasswordUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    public void init() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        editTextCurrentPassword = findViewById(R.id.editTextCurrentPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextNewPasswordAgain = findViewById(R.id.editTextNewPasswordAgain);
        buttonChangePasswordUpdate = findViewById(R.id.buttonChangePasswordUpdate);
    }

    public void changePassword() {
        if (currentUser != null) {
            String currentPassword = editTextCurrentPassword.getText().toString();
            final String newPassword = editTextNewPassword.getText().toString();
            String newPasswordAgain = editTextNewPasswordAgain.getText().toString();
            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(newPasswordAgain) || TextUtils.isEmpty(currentPassword)) {
                Toast.makeText(this, "Current Password or Password or ConfirmPassword can not be empty!", Toast.LENGTH_LONG).show();
            } else if (!newPassword.equals(newPasswordAgain)) {
                Toast.makeText(this, "Password and ConfirmPassword must be the same!", Toast.LENGTH_LONG).show();
            } else {
                AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);
                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangePasswordActivity.this, "Your password has been changed.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ChangePasswordActivity.this, OtherProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, "Error!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        } else {
            Toast.makeText(ChangePasswordActivity.this, "A user must be logged in!", Toast.LENGTH_LONG).show();
        }
    }
}
