package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

public class FragmentAddPost2 extends androidx.fragment.app.Fragment {

    private FirebaseAuth mAuth;
    private View fragmentView;

    private ImageButton nextBtn;
    private CircularImageView profilePicture;

    public FragmentAddPost2(){

    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable android.os.Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_add_post_2, container, false);

        mAuth = FirebaseAuth.getInstance();

        nextBtn = getActivity().findViewById(R.id.nextBtn);
        profilePicture = fragmentView.findViewById(R.id.descriptionProfilePicture);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final DatabaseReference profilePictureReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
        profilePictureReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("profilePictureUrl")){
                    Picasso.get().load(snapshot.child("profilePictureUrl").getValue().toString()).into(profilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return fragmentView;
    }
}

