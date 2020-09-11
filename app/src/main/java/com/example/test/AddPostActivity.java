package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class AddPostActivity extends AppCompatActivity {

    private ImageButton backBtn, nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentAddPost1()).commit();

    }
}