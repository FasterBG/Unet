package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class DefaultActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button logout;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private CircularImageView profilePicture;
    private ImageView addContentBtn;
    private ImageButton talksBtn;

    private MapboxMap mapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentMap()).commit();

        profilePicture = findViewById(R.id.profilePciture);
        addContentBtn = findViewById(R.id.addContentBtn);
        talksBtn = findViewById(R.id.talksBtn);

        final DatabaseReference profilePictureReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
        profilePictureReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("profilePictureUrl")) {
                    Picasso.get().load(snapshot.getValue().toString()).into(profilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentProfile()).commit();
            }
        });

        addContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mDialog = new AlertDialog.Builder(DefaultActivity.this);
                View dialogView = LayoutInflater.from(DefaultActivity.this).inflate(R.layout.alert_dialog_add_content, null);
                mDialog.setView(dialogView);
                final  AlertDialog dialog = mDialog.show();

                Button imageBtn, videoBtn, storyBtn;
                imageBtn = dialogView.findViewById(R.id.imageBtn);
                videoBtn = dialogView.findViewById(R.id.videoBtn);
                storyBtn = dialogView.findViewById(R.id.storyBtn);

                imageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toAddPostActivity = new Intent(DefaultActivity.this, AddPostActivity.class);
                        toAddPostActivity.putExtra("contentType", "image");
                        startActivity(toAddPostActivity);
                    }
                });

                videoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toAddPostActivity = new Intent(DefaultActivity.this, AddPostActivity.class);
                        toAddPostActivity.putExtra("contentType", "video");
                        startActivity(toAddPostActivity);
                    }
                });

                storyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toAddStoryActivity = new Intent(DefaultActivity.this, AddStoryActivity.class);
                        toAddStoryActivity.putExtra("contentType", "story");
                        startActivity(toAddStoryActivity);
                    }
                });
            }
        });

        talksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_talks_activity = new Intent(DefaultActivity.this, TalksActivity.class);
                startActivity(to_talks_activity);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/vents04/ckafl3aeg1ldx1irplfokb1uw"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

            }
        });

    }
}