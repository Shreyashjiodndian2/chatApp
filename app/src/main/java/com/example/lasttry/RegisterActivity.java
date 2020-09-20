package com.example.lasttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText etUserName,etPassword,etEmail;
    Button btnRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = findViewById(R.id.etLoginUserName);
        etPassword = findViewById(R.id.etLoginPassword);
        etEmail =  findViewById(R.id.etEmail);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();

                registerNow(userName,email,password);
            }
        });
    }

    private void registerNow(final String userName,String email,String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

                    //hashMaps for data storage
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",userName);
                    hashMap.put("imageURL","default");

                    //opening the main activity after success registration
                    myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                String err = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, err, Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegisterActivity.this, "hashMap is not added to stroage", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else{
                    String err = task.getException().toString();
                    Toast.makeText(RegisterActivity.this, err, Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, "Registation is failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }









}

