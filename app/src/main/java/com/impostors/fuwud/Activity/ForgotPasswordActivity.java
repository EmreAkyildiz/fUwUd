package com.impostors.fuwud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.impostors.fuwud.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextForgotPassword;
    private Button buttonSendForgotPassword;
    private CheckBox checkBoxIsRobot;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();

    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        editTextForgotPassword = findViewById(R.id.editTextForgotPassword);
        buttonSendForgotPassword = findViewById(R.id.buttonSendForgotPassword);
        checkBoxIsRobot = findViewById(R.id.checkBoxIsRobot);
    }

    public void checkbox_clicked(View v) {

        if (checkBoxIsRobot.isChecked()) {
            buttonSendForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = editTextForgotPassword.getText().toString();
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(ForgotPasswordActivity.this, "You need to enter a valid Email!", Toast.LENGTH_LONG).show();
                    } else {
                        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Please check your email!", Toast.LENGTH_LONG).show();
                                    Intent forgot_login_intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                    startActivity(forgot_login_intent);
                                } else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(ForgotPasswordActivity.this, "Error occurred : " + message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}