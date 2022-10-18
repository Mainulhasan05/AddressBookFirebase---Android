package com.example.addressbook_firebase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DataEntryForm extends AppCompatActivity {
    private EditText nameET,jobET,phoneET,presentAddressET,permanentAddressET;
    private Button saveBtn;
    private String name,phone,job,presentAddress,permanentAddress;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry_form);
        db= FirebaseFirestore.getInstance();


        initialize();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(formValidation()){
                    Map<String,Object> user=new HashMap<>();
                    user.put("name",name);
                    user.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    user.put("job",job);
                    user.put("phone",phone);
                    user.put("presentAddress",presentAddress);
                    user.put("permanentAddress",permanentAddress);
                    db.collection("users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DataEntryForm.this,"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DataEntryForm.this,"Data Failed to Save",Toast.LENGTH_SHORT).show();
                                }
                            });


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearForm();
                        }
                    },1000);
                }
            }
        });
    }

    public void initialize(){
        nameET=findViewById(R.id.inputName);
        jobET=findViewById(R.id.inputJob);
        phoneET=findViewById(R.id.inputPhone);
        presentAddressET=findViewById(R.id.inputPresentAddress);
        permanentAddressET=findViewById(R.id.inputParmanentAddress);
        saveBtn=findViewById(R.id.saveBtn);
    }

    public boolean formValidation(){
        name=nameET.getText().toString();
        job=jobET.getText().toString();
        phone=phoneET.getText().toString();
        presentAddress=presentAddressET.getText().toString();
        permanentAddress=permanentAddressET.getText().toString();

        if(name.length()!=0 && job.length()!=0 && phone.length()!=0 &&presentAddress.length()!=0 && permanentAddress.length()!=0){
            return true;
        }
        else{
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void clearForm(){
        nameET.setText("");
        jobET.setText("");
        phoneET.setText("");
        presentAddressET.setText("");
        permanentAddressET.setText("");

        name=phone=job=presentAddress=permanentAddress="";


    }
}