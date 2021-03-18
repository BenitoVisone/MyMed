package com.example.mymed;



import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddDoctorActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText editSurname;
    private RecyclerView recyclerView;
    private HelperAdapter helperAdapter;
    private ImageButton searchButton;
    private DatabaseReference databaseReference;

    List<Doctors> doctorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            // User is not logged in
            startActivity(new Intent(AddDoctorActivity.this, MainActivity.class));
            finish();
        }

        editSurname = (EditText)findViewById(R.id.search_field);
        searchButton = (ImageButton)findViewById(R.id.search_btn);
        recyclerView= (RecyclerView) findViewById(R.id.result_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorsList = new ArrayList<>();


        databaseReference = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child("doctors");



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = editSurname.getText().toString();
                doctorsList = new ArrayList<>();
                helperAdapter = new HelperAdapter(doctorsList);
                recyclerView.setAdapter(helperAdapter);
                searchText = searchText.substring(0, 1).toUpperCase() + searchText.substring(1).toLowerCase();
                Query queryRef = databaseReference.orderByChild("surname").startAt(searchText).endAt(searchText + "\uf8ff");
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()){
                            Doctors dr = ds.getValue(Doctors.class);
                            dr.setKey(ds.getKey());
                            doctorsList.add(dr);
                        }
                        helperAdapter = new HelperAdapter(doctorsList);
                        recyclerView.setAdapter(helperAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


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
            startActivity(new Intent(AddDoctorActivity.this, AddDoctorActivity.class));
            finish();
        }
        else if(id == R.id.edit_profile){
            startActivity(new Intent(AddDoctorActivity.this, ChangePasswordActivity.class));
            finish();
        }
        else if(id == R.id.logout){
            firebaseAuth.signOut();
            startActivity(new Intent(AddDoctorActivity.this, MainActivity.class));
            finish();
        }
        else if(id == R.id.show_bookings){
            startActivity(new Intent(AddDoctorActivity.this, ShowBookingsActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}