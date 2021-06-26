package com.example.secureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    TextInputLayout text_email_resetPw;
    Button btn_resetPw, btn_cancelResetPw;
    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        text_email_resetPw = findViewById(R.id.email_resetPw);
        btn_resetPw = findViewById(R.id.resetPw_btn);
        progressBar = findViewById(R.id.progressBar);
        btn_cancelResetPw = findViewById(R.id.btn_goBackLogin);

        mAuth = FirebaseAuth.getInstance();

        btn_cancelResetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        btn_resetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email = text_email_resetPw.getEditText().getText().toString();

        if(email.isEmpty()){
            text_email_resetPw.setError("Email address cannot be blank");
            text_email_resetPw.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            text_email_resetPw.setError("Please provide a valid email address");
            text_email_resetPw.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPassword.this, "A link has been sent to your email. Check your inbox to reset password.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetPassword.this,Login.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ResetPassword.this, "Some errors has occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}