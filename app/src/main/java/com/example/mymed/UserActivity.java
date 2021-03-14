package com.example.mymed;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button logoutButton;
    private EditText selectedData;
    private DatePickerDialog picker;
    private List<String> offices_selected = new ArrayList<String>();
    private List<String> all_timetables = new ArrayList<>();
    private List<String> all_ids = new ArrayList<>();
    private Spinner spinner;
    private Spinner spinnerTwo;
    private String id_doctor;
    private boolean checkBooking = false;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        firebaseAuth = FirebaseAuth.getInstance();

        logoutButton = (Button)findViewById(R.id.logout_button_med);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity(new Intent(UserActivity.this, MainActivity.class));
                finish();

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            // User is not logged in
            startActivity(new Intent(UserActivity.this, MainActivity.class));
            finish();
        }

        String user_id = firebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();

        myRef.child("users").child("patients").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().child("doctor").getValue() == null){
                        startActivity(new Intent(UserActivity.this, AddDoctorActivity.class));
                        finish();
                    }
                }
            }
        });

        selectedData=(EditText) findViewById(R.id.editTextDataPrenotazione);
        spinner = (Spinner)findViewById(R.id.planet_spinner);
        spinnerTwo = (Spinner)findViewById(R.id.planet_spinner_two);
        selectedData.setInputType(InputType.TYPE_NULL);
        selectedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(UserActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                selectedData.setText(simpledateformat.format(newDate.getTime()));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        DatabaseReference myRef1 = database.getReference();
        myRef1.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                id_doctor = dataSnapshot.child("patients").child(user_id).child("doctor").getValue(String.class);
                final List<String> offices = new ArrayList<String>();
                for(DataSnapshot snapshot : dataSnapshot.child("doctors").child(id_doctor).child("offices").getChildren()){
                    String address = snapshot.getKey();
                    offices.add(address);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserActivity.this, android.R.layout.simple_spinner_item, offices);
                offices_selected = offices;
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Selezionato un elemento dallo spinner, effettuo un interrogazione per recuperare tutti i timetables disponibili, da qui devo poi escludere
                //quelli già prenotati, interrogando il ramo "bookings" attraverso id_doctor e data, costruito in modo da ottimizzare ricerca e consentire notifiche al dottore
                if(!selectedData.getText().toString().isEmpty()){
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd-MM-yyyy").parse(selectedData.getText().toString());
                } catch (ParseException e) {
                    try {
                        date1 = new SimpleDateFormat("d-MM-yyyy").parse(selectedData.getText().toString());
                    } catch (ParseException parseException) {
                        try {
                            date1 = new SimpleDateFormat("dd-M-yyyy").parse(selectedData.getText().toString());
                        } catch (ParseException exception) {
                            try {
                                date1 = new SimpleDateFormat("d-M-yyyy").parse(selectedData.getText().toString());
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

                String giornoSettimana = getDayStringOld(date1, Locale.ITALIAN);
                Log.e("giornoSettimana", "Giorno selezionato: " + giornoSettimana);

                DatabaseReference myRef2 = database.getReference();
                myRef2.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final List<String> timetables = new ArrayList<String>();
                        List<String> tmt = new ArrayList<>();
                        final List<String> sIds = new ArrayList<>();//add ids in this list
                        final List<String> states = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.child("doctors").child(id_doctor).child("offices").child(spinner.getSelectedItem().toString()).child("timetables").child(giornoSettimana).getChildren()) {
                            String start_hour = snapshot.child("start_hour").getValue().toString();
                            String end_hour = snapshot.child("endHour").getValue().toString();
                            String start_min = snapshot.child("start_min").getValue().toString();
                            String end_min = snapshot.child("end_min").getValue().toString();
                            String tbls = "Dalle ore "+ start_hour + ":" + start_min + " alle ore: "+end_hour + ":"+end_min;

                            timetables.add(tbls);
                            sIds.add(snapshot.getKey());

                            for(DataSnapshot snapshot2 : snapshot.child("bookings").getChildren()){
                                if(snapshot2.child("data_prenotazione").getValue().toString().equals(selectedData.getText().toString())){
                                    timetables.remove(tbls);
                                    sIds.remove(snapshot.getKey());
                                }

                            }


                           // if(!bookedSlot(id_doctor,snapshot.getKey(),selectedData.getText().toString(),database)){

                             /*   Log.d("ritorno_valore","tornato false");
                            }
                            checkBooking = false;*/
                        }
                        all_timetables = timetables;
                        all_ids = sIds;

                        ArrayAdapter<String> adapterTwo = new ArrayAdapter<String>(UserActivity.this, android.R.layout.simple_spinner_item, timetables);
                        adapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTwo.setAdapter(adapterTwo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public boolean bookedSlot(String id_doctor,String id_turno_richiesto,String data_richiesta,FirebaseDatabase database){
        DatabaseReference myRef = database.getReference();
        myRef.child("bookings").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for(DataSnapshot snapshot : task.getResult().child(id_doctor).child(data_richiesta).getChildren()){
                        String status = snapshot.child("stato").getValue().toString();
                        String id_turno = snapshot.child("id_turno").getValue().toString();
                        Log.d("stato","Stato trovato: "+status);
                        Log.d("turno","Id_turno: "+id_turno);
                        Log.d("turno richiesto","turno_richiesto"+id_turno_richiesto);
                        if(status.equals("booked") && id_turno.equals(id_turno_richiesto)){
                            Log.d("checkBooking","CheckBooking True");
                            checkBooking = true;
                            break;
                        }
                    }
                }
            }
        });
        return checkBooking;
    }
    public static String getDayStringOld(Date date, Locale locale) {
        DateFormat formatter = new SimpleDateFormat("EEEE", locale);
        return formatter.format(date).substring(0, 1).toUpperCase() + formatter.format(date).substring(1).toLowerCase();

    }
}