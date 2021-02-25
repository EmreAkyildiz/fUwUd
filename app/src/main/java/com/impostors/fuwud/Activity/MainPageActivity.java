package com.impostors.fuwud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.impostors.fuwud.Model.Product;
import com.impostors.fuwud.R;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        init();

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder,new FragmentOrder()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.actionOrder){
                    tempFragment = new FragmentOrder();
                }

                if(menuItem.getItemId() == R.id.actionSearch){
                    tempFragment = new FragmentSearch();
                }

                if(menuItem.getItemId() == R.id.actionBasket){
                    tempFragment = new FragmentBasket();
                }

                if(menuItem.getItemId() == R.id.actionOther){
                    tempFragment = new FragmentOther();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder,tempFragment).commit();

                return true;
            }
        });
    }

    public void init() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }
}