package com.example.mymed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class DoctorActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private Button logoutButton;
    private Button editInsertButton;
    private Button viewBookButton;
    private Button viewRequestButton;
    private Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        firebaseAuth = FirebaseAuth.getInstance();

        logoutButton = (Button)findViewById(R.id.logout_button_med);
        editInsertButton = (Button)findViewById(R.id.buttonInsert);
        viewBookButton = (Button)findViewById(R.id.buttonViewBooking);
        viewRequestButton = (Button)findViewById(R.id.buttonViewRequest);
        editProfileButton = (Button)findViewById(R.id.buttonEditProfile);

        if (firebaseAuth.getCurrentUser() == null) {
            // User is logged in
            startActivity(new Intent(DoctorActivity.this, MainActivity.class));
            finish();
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(DoctorActivity.this, MainActivity.class));
                finish();
            }
        });
        editInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorActivity.this, ScheduleActivity.class));
            }
        });
        viewBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorActivity.this, MainActivity.class));
            }
        });
        viewRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorActivity.this, MainActivity.class));
            }
        });
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorActivity.this, MainActivity.class));
            }
        });

    }
}