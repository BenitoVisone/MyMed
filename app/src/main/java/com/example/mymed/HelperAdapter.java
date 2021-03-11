package com.example.mymed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class HelperAdapter extends RecyclerView.Adapter {

    List<Doctors> doctorsList;
    private FirebaseAuth firebaseAuth;

    public HelperAdapter(List<Doctors> doctorsList) {
        this.doctorsList = doctorsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolderClass viewHolderClass=(ViewHolderClass)holder;

        Doctors doctor = doctorsList.get(position);
        viewHolderClass.name.setText(doctor.getName());
        viewHolderClass.surname.setText(doctor.getSurname());
        viewHolderClass.birth_date.setText(doctor.getBirth_date());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setPositiveButton("Sì",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseAuth = FirebaseAuth.getInstance();
                        String user_id = firebaseAuth.getCurrentUser().getUid();
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mymed-b094e-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference myRef = database.getReference();
                        myRef.child("users").child("patients").child(user_id).child("doctor").setValue(doctor.getKey());
                        Log.d("HelperAdapter", "ID Dottore: "+doctor.getKey());
                        Toast.makeText(view.getContext(), "Hai aggiunto correttamente il tuo medico", Toast.LENGTH_LONG).show();
                        view.getContext().startActivity(new Intent(view.getContext(),UserActivity.class));
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                builder.setMessage("Vuoi aggiungere "+ doctor.getSurname() + " "+ doctor.getName()+" come tuo medico?")
                        .setTitle("Aggiunta medico");
                AlertDialog dialog = builder.create();

                dialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView name, surname, birth_date;
        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_text);
            surname = itemView.findViewById(R.id.surname_text);
            birth_date = itemView.findViewById(R.id.birth_date_text);
        }
    }
}
