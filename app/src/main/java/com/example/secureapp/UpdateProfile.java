package com.example.secureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class UpdateProfile extends AppCompatActivity {
    //Variables
    TextInputLayout profileName, profileUniID, profileUniEmail, profileFaculty;
    Button profile_submit, btn_backProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        //Hooks to all xml elements in activity_sign_up.xml
        profileName = findViewById(R.id.profile_name);
        profileUniID = findViewById(R.id.profile_uniID);
        profileUniEmail = findViewById(R.id.profile_uniEmail);
        profileFaculty = findViewById(R.id.profile_faculty);
        profile_submit = findViewById(R.id.profile_submit);
        btn_backProfile = findViewById(R.id.btn_backProfile);

        // linking to realtime database
        DAOUsers dao = new DAOUsers();

        btn_backProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateProfile.this, ProfilePage.class);
                startActivity(intent);
                finish();
            }
        });

        profile_submit.setOnClickListener(view ->{
            String name,uniEmail,uniID,faculty;
            name = profileName.getEditText().getText().toString();
            uniID = profileUniID.getEditText().getText().toString();
            uniEmail = profileUniEmail.getEditText().getText().toString();
            faculty = profileFaculty.getEditText().getText().toString();

            Users user = new Users(name,uniID,uniEmail,faculty);
            dao.add_update(user).addOnSuccessListener(suc->{
                Toast.makeText(this,"Profile has been updated!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateProfile.this, ProfilePage.class));
                finish();
            }).addOnFailureListener(er->{
                Toast.makeText(this,""+er.getMessage(),Toast.LENGTH_SHORT).show();
            });
        });
    }
}