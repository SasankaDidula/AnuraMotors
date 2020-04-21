package com.anura.anuramotors.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anura.anuramotors.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scorpion.anuramotors.data.model.vehicle;

public class EditVehicle extends AppCompatActivity {

    Button editAssBtn,deleteAssBtn;
    vehicle vehicle;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference dBRef;

    private EditText txtModule,txtSem,txtYear,txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        txtModule = findViewById(R.id.txtModule);
        txtSem = findViewById(R.id.txtSem);
        txtYear = findViewById(R.id.txtYear);
        txtTitle = findViewById(R.id.txtTitle);

        editAssBtn = findViewById(R.id.editAssBtn);
        deleteAssBtn = findViewById(R.id.btnDelete);

        vehicle = new vehicle();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();

        String assTitle = intent.getStringExtra("assTitle");
        String year = intent.getStringExtra("year");
        String sem = intent.getStringExtra("sem");
        String module = intent.getStringExtra("module");

        Toast.makeText(this, ""+assTitle, Toast.LENGTH_SHORT).show();

        vehicle.setAssTitle(assTitle);
        vehicle.setVNO(year);
        vehicle.setTYPE(sem);
        vehicle.setCOLOUR(module);

        txtTitle.setText(vehicle.getAssTitle());
        txtYear.setText(vehicle.getVNO());
        txtSem.setText(vehicle.getTYPE());
        txtModule.setText(vehicle.getCOLOUR());

        //update code!
        editAssBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();

                DatabaseReference updateReference = FirebaseDatabase.getInstance().getReference().child("Velicle").child(firebaseUser.getUid());
                updateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(firebaseUser.getUid())){
                            try{

                                vehicle.setAssTitle(txtTitle.getText().toString().trim());
                                vehicle.setCOLOUR(txtModule.getText().toString().trim());
                                vehicle.setTYPE(txtSem.getText().toString().trim());
                                vehicle.setVNO(txtYear.getText().toString().trim());

                               dBRef = FirebaseDatabase.getInstance().getReference().child("Velicle").child(firebaseUser.getUid());
                               dBRef.setValue(vehicle);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(), "Inavlied Assignment!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Enter an assignment",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });

        //delete code
        deleteAssBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Velicle");
                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(firebaseUser.getUid())){
                            dBRef = FirebaseDatabase.getInstance().getReference().child("Velicle").child(firebaseUser.getUid());
                            delRef.removeValue();
                            Toast.makeText(getApplicationContext(),"Assignment deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"First add an Assignment!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }
}


