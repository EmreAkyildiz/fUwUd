package com.impostors.fuwud.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class BaseActivity extends AppCompatActivity {


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


}