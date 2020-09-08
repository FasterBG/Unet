package com.example.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

public class FragmentHome extends androidx.fragment.app.Fragment {

    private FirebaseAuth mAuth;
    private View fragmentView;

    public FragmentHome(){

    }

    @Nullable
    @Override
    public android.view.View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable android.os.Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        return fragmentView;
    }
}

