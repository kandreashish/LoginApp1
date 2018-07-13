package com.example.ashishthelegend.loginapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.sax.StartElementListener;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private Button login;
    private EditText name;
    private EditText password;
    private TextView counter;
    private FirebaseAuth firebaseAuth;
    private TextView signup;
    private ProgressDialog progressdialog;
    private TextView forgotpassword;

    int count = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starting");

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        assign();

        counter.setText("No of attemts remaining " + String.valueOf(count));
        progressdialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), SecondscreenActivity.class));

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               closeKeyboard();
                if (validate()) {
                    progressdialog.setMessage("Ashish is legend");
                    progressdialog.show();
                    String name1 = name.getText().toString().trim();
                    String pass1 = password.getText().toString().trim();
                    firebaseAuth.signInWithEmailAndPassword(name1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressdialog.dismiss();
                                count = 5;
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();

                            } else if (task.isCanceled()) {
                                progressdialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();
                            } else {
                                progressdialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                count--;
                                counter.setText("No of attemts remaining " + String.valueOf(count));
                                if (count == 0) {
                                    login.setEnabled(false);
                                }
                            }

                        }
                    });
                } else {
                    Snackbar snackbar = Snackbar.make(view, "Fields can't be full", Snackbar.LENGTH_SHORT);
                    View sb = snackbar.getView();
                    TextView tv = sb.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.YELLOW);
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();
                    //Snackbar.make(view,"Fields can't be empty",Snackbar.LENGTH_SHORT).show();
                }
            }

        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "going to signup", Snackbar.LENGTH_INDEFINITE).show();
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class
                ));
            }
        });
    }

    private boolean validate() {
        if (name.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void assign() {
        login = findViewById(R.id.btn_login);
        name = findViewById(R.id.et_Username);
        password = findViewById(R.id.et_Password);
        counter = findViewById(R.id.tv_count);
        signup = findViewById(R.id.tv_Signupnav);
        firebaseAuth = FirebaseAuth.getInstance();
        forgotpassword=findViewById(R.id.tv_forgotpass);

    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

