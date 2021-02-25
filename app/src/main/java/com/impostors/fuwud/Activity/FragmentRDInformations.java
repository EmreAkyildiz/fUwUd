package com.impostors.fuwud.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.impostors.fuwud.R;

public class FragmentRDInformations extends Fragment {
    EditText editTextRDInfoName, editTextRDInfoPlaceX, editTextRDInfoPlaceY, editTextRDInfoPhoneNumber, editTextRDInfoEmail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_r_d_informations, container, false);

        editTextRDInfoName=view.findViewById(R.id.editTextRDInfoName);
        editTextRDInfoPlaceX=view.findViewById(R.id.editTextRDInfoPlaceX);
        editTextRDInfoPlaceY=view.findViewById(R.id.editTextRDInfoPlaceY);
        editTextRDInfoPhoneNumber=view.findViewById(R.id.editTextRDInfoPhoneNumber);
        editTextRDInfoEmail=view.findViewById(R.id.editTextRDInfoEmail);

        final String restaurantName=getActivity().getIntent().getStringExtra("restaurantName");
        final String restaurantEmail=getActivity().getIntent().getStringExtra("restaurantEmail");
/*      final double restaurantLatitude=getActivity().getIntent().getDoubleExtra("restaurantLatitude",0.0);
        final double restaurantLongitude=getActivity().getIntent().getDoubleExtra("restaurantLongitude",0.0);*/
        final String restaurantPhone=getActivity().getIntent().getStringExtra("restaurantPhone");

        editTextRDInfoPhoneNumber.setText(restaurantPhone);
        editTextRDInfoName.setText(restaurantName);
/*      textViewLatitude.setText(String.valueOf(restaurantLatitude));
        textViewLongitude.setText(String.valueOf(restaurantLongitude));*/
        editTextRDInfoEmail.setText(restaurantEmail);


        return view;
    }
}