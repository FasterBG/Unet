package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.mikhaellopez.circularimageview.CircularImageView;

public class DefaultActivity extends AppCompatActivity {

    private Button logout;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private CircularImageView profilePicture;
    private ImageView addContentBtn;
    private ImageButton talksBtn;

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

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent toProfileActivity = new Intent(DefaultActivity.this, ProfileActivity.class);
                startActivity(toProfileActivity);
                 */
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
                        // go to add Image Activity
                    }
                });

                videoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // go to add Video Activity
                    }
                });

                storyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // go to add Story Activity
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
}