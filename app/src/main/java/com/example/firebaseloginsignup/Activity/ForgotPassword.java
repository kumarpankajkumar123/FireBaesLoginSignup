package com.example.firebaseloginsignup.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseloginsignup.LoginActivity;
import com.example.firebaseloginsignup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText email_forgot;
    TextView back_to_login;
    Button forgot_button;
    String frtEmail;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        back_to_login = (TextView) findViewById(R.id.back_to_login);
        forgot_button = (Button) findViewById(R.id.forgot_button);
        email_forgot = (EditText) findViewById(R.id.email_forgot);
        Auth = FirebaseAuth.getInstance();

//        ============= back to login textview pr click krne se hum sidhe login vali activity pr phuch jayege ====================
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        agar koi forgot button pr click krta h to bhe sidhe reset password krega email ke duara ====================

        forgot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateData();
            }
        });
    }

    private void ValidateData() {
         frtEmail = email_forgot.getText().toString();
        if(frtEmail.isEmpty()){
            email_forgot.setError("Required");
        }
        else{
            ForgotPassword();
        }
    }

    private void ForgotPassword() {
        Auth.sendPasswordResetEmail(frtEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "check your email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(ForgotPassword.this, "Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}