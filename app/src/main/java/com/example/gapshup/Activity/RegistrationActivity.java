package com.example.gapshup.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gapshup.R;
import com.example.gapshup.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegistrationActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseStorage storage;
    TextView txt_signIn;
    TextView txt_signUp_btn;
    EditText reg_name, reg_email, reg_pwd, reg_cnf_pwd;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        txt_signIn = findViewById(R.id.txt_signIn);
        reg_name = findViewById(R.id.reg_name);
        reg_email = findViewById(R.id.reg_email);
        reg_pwd = findViewById(R.id.reg_pwd);
        reg_cnf_pwd = findViewById(R.id.reg_cnf_pwd);
        txt_signUp_btn = findViewById(R.id.txt_signUp_btn);

        txt_signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = reg_name.getText().toString();
                String email = reg_email.getText().toString();
                String password = reg_pwd.getText().toString();
                String cnf_password = reg_cnf_pwd.getText().toString();
                String status = "Hey there! I am using GupShup.";

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cnf_password)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "One or more text fields is empty", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    reg_email.setError("Invalid Email Address");
                    Toast.makeText(RegistrationActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(cnf_password)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password doesn't matched", Toast.LENGTH_SHORT).show();
                } else if (!(password.length() >= 8)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password Length Should Be Greater than 8", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                Users users = new Users(auth.getUid(), name, email, status);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(RegistrationActivity.this, "SignUp Successful.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(RegistrationActivity.this, "Error while creating a new user.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                }

            }
        });
        txt_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }
}