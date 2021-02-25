package com.impostors.fuwud.Activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.impostors.fuwud.R;

public class RestaurantMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker marker;
    private LatLng currentPosition;
    private Button buttonLocatePlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonLocatePlace = findViewById(R.id.buttonLocatePlace);

        buttonLocatePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registration_restaurant_intent = new Intent(RestaurantMapActivity.this, RegistrationRestaurantActivity.class);

                registration_restaurant_intent.putExtra("latitude",currentPosition.latitude);
                registration_restaurant_intent.putExtra("longitude",currentPosition.longitude);

                registration_restaurant_intent.putExtra("restaurantEmail",getIntent().getStringExtra("restaurantEmail"));
                registration_restaurant_intent.putExtra("restaurantName",getIntent().getStringExtra("restaurantName"));
                registration_restaurant_intent.putExtra("restaurantBusinessPhoneNumber",getIntent().getStringExtra("restaurantBusinessPhoneNumber"));
                startActivity(registration_restaurant_intent);
                finish();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        currentPosition = new LatLng(40.986883, 29.121839);
        marker = mMap.addMarker(new MarkerOptions().position(currentPosition).draggable(true).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                currentPosition = marker.getPosition();
                Toast.makeText(getApplicationContext(), currentPosition.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }


}