package com.darkcodedclan.notehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        Button login_button = findViewById(R.id.login_button);
        Button signup_button = findViewById(R.id.signup_button);

        login_button.setOnClickListener(this);
        signup_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.login_button) {
            intent = new Intent(WelcomeScreen.this,Login_Screen.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.signup_button) {
            intent = new Intent(WelcomeScreen.this,Signup_Screen.class);
            startActivity(intent);
        }
    }
}