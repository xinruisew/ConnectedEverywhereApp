package com.example.secureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    Button btn_logout;
    ImageButton btn_profilePage;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btn_logout = findViewById(R.id.ds_logOut);
        btn_profilePage = findViewById(R.id.btn_profilePage);
        mAuth = FirebaseAuth.getInstance();

        btn_logout.setOnClickListener(view->{
            mAuth.signOut();
            startActivity(new Intent(Dashboard.this, Login.class));
            Toast.makeText(Dashboard.this, "User logged out successfully!",Toast.LENGTH_SHORT).show();
            finish();
        });

        btn_profilePage.setOnClickListener(view->{
            startActivity(new Intent(Dashboard.this, FingerprintAuth.class));
            finish();
        });
    }
}