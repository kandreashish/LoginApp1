package com.example.ashishthelegend.loginapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private Button signUp;
    private EditText password,rePassword,email,age;
    private TextView loginNav;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RadioGroup sex;
    private RadioButton sex2;
    String email1,age2,sex3;
    int radioButtonId;
    private ImageView propic;
    private static final int IMAGE_PIC=100;
    private final FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();;
    private StorageReference srefer;
    Uri imagepath;

    LoginActivity loginActivity=new LoginActivity();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==IMAGE_PIC && resultCode==RESULT_OK && data.getData()!= null)
        {
            imagepath=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                propic.setImageBitmap(bitmap);
                propic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starting");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signupactivity);
        assign();
        loginNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        srefer=firebaseStorage.getReference();
        propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"),IMAGE_PIC);

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButtonId=sex.getCheckedRadioButtonId();
                sex2=findViewById(radioButtonId);
                age2=age.getText().toString().trim();
                sex3=sex2.getText().toString();
                loginActivity.closeKeyboard();
                progressDialog.setMessage("Ashish is legend");
                progressDialog.show();
        if(validate())
        {
            if(password.getText().toString().trim().length()<6)
            {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "Password should be more than 6 letters", Toast.LENGTH_SHORT).show();
            }
            else {
                email1 = email.getText().toString().trim();
                String pass1 = password.getText().toString().trim();
                firebaseAuth.createUserWithEmailAndPassword(email1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            StorageReference srefer1=srefer.child(firebaseAuth.getUid());

                            progressDialog.dismiss();
                            register();
                            firebaseAuth.signOut();
                            finish();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                            finish();
                        } else if (task.isCanceled()) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Signup cancelled", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else {
            progressDialog.dismiss();
            Snackbar.make(view,"problem occured",Snackbar.LENGTH_INDEFINITE);
        }
            }
        });
    }
    private void assign()
    {
        signUp=findViewById(R.id.btn_Signup);
        email =findViewById(R.id.et_sname);
        password=findViewById(R.id.et_spassword);
        rePassword =findViewById(R.id.et_repassword);
        loginNav =findViewById(R.id.tv_alreadylogin);
        age=findViewById(R.id.et_Age);
        progressDialog=new ProgressDialog(this);
        sex=findViewById(R.id.rg_sex);
        propic=findViewById(R.id.iv_profilepic);
    }
    private boolean validate()
    {
        if((!email.getText().toString().isEmpty()
                ||!password.getText().toString().isEmpty()
                || !rePassword.getText().toString().isEmpty()
        ||imagepath != null)
                &&(password.getText().toString().equals(rePassword.getText().toString())))
        {
            return true;
        }
        return false;
    }
    private void register()
    {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference ref= firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile=new UserProfile(email1,age2,sex3);
        StorageReference storageReference=srefer.child(firebaseAuth.getUid()).child("Images").child("profilepic");
        UploadTask uploadTask=storageReference.putFile(imagepath);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(getApplicationContext(), "Upload Succesfull", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
        ref.setValue(userProfile);
    }
}
