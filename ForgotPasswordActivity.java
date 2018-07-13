package com.example.ashishthelegend.loginapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button reset;
    private EditText username;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        reset=findViewById(R.id.btn_reset);
        username=findViewById(R.id.et_remail);
        firebaseAuth=FirebaseAuth.getInstance();



        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(ForgotPasswordActivity.this, "fields can't be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    String email = username.getText().toString().trim();
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "password reset email sent succefully..", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            }
                            else
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "Email Not registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });




    }
}
