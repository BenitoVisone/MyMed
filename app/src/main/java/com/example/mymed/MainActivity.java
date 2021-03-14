package com.example.mymed;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class
MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth firebaseAuth;
    private Button loginButton;
    private Button registerButton;
    private EditText emailEdit;
    private EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            String user_id = firebaseAuth.getCurrentUser().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference();
            myRef.child("users").child("doctors").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        if(task.getResult().getValue()!=null) MainActivity.this.startActivity(new Intent(MainActivity.this, DoctorActivity.class));
                        else MainActivity.this.startActivity(new Intent(MainActivity.this, UserActivity.class));
                        MainActivity.this.finish();
                    }
                }
            });
        }
        loginButton = (Button)findViewById(R.id.login_button);
        registerButton = (Button)findViewById(R.id.button_register);
        emailEdit = (EditText)findViewById(R.id.editEmail);
        passwordEdit = (EditText)findViewById(R.id.editPassword);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String useremail = emailEdit.getText().toString();
                String userpassword = passwordEdit.getText().toString();

                if (TextUtils.isEmpty(useremail)) {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Inserisci indirizzo email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userpassword)) {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Inserisci la password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(useremail, userpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {

                                    if (userpassword.length() < 6) {
                                        passwordEdit.setError("Inserisci una password di almeno 6 caratteri");
                                    } else {
                                        Toast.makeText(MainActivity.this, "Autenticazione fallita", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    String user_id = firebaseAuth.getCurrentUser().getUid();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/");
                                    DatabaseReference myRef = database.getReference();
                                    myRef.child("users").child("doctors").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if(task.getResult().getValue()!=null) MainActivity.this.startActivity(new Intent(MainActivity.this, DoctorActivity.class));
                                                else MainActivity.this.startActivity(new Intent(MainActivity.this, UserActivity.class));
                                                MainActivity.this.finish();
                                            }
                                        }
                                    });

                                }
                            }
                        });

            }
        });
    }

}