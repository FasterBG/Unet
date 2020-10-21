package com.example.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.view.View.VISIBLE;

public class FragmentMap extends androidx.fragment.app.Fragment implements OnMapReadyCallback {

    private FirebaseAuth mAuth;
    private View fragmentView;

    private GoogleMap map;

    public FragmentMap(){

    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable android.os.Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return fragmentView;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.string.dark_neon));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        LatLng Maharashtra = new LatLng(19.169257, 73.341601);
        map.addMarker(new MarkerOptions().position(Maharashtra).title("Maharashtra"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Maharashtra));
    }
}

