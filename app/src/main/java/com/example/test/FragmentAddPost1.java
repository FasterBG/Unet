package com.example.test;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;

public class FragmentAddPost1 extends androidx.fragment.app.Fragment {

    public static final int PICKFILE_RESULT_CODE = 1;

    private FirebaseAuth mAuth;
    private View fragmentView;
    private String contentType;

    private Button firstBtn, secondBtn, thirdBtn;

    private TextView fileTextView;
    private ImageView file;
    private Uri fileUri;
    private String filePath;

    public FragmentAddPost1(){

    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable android.os.Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_add_post_1, container, false);

        contentType = getActivity().getIntent().getStringExtra("contentType");

        file = fragmentView.findViewById(R.id.file);
        fileTextView = fragmentView.findViewById(R.id.fileTextView);
        firstBtn = fragmentView.findViewById(R.id.firstBtn);;
        secondBtn = fragmentView.findViewById(R.id.secondBtn);
        thirdBtn = fragmentView.findViewById(R.id.thirdBtn);

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType(contentType + "/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file.requestLayout();
                file.getLayoutParams().height = 1080;
                firstBtn.setBackgroundResource(R.drawable.layout_button_border);
                secondBtn.setBackgroundColor(Color.parseColor("#fafafa"));
                thirdBtn.setBackgroundColor(Color.parseColor("#fafafa"));
            }
        });

        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file.requestLayout();
                file.getLayoutParams().height = ((1080*9)/16);
                secondBtn.setBackgroundResource(R.drawable.layout_button_border);
                firstBtn.setBackgroundColor(Color.parseColor("#fafafa"));
                thirdBtn.setBackgroundColor(Color.parseColor("#fafafa"));
            }
        });

        thirdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file.requestLayout();
                file.getLayoutParams().height = 1350;
                thirdBtn.setBackgroundResource(R.drawable.layout_button_border);
                firstBtn.setBackgroundColor(Color.parseColor("#fafafa"));
                secondBtn.setBackgroundColor(Color.parseColor("#fafafa"));
            }
        });

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    filePath = fileUri.getPath();
                    file.setImageURI(fileUri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    try{
                        BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(fileUri), null, options);
                    }
                    catch (FileNotFoundException e){

                    }
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    if(imageHeight > imageWidth){
                        file.getLayoutParams().height = 1350;
                    }else if(imageWidth*9 == imageHeight*16){
                        file.getLayoutParams().height = ((1080*9)/16);
                    }else{
                        file.getLayoutParams().height = 1080;
                    }
                    fileTextView.setVisibility(View.GONE);
                }
                break;
        }
    }
}

