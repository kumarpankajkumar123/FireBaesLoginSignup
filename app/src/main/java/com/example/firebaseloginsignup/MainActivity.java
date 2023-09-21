package com.example.firebaseloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseloginsignup.Modals.DetailLoginSignup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText email_Signup,name,confirmPassword_signup;
    EditText password_signup;
    TextView login_click;
    Button signup_button;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        signup_button = (Button) findViewById(R.id.signup_button);
        email_Signup = (EditText) findViewById(R.id.email_Signup);
        password_signup = (EditText) findViewById(R.id.password_signup);
        name = (EditText) findViewById(R.id.name);
        confirmPassword_signup = (EditText) findViewById(R.id.confirmPassword_signup);

//        progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setTitle("hello");
//        progressDialog.setMessage("we're creating your account");

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email1 = email_Signup.getText().toString();
                String password1 = password_signup.getText().toString();
                String name1 = name.getText().toString();
                String confirm = confirmPassword_signup.getText().toString();
                if(email1.isEmpty() || password1.isEmpty() || name1.isEmpty() || confirm.isEmpty()){
                    Toast.makeText(MainActivity.this, "please fill all details ", Toast.LENGTH_SHORT).show();
                } else if (!password1.equals(confirm)) {
                    Toast.makeText(MainActivity.this, "password and confirm password does not match", Toast.LENGTH_SHORT).show();
                } else{
//                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
//                       progressDialog.dismiss();
                            if(task.isSuccessful()){
                                DetailLoginSignup detailLoginSignup = new DetailLoginSignup(name1,email1,password1);
                                String id = task.getResult().getUser().getUid();
                                firebaseDatabase.getReference().child("Users").child(id).setValue(detailLoginSignup);
                                Toast.makeText(MainActivity.this, "signup successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        login_click = (TextView) findViewById(R.id.login_click);
        login_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        password_signup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX() >= password_signup.getRight()-password_signup.getCompoundDrawables()[Right].getBounds().width()){
                        int selected = password_signup.getSelectionEnd();
                        if(passwordVisible){
                            password_signup.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.close_eyes,0);
                            password_signup.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }
                        else{
                            password_signup.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.open_eye,0);
                            password_signup.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        password_signup.setSelection(selected);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}