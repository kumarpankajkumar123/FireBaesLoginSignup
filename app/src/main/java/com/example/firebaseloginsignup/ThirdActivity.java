package com.example.firebaseloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ThirdActivity extends AppCompatActivity {

    FirebaseAuth auth;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    TextView third_name,third_email;
    Button Signout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        third_email = (TextView) findViewById(R.id.third_email);
        third_name = (TextView) findViewById(R.id.third_name);
        Signout_btn = (Button) findViewById(R.id.Signout_btn);

        auth = FirebaseAuth.getInstance();

//


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(ThirdActivity.this,gso);

        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if(acc != null){
            String personal_name = acc.getDisplayName();
            String personal_email = acc.getEmail();
            third_name.setText(personal_name);
            third_email.setText(personal_email);
        }

        Signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signout();
                Signout_login();
            }
        });
    }
    void Signout(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                Intent intent = new Intent(ThirdActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    void Signout_login(){
        auth.signOut();
        Intent intent = new Intent(ThirdActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}