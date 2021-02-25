package com.impostors.fuwud.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.impostors.fuwud.R;

public class FragmentOther extends Fragment {

    private Button buttonProfile, buttonPreviousOrders, buttonFavorites, buttonAddresses, buttonPayments, buttonAboutApp, buttonSignOut;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        buttonProfile = view.findViewById(R.id.buttonProfile);
        buttonPreviousOrders = view.findViewById(R.id.buttonPreviousOrders);
        buttonFavorites = view.findViewById(R.id.buttonFavorites);
        buttonAddresses = view.findViewById(R.id.buttonAddresses);
        buttonAboutApp = view.findViewById(R.id.buttonAboutApp);
        buttonSignOut = view.findViewById(R.id.buttonSignOut);

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutClicked();
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfilePage();
            }
        });

        buttonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFavoritesPage();
            }
        });

        buttonAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddressesPage();
            }
        });


        buttonAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAboutAppPage();
            }
        });
        buttonPreviousOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreviousOrdersPage();
            }
        });
        return view;
    }

    public void signOutClicked() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void goToProfilePage() {
        Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
        startActivity(intent);
    }

    public void goToPreviousOrdersPage() {
        Intent intent = new Intent(getActivity(), OtherPreviousOrdersActivity.class);
        startActivity(intent);
    }

    public void goToFavoritesPage() {
        Intent intent = new Intent(getActivity(), OtherFavoritesActivity.class);
        startActivity(intent);
    }

    public void goToAddressesPage() {
        Intent intent = new Intent(getActivity(), OtherAddressesActivity.class);
        startActivity(intent);
    }

    public void goToAboutAppPage() {
        Intent intent = new Intent(getActivity(), OtherAboutAppActivity.class);
        startActivity(intent);
    }

}
