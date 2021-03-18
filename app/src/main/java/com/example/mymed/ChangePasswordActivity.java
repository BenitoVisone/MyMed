package com.example.mymed;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private Button button_save_change;
    private EditText old_password;
    private EditText new_password_1;
    private EditText new_password_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        firebaseAuth = FirebaseAuth.getInstance();
        button_save_change = (Button)findViewById(R.id.button_save_change);
        old_password = (EditText)findViewById(R.id.old_password);
        new_password_1 = (EditText)findViewById(R.id.new_password_1);
        new_password_2 = (EditText)findViewById(R.id.new_password_2);
        button_save_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(!new_password_1.getText().toString().equals(new_password_2.getText().toString())) {
                    showToast("Password non corrispondenti");
                }
                else {

                    final String email = user.getEmail();
                    AuthCredential credential = EmailAuthProvider.getCredential(email, old_password.getText().toString());
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(new_password_1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            showToast("Something went wrong. Please try again later");
                                        } else {
                                            showToast("Password Successfully Modified");
                                        }
                                    }
                                });
                            } else {
                                showToast("Authentication Failed");
                            }
                        }
                    });
                }
            }
        });
    }

    public void showToast(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }





}
