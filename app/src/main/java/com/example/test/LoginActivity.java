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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button sendRequestBtn;
    private TextView toSignup;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Logging...");

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        toSignup = findViewById(R.id.toSignup);
        sendRequestBtn = findViewById(R.id.sendRequestBtn);

        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.d("LOGIN RESULT:", "signInWithEmail:success");
                            Intent toDefaultActivity = new Intent(LoginActivity.this, DefaultActivity.class);
                            startActivity(toDefaultActivity);
                            finish();
                        } else {
                            Log.w("LOGIN RESULT:", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Logging in failed. Check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        toSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignupActivity = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(toSignupActivity);
                finish();
            }
        });
    }
}