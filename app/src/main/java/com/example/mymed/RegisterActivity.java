package com.example.mymed;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth firebaseAuth;
    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText nameEdit;
    private EditText surnameEdit;
    private EditText birth_dateEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        firebaseAuth = FirebaseAuth.getInstance();

        Button signInButton = (Button)findViewById(R.id.sign_in_button);
        emailEdit = (EditText)findViewById(R.id.editEmail);
        passwordEdit = (EditText)findViewById(R.id.editPassword);
        nameEdit = (EditText)findViewById(R.id.editName);
        surnameEdit = (EditText)findViewById(R.id.editSurname);
        birth_dateEdit = (EditText)findViewById(R.id.editDate);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.registerUser();
            }
        });
    }

    private void registerUser() {
        String userEmail = emailEdit.getText().toString().trim();
        String userPassword = passwordEdit.getText().toString().trim();
        String userName = nameEdit.getText().toString().trim();
        String userSurname = surnameEdit.getText().toString().trim();
        String userBirthDate = birth_dateEdit.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)) {
            showToast("Inserisci indirizzo email!");
            return;
        }

        if(TextUtils.isEmpty(userPassword)){
            showToast("Inserisci password!");
            return;
        }

        if(userPassword.length()<6){
            showToast("La password deve contenere almeno 6 caratteri!");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "Nuova registrazione: " + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            String gotError = task.getException().toString();
                            if(gotError.toLowerCase().contains("already in use"))
                                RegisterActivity.this.showToast("Registrazione fallita. Email già utilizzata");
                            else RegisterActivity.this.showToast("Registrazione fallita: " + task.getException());
                        } else {
                            String user_id = firebaseAuth.getCurrentUser().getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/");
                            DatabaseReference myRef = database.getReference();
                            myRef.child("users").child("patients").child(user_id).child("name").setValue(userName);
                            myRef.child("users").child("patients").child(user_id).child("surname").setValue(userSurname);
                            myRef.child("users").child("patients").child(user_id).child("birth_date").setValue(userBirthDate);

                            showToast("Registrazione avvenuta con successo!");
                            RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            RegisterActivity.this.finish();
                        }
                    }
                });
    }

    public void showToast(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }

}