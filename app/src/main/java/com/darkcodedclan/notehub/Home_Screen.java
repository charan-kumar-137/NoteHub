package com.darkcodedclan.notehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home_Screen extends AppCompatActivity {
    private Button upload;
    private Button download;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home__screen);

        mAuth = FirebaseAuth.getInstance();

        upload = findViewById(R.id.upload);
        download = findViewById(R.id.download);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this,Upload_Download.class);
                intent.putExtra("key","upload");
                startActivity(intent);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Screen.this,Upload_Download.class);
                intent.putExtra("key","download");
                startActivity(intent);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(Home_Screen.this,Login_Screen.class));
        }
    }
}