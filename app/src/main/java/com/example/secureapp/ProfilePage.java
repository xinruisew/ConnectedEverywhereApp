package com.example.secureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {

    TextView tv_name, tv_uniID, tv_uniEmail, tv_faculty;
    Button btn_editData, btn_backDash;
    DatabaseReference rootDatabaseref;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        tv_name = findViewById(R.id.tv_name);
        tv_uniID = findViewById(R.id.tv_uniID);
        tv_uniEmail = findViewById(R.id.tv_uniEmail);
        tv_faculty = findViewById(R.id.tv_faculty);
        btn_editData = findViewById(R.id.btn_editData);
        btn_backDash = findViewById(R.id.btn_backDash);

        String userID = mAuth.getInstance().getCurrentUser().getUid();
        rootDatabaseref = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        rootDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    String uniID = snapshot.child("uniID").getValue().toString();
                    String uniEmail = snapshot.child("uniEmail").getValue().toString();
                    String faculty = snapshot.child("faculty").getValue().toString();
                    tv_name.setText(name);
                    tv_uniID.setText(uniID);
                    tv_uniEmail.setText(uniEmail);
                    tv_faculty.setText(faculty);
                }else{
                    Toast.makeText(ProfilePage.this,"No records found in the system",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btn_editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, UpdateProfile.class);
                startActivity(intent);
                finish();
            }
        });

        btn_backDash.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}