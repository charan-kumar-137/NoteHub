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

public class Login_Screen extends AppCompatActivity implements View.OnClickListener {
    private EditText login_email;
    private EditText login_password;
    private Button login_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login__screen);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);

        login_submit = findViewById(R.id.login_submit);
        login_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String email_login = login_email.getText().toString().trim();
        String password_login = login_password.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(TextUtils.isEmpty(email_login) && TextUtils.isEmpty(password_login)){
            Toast.makeText(this, "Something is missing!!", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email_login,password_login)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               login_success();
                            } else {
                                Toast.makeText(Login_Screen.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void login_success(){
        Intent intent = new Intent(Login_Screen.this,Home_Screen.class);
        startActivity(intent);
    }
}