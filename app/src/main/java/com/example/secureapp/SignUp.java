package com.example.secureapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    //Variables
    TextInputLayout regEmail, regPassword, regConfirmPW;
    Button regBtn, regToLoginBtn;

    AwesomeValidation awesomeValidation;
    FirebaseAuth mAuth;

    // recaptcha
    CheckBox checkBox;
    GoogleApiClient googleApiClient;
    String SiteKey = "6Lc3okcbAAAAAGBlI5TJuwvHAul1Bcl0I81AkfEw";
    //recaptcha end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Hooks to all xml elements in activity_sign_up.xml
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regConfirmPW = findViewById(R.id.reg_confirmPW);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_login_btn);

        regToLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        // validate password
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,
                R.id.reg_password,
            "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*_+=|(){}:;'<>,.?/]).{6,}",
                R.string.notValidPW);
        awesomeValidation.addValidation(this,R.id.reg_confirmPW,R.id.reg_password,R.string.passwordisnotcorrect);

        // ----- Recaptcha -----
        // Assign Variable
        checkBox = findViewById(R.id.checkRobot);
        // Create Google Api Client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(SignUp.this)
                .build();
        googleApiClient.connect();
        checkBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Set if condition when checkbox checked
                if (checkBox.isChecked()) {
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,SiteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    if ((status != null) && status.isSuccess()){
                                        //Display Successful Message
                                        Toast.makeText(getApplicationContext()
                                                ,"Successfully Verified..."
                                                , Toast.LENGTH_SHORT).show();
                                        //Change Checkbox text color
                                        checkBox.setTextColor(Color.GREEN);
                                    }
                                }
                            });
                }else{
                    // Default Checkbox text color
                    checkBox.setTextColor(Color.BLACK);
                    Toast.makeText(SignUp.this,"Verification failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        regBtn.setOnClickListener(view ->{
            if (awesomeValidation.validate()){
                if(checkBox.getCurrentTextColor() == Color.GREEN){
                    createUser();
                }else{
                    Toast.makeText(this,"Please verify that you are not a robot",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Failed to create account",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUser(){
        String email = regEmail.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        if(TextUtils.isEmpty(email)){
            regEmail.setError("Email address cannot be empty");
            regEmail.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            regPassword.setError("Password cannot be empty");
            regPassword.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        // sending email verification
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SignUp.this, "User registered successfully. Please verify your email address.",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignUp.this,Login.class));
                                    finish();
                                }else{
                                    Toast.makeText(SignUp.this, "Registration Error: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(SignUp.this, "Registration Error: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}