package com.impostors.fuwud.Activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.impostors.fuwud.R;

import java.util.HashMap;

public class OtherAddressesActivity extends AppCompatActivity implements LocationListener {
    Button buttonSaveAddress;
    TextView textViewLongitude,textViewLatitude;
    Double currentLatitude=0.0,currentLongitude=0.0;
    private LocationManager locationManager;
    private int checkForPermission;
    private String locationProvider = "gps";
    Location loc;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_addresses);

        init();

        buttonSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                HashMap<String,Object> updateInfo = new HashMap();
                updateInfo.put("latitude", currentLatitude);
                updateInfo.put("longitude",currentLongitude);
                databaseReference.child("users").child(currentUser.getUid()).updateChildren(updateInfo);
                Toast.makeText(getBaseContext(), "Lokasyon Kaydedildi.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void init(){
        textViewLongitude=findViewById(R.id.textViewLongitude);
        textViewLatitude=findViewById(R.id.textViewLatitude);
        buttonSaveAddress=findViewById(R.id.buttonSaveAddress);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        textViewLatitude.setText(currentLatitude+"");
        textViewLongitude.setText(currentLongitude+"");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void getLocation(){

        checkForPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(checkForPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }else
        {
            locationManager = (LocationManager) this
                    .getSystemService(Context.LOCATION_SERVICE);
            boolean checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (checkGPS) {
                if (loc == null) { // eğer bir kere loc aldıysak 2.ye gerek yok o kadar hızlı yer değiştiremez çünkü değişmeme sebebi bu
                    try {

                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                10,// locationu değiştirmek için kaç ms gerek
                                10, this);//locationu update etmek için kaç metre uzaklık gerek
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (loc != null) {
                                onLocationChanged(loc);
                            }
                        }
                    } catch (SecurityException e) {

                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100){

            checkForPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) this
                        .getSystemService(Context.LOCATION_SERVICE);
                boolean checkGPS = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (checkGPS) {
                    if (loc == null) { // eğer bir kere loc aldıysak 2.ye gerek yok o kadar hızlı yer değiştiremez çünkü değişmeme sebebi bu
                        try {

                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    10, // locationu değiştirmek için kaç ms gerek
                                    10, this);//locationu update etmek için kaç metre uzaklık gerek
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                loc = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                if (loc != null) {
                                    onLocationChanged(loc);
                                    HashMap<String,Object> updateInfo = new HashMap();
                                    updateInfo.put("latitude", currentLatitude);
                                    updateInfo.put("longitude",currentLongitude);
                                    databaseReference.child("users").child(currentUser.getUid()).updateChildren(updateInfo);
                                    Toast.makeText(this, "Lokasyon Kaydedildi.", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (SecurityException e) {

                        }
                    }
                }

            } else {
                Log.e("location", "İzin Verilmedi");

            }

        }
    }









}