package com.darkcodedclan.notehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup_Screen extends AppCompatActivity implements View.OnClickListener {
    private Button signup_submit;
    private EditText signup_email;
    private EditText signup_password;
    private EditText signup_name;
//    private String name_signup;    // currently not used
    private String email_signup;
    private String password_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup__screen);

        signup_submit = findViewById(R.id.signup_submit);
        signup_submit.setOnClickListener(this);

        signup_name = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
    }

    @Override
    public void onClick(View v) {
//        name_signup = signup_name.getText().toString().trim();
        email_signup = signup_email.getText().toString().trim();
        password_signup = signup_password.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (TextUtils.isEmpty(email_signup) && TextUtils.isEmpty(password_signup)){
            Toast.makeText(this, "Something is missing!!", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email_signup, password_signup)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Signup_Screen.this, "Sign up Success", Toast.LENGTH_SHORT).show();
                                signup_success();
                            } else {
                                Toast.makeText(Signup_Screen.this, "Sign up Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void signup_success(){
        Intent intent = new Intent(Signup_Screen.this,Home_Screen.class);
        startActivity(intent);
    }
}