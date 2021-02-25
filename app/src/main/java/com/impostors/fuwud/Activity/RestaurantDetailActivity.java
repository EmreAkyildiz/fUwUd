package com.impostors.fuwud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.impostors.fuwud.R;

public class RestaurantDetailActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment tempFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        tempFragment = new FragmentRDMenu();
        init();


        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolderRestaurantDetail,new FragmentRDMenu()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.actionMenu){
                    tempFragment = new FragmentRDMenu();
                }

                if(menuItem.getItemId() == R.id.actionInformations){
                    tempFragment = new FragmentRDInformations();
                }

                if(menuItem.getItemId() == R.id.actionRestaurantComments){
                    tempFragment = new FragmentRDComments();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolderRestaurantDetail,tempFragment).commit();

                return true;
            }
        });
    }

    public void init() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_restaurantDetail);
    }
}