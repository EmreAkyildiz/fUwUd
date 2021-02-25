package com.impostors.fuwud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.impostors.fuwud.R;

public class RestaurantPanelActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restauran_panel);

        init();

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder,new FragmentRestaurantMenu()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.actionEditMenu){
                    tempFragment = new FragmentRestaurantMenu();
                }

                if(menuItem.getItemId() == R.id.actionOrders){
                    tempFragment = new FragmentRestaurantOrders();
                }

                if(menuItem.getItemId() == R.id.actionComments){
                    tempFragment = new FragmentRestaurantComments();
                }

                if(menuItem.getItemId() == R.id.actionExit){
                    FirebaseAuth.getInstance().signOut();
                    Intent to_login_intent = new Intent(RestaurantPanelActivity.this, LoginActivity.class);
                    startActivity(to_login_intent);
                    finish();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder,tempFragment).commit();

                return true;
            }
        });
    }

    public void init() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_panel);
    }
}