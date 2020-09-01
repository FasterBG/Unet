package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private EditText email, password;
    private Button sendRequestBtn;
    private TextView toLogin;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Logging...");

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        toLogin = findViewById(R.id.toLogin);
        sendRequestBtn = findViewById(R.id.sendRequestBtn);

        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(SignupActivity.this, "All inputs should not be empty.", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().length() < 8){
                    Toast.makeText(SignupActivity.this, "Password should be longer than 8 symbols.", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Log.d("SIGNUP RESULT:", "createUserWithEmail:success");
                                        Intent toDefaultActivity = new Intent(SignupActivity.this, DefaultActivity.class);
                                        startActivity(toDefaultActivity);
                                        finish();
                                    } else {
                                        Log.w("SIGNUP RESULT:", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SignupActivity.this, "Signing up failed. Try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginActivity = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(toLoginActivity);
                finish();
            }
        });
    }
}