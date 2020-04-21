package com.anura.anuramotors.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anura.anuramotors.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.scorpion.anuramotors.data.model.vehicle;

import java.util.ArrayList;

public class RetriveVehicle extends AppCompatActivity {

    //create a new object for the button
    Button addBtn;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private ArrayList<vehicle> arrayList;
    private FirebaseRecyclerOptions<vehicle> options;
    private FirebaseRecyclerAdapter<vehicle, FirebaseViewHolderVehicle> adapter;
    private DatabaseReference databaseReference;


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_vehicle);

        recyclerView = findViewById(R.id.recyclerviewAss);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<vehicle>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Velicle").child(firebaseUser.getUid());
        databaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<vehicle>().setQuery(databaseReference, vehicle.class).build();

        adapter = new FirebaseRecyclerAdapter<vehicle, FirebaseViewHolderVehicle>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolderVehicle holder, int position, @NonNull final vehicle model) {
                holder.assTitle.setText(model.getAssTitle());
                holder.year.setText(model.getVNO());
                holder.sem.setText(model.getTYPE());
                holder.module.setText(model.getCOLOUR());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RetriveVehicle.this, EditVehicle.class);
                        intent.putExtra("assTitle",model.getAssTitle());
                        intent.putExtra("year",model.getVNO());
                        intent.putExtra("sem",model.getTYPE());
                        intent.putExtra("module",model.getCOLOUR());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FirebaseViewHolderVehicle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolderVehicle(LayoutInflater.from(RetriveVehicle.this).inflate(R.layout.row_vehicle,parent,false));
            }
        };


        recyclerView.setAdapter(adapter);





        //assign it to what we want
        addBtn = findViewById(R.id.AddNewAsBtn);

        //click event
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), AddVehicle.class);
                startActivity(i);
            }
        });
    }

}
