package com.example.mymed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowBookingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private Button buttonPrenota;
    private HelperAdapterBooking helperAdapter;
    private DatabaseReference databaseReference;
    List<BookingUser> bookingsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bookings);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            // User is not logged in
            startActivity(new Intent(ShowBookingsActivity.this, MainActivity.class));
            finish();
        }

        bookingsList= new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.result_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(helperAdapter);

        buttonPrenota = (Button)findViewById(R.id.button_prenota);

        String user_id = firebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child("patients").child(user_id).child("bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot turno:snapshot.getChildren()) {
                    for (DataSnapshot prenotazione : turno.getChildren()) {
                        BookingUser bk = prenotazione.getValue(BookingUser.class);
                        bookingsList.add(bk);
                    }
                }
                helperAdapter = new HelperAdapterBooking(bookingsList);
                recyclerView.setAdapter(helperAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        myRef.child("users").child("patients").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().child("doctor").getValue() == null){
                        startActivity(new Intent(ShowBookingsActivity.this, AddDoctorActivity.class));
                        finish();
                    }
                }
            }
        });

        buttonPrenota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowBookingsActivity.this, UserActivity.class));
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.edit_doctor){
            startActivity(new Intent(ShowBookingsActivity.this, AddDoctorActivity.class));
            finish();
        }
        else if(id == R.id.edit_profile){
            startActivity(new Intent(ShowBookingsActivity.this, ChangePasswordActivity.class));
            finish();
        }
        else if(id == R.id.logout){
            firebaseAuth.signOut();
            startActivity(new Intent(ShowBookingsActivity.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}