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
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity  {

    private FirebaseAuth firebaseAuth;
    private Spinner spinner;
    private Button button_dialog;
    private Button button_save;
    private List<String> office_selected;
    private EditText edit_indirizzo;
    private EditText edit_slot;
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
        button_dialog = (Button)findViewById(R.id.addSchedule);
        button_save = (Button)findViewById(R.id.saveButton);
        edit_indirizzo = (EditText)findViewById(R.id.indirizzo_studio);
        edit_slot = (EditText)findViewById(R.id.edit_slot);

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

                    getSlottedTimetable(ore_inizio.get(i).getText().toString(),ore_fine.get(i).getText().toString(),Integer.valueOf(edit_slot.getText().toString()),myRef,user_id,i);

                }

                showToast("Dati inseriti correttamente.");
            }
        });

    }

    public void showToast(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }

    private void getSlottedTimetable(String ore_inizio, String ore_fine,int slot_time,DatabaseReference myRef, String user_id,int i){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime lt_start = LocalTime.parse(ore_inizio);
        LocalTime lt_stop = LocalTime.parse(ore_fine);

        while(LocalTime.parse(df.format(lt_start.plusMinutes(slot_time))).isBefore(lt_stop)) {

            LocalTime lt_slot = LocalTime.parse(df.format(lt_start.plusMinutes(slot_time)));
            myRef.child("users").child("doctors").child(user_id).child("offices").child(edit_indirizzo.getText().toString()).child("timetables").child(spinners.get(i).getSelectedItem().toString()).push().setValue(new Timetable(Integer.parseInt(lt_start.toString().split(":")[0]),Integer.parseInt(lt_start.toString().split(":")[1]),Integer.parseInt(lt_slot.toString().split(":")[0]),Integer.parseInt(lt_slot.toString().split(":")[1]),spinners.get(i).getSelectedItem().toString()));

            lt_start = lt_slot;
        }

    }

}

