package com.example.addressbook_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
EditText inputName,inputEmail,inputPassword,inputConfirmPassword;
Button signupBtn;
TextView signin;
String email,password,name,confirmPass;
private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initialize();
        setListeners();
    }
    public void initialize(){
        inputName=findViewById(R.id.inputName);
        inputEmail=findViewById(R.id.inputEmail1);
        inputPassword=findViewById(R.id.inputPassword1);
        inputConfirmPassword=findViewById(R.id.inputConfirmPassword);
        signupBtn=findViewById(R.id.btnSignup);
        signin=findViewById(R.id.textSignin);
        firebaseAuth=FirebaseAuth.getInstance();
    }
    public void setListeners(){
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=inputEmail.getText().toString();
                password=inputPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()){
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Account Created Successfully",Toast.LENGTH_SHORT).show();
                                        sendEmailVerification1();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SigninActivity.class));
            }
        });
    }

    public void sendEmailVerification1() {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Email Verification is sent",Toast.LENGTH_SHORT).show();                                }
                        startActivity(new Intent(getApplicationContext(),SigninActivity.class));
                        finish();
                    }
                });
    }
}
