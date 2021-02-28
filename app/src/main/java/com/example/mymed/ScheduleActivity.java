package com.example.mymed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity  {

    private FirebaseAuth firebaseAuth;
    private Spinner spinner;
    private Button button_dialog;
    private Button button_save;
    private List<String> office_selected;
    private EditText edit_indirizzo;
    private LinearLayout layoutList;
    private ArrayList<Schedule> schedules;

    private ArrayList<EditText> ore_inizio;
    private ArrayList<EditText> ore_fine;
    private ArrayList<Spinner> spinners;
    private ArrayList<String> indirizzi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        schedules = new ArrayList<>();

        ore_inizio = new ArrayList<>();
        ore_fine = new ArrayList<>();
        spinners = new ArrayList<>();
        indirizzi = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        layoutList = findViewById(R.id.mainLayout);

        if (firebaseAuth.getCurrentUser() == null) {
            // User is not logged in
            startActivity(new Intent(ScheduleActivity.this, MainActivity.class));
            finish();
        }
        //spinner = (Spinner)findViewById(R.id.planets_spinner);
        button_dialog = (Button)findViewById(R.id.addSchedule);
        button_save = (Button)findViewById(R.id.saveButton);
        edit_indirizzo = (EditText)findViewById(R.id.indirizzo_studio);

        String user_id = firebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();

        button_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View row_schedule = getLayoutInflater().inflate(R.layout.row_schedule,null,false);
                layoutList.addView(row_schedule);

                EditText ora_inizio = (EditText)row_schedule.findViewById(R.id.ora_inizio);
                EditText ora_fine = (EditText)row_schedule.findViewById(R.id.ora_fine);
                Spinner spinner_day = (Spinner)row_schedule.findViewById(R.id.giorno_settimana);


                ore_inizio.add(ora_inizio);
                ore_fine.add(ora_fine);
                spinners.add(spinner_day);
                indirizzi.add(edit_indirizzo.getText().toString());

            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference();
                for(int i = 0;i<indirizzi.size();i++) {
                    int start_hour, start_min, end_hour, end_min;
                    String[] parts_start = new String[2];
                    String[] parts_end = new String[2];
                    if(ore_inizio.get(i).getText().toString().contains(":")){
                        parts_start = ore_inizio.get(i).getText().toString().split(":");
                    }
                    else{
                        parts_start[0] = ore_inizio.get(i).getText().toString();
                        parts_start[1] = "0";
                    }
                    if(ore_fine.get(i).getText().toString().contains(":")){
                        parts_end = ore_fine.get(i).getText().toString().split(":");
                    }
                    else{
                        parts_end[0] = ore_fine.get(i).getText().toString();
                        parts_start[1] = "0";
                    }

                    myRef.child("users").child("doctors").child(user_id).child("offices").child(edit_indirizzo.getText().toString()).child("timetables").push().setValue(new Timetable(Integer.valueOf(parts_start[0].toString()),Integer.valueOf(parts_start[1].toString()),Integer.valueOf(parts_end[0].toString()),Integer.valueOf(parts_end[1].toString()),spinners.get(i).getSelectedItem().toString()));
                }

                showToast("Dati inseriti correttamente.");
            }
        });

    }

    public void showToast(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }

}

