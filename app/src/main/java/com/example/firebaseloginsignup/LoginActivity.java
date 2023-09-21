package com.example.firebaseloginsignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseloginsignup.Activity.ForgotPassword;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {


    FirebaseAuth auth;
    EditText email_login,password_login;
    Button login_button;
    TextView signup_click,forgot_click;
   GoogleSignInClient gsc;
   GoogleSignInOptions gso;

   boolean passwordVisible;
   Button google_btn,facebook_btn;

   CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_login = (EditText) findViewById(R.id.email_login);
        password_login = (EditText) findViewById(R.id.password_login);
        login_button = (Button)findViewById(R.id.login_button);
        google_btn = (Button)findViewById(R.id.google_btn);
        forgot_click = (TextView) findViewById(R.id.forgot_click);
        facebook_btn = (Button)findViewById(R.id.facebook_btn);
        auth = FirebaseAuth.getInstance();

        

//        facebook_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                =============login with facebook==========
//                  Intent intent = new Intent(LoginActivity.this, FacebookAuthenticate.class);
//                  startActivity(intent);
//                  finish();
//            }
//        });

//        ================= jb koi forgot password pr click krne pr bhe sidhe forgot password activity me phuch jayega =================
        forgot_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

//gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(LoginActivity.this,gso);

//     ===============the sign in with google ==========

        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

//        ===============Login with login button ===================
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_login_text = email_login.getText().toString();
                String password_login_text = password_login.getText().toString();

                if(email_login_text.isEmpty() || password_login_text.isEmpty()){
                    Toast.makeText(LoginActivity.this, "please fill email and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(email_login_text,password_login_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, ThirdActivity.class);

                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        if(auth.getCurrentUser() != null){
           Intent intent = new Intent(LoginActivity.this, ThirdActivity.class);
           startActivity(intent);
           finish();
        }

        signup_click = (TextView) findViewById(R.id.signup_click);
        signup_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if(acc != null){
            navigateToOtherActivity();
        }

//   =========hum onTouch methods ko set krenge jisse hum password ko dikha sakte h aur nhi ==========
        password_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX() >= password_login.getRight()-password_login.getCompoundDrawables()[Right].getBounds().width()){
                        int selected = password_login.getSelectionEnd();
                        if(passwordVisible){
                          password_login.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.close_eyes,0);
                          password_login.setTransformationMethod(PasswordTransformationMethod.getInstance());
                          passwordVisible = false;
                        }
                        else{

                            password_login.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.open_eye,0);
                            password_login.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        password_login.setSelection(selected);
                        return true;
                    }
                }
                return false;
            }
        });
    }


    private int REQUEST_CODE = 1000;
    private void SignIn() {
       Intent intent = gsc.getSignInIntent();
       startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseWithAuth(account.getIdToken());
                navigateToOtherActivity();
            } catch (ApiException e) {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    public void firebaseWithAuth(String idToken){
//        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken,null);
//        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if(task.isSuccessful()){
//                    FirebaseUser firebaseUser = auth.getCurrentUser();
//                    Intent intent = new Intent(LoginActivity.this, ThirdActivity.class);
//                    Toast.makeText(LoginActivity.this, "Login Successful with google", Toast.LENGTH_SHORT).show();
//                    startActivity(intent);
//                    finish();
//                }
//                else{
//                    Toast.makeText(LoginActivity.this, "you are in firebaseWithAuth function", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
    private void navigateToOtherActivity() {
        Intent intent = new Intent(LoginActivity.this,ThirdActivity.class);
        startActivity(intent);
        finish();
    }

}