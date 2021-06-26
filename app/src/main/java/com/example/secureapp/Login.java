package com.example.secureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button log_btn_signup, log_btn_login, btn_phone_login, btn_resetPw;
    TextInputLayout logEmail, logPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        log_btn_signup = findViewById(R.id.log_signup_btn);
        log_btn_login = findViewById(R.id.log_btn);
        btn_phone_login = findViewById(R.id.login_phone);
        btn_resetPw = findViewById(R.id.forgotPw_btn);

        btn_resetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ResetPassword.class));
            }
        });

        log_btn_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

        btn_phone_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,PhoneAuthentication.class);
                startActivity(intent);
            }
        });

        logEmail = findViewById(R.id.log_email);
        logPassword = findViewById(R.id.log_password);
        mAuth = FirebaseAuth.getInstance();
        log_btn_login.setOnClickListener(view -> {
            loginUser();
        });
    }

    private void loginUser(){
        String email = logEmail.getEditText().getText().toString();
        String password = logPassword.getEditText().getText().toString();

        if(TextUtils.isEmpty(email)){
            logEmail.setError("Email address cannot be empty");
            logEmail.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            logPassword.setError("Password cannot be empty");
            logPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        // check if user email address is verified
                        if (mAuth.getCurrentUser().isEmailVerified()) {
                            Toast.makeText(Login.this, "User logged in successfully!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this,Dashboard.class));
                            finish();
                        }else{
                            Toast.makeText(Login.this,"Please verify your email address.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Login.this, "Log in Error: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}