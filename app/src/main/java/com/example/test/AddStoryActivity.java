package com.example.test;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewDebug;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.Policy;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddStoryActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    public static final int PICKFILE_RESULT_CODE = 1;

    private Uri mImageUri;
    String myUrl = "";
    private StorageTask storageTask;
    StorageReference storageReference;
    private Camera camera;
    private Camera.PictureCallback pictureCallback;
    private SurfaceHolder surfaceHolder;
    private SurfaceView mSurfaceView;
    private ImageButton mShootButton;
    private ImageButton mStorageButton;
    private String contentType;
    private FirebaseDatabase database;

    private int CAMERA_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        mSurfaceView = findViewById(R.id.surfaceView);
        mShootButton = findViewById(R.id.shootBtn);
        mStorageButton = findViewById(R.id.storageBtn);

        contentType = getIntent().getStringExtra("contentType");


        if (ContextCompat.checkSelfPermission(AddStoryActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(AddStoryActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        storageReference = FirebaseStorage.getInstance().getReference("story");

        mStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });

        mShootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, pictureCallback);
            }
        });

        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap cbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), null, true);
                String pathFileName = currentDataFormat();
                mImageUri = getImageUri(AddStoryActivity.this, cbmp);
                publishStory(storePhoto(cbmp, pathFileName));
            }
        };

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private byte[] storePhoto(Bitmap cbmp, String pathFileName){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cbmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

    private String currentDataFormat(){
        return "SMTH";
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void publishStory(final byte[] data){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();

        if (mImageUri != null){
            final StorageReference imageReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

            storageTask = imageReference.putFile(mImageUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Task<Uri> then(@NonNull @NotNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()){
                        Toast.makeText(AddStoryActivity.this, "Successful task!", Toast.LENGTH_LONG);

                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();

                        String myid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stories").child(myid);

                        String storyid = reference.push().getKey();
                        long timeend = System.currentTimeMillis() + 86400000; // 24 h

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageurl", myUrl);
                        hashMap.put("timeStart", ServerValue.TIMESTAMP);
                        hashMap.put("timeend", timeend);
                        hashMap.put("storyid", storyid);
                        hashMap.put("userid", myid);

                        reference.child(storyid).setValue(hashMap);
                        pd.dismiss();

                        finish();
                    }else{
                        Toast.makeText(AddStoryActivity.this, "Unsuccessful task!", Toast.LENGTH_LONG);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(AddStoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No Image Selected!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            camera = Camera.open();
        }catch (Exception e){
            Log.d("tag", "unsuccessful camera init");
        }

        Camera.Parameters parameters;
        parameters = camera.getParameters();
        List<Camera.Size> sizes =  parameters.getSupportedPictureSizes();
        Camera.Size size = sizes.get(0);
        parameters.setPictureSize(size.width, size.height);
        parameters.setPreviewFrameRate(20);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try{
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        switch (requestCode){
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1){

                }
        }
    }
}