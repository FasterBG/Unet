package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class AddPostActivity extends AppCompatActivity implements FragmentAddPost1.OnDataPassTo2 {

    private ImageButton backBtn, nextBtn;
    private Uri fileUri;
    private String layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentAddPost1()).commit();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onDataPassTo2(Uri fileUri, String layout) {
        fileUri = fileUri;
        layout = layout;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentAddPost2()).commit();
    }
}