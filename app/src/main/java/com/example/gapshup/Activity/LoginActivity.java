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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView txt_signUp, txt_signIn;
    EditText edt_email;
    EditText edt_pwd;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        txt_signUp = findViewById(R.id.txt_signUp);
        txt_signIn = findViewById(R.id.txt_signIn);
        edt_email = findViewById(R.id.edt_email);
        edt_pwd = findViewById(R.id.edt_pwd);
        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        txt_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = edt_email.getText().toString();
                String password = edt_pwd.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    progressDialog.dismiss();
                    Toast.makeText( LoginActivity.this,"Email or Password is empty", Toast.LENGTH_SHORT).show();
                }
                else if(!email.matches(emailPattern))
                {
                    progressDialog.dismiss();
                    edt_email.setError("Invalid Email");
                    Toast.makeText(LoginActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }
                else if(!(password.length()>=8))
                {
                    progressDialog.dismiss();
                    edt_email.setError("Invalid Password");
                    Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"Email or Password is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}