package com.example.mymed;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button logoutButton;
    private TextView textName;
    private TextView textSurname;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        firebaseAuth = FirebaseAuth.getInstance();

        logoutButton = (Button)findViewById(R.id.logout_button_med);
        textName = (TextView)findViewById(R.id.textName);
        textSurname = (TextView)findViewById(R.id.textSurname);

        String name, surname;

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity(new Intent(UserActivity.this, MainActivity.class));
                finish();

            }
        });

        String user_id = firebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference myRef = database.getReference();

        myRef.child("users").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    textName.setText(String.valueOf(task.getResult().child("name").getValue()));
                    textSurname.setText(String.valueOf(task.getResult().child("surname").getValue()));
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });


    }

}