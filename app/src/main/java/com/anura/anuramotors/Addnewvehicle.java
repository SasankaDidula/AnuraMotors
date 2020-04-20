package com.anura.anuramotors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Addnewvehicle extends AppCompatActivity {
    EditText Vehicle,Type,vehiclenumber,color,details;
    Button button5;
    DatabaseReference reff;
    addvdata addvdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewvehicle);
        Vehicle = findViewById(R.id.Vehicle);
        Type=findViewById(R.id.Type);
        vehiclenumber = findViewById(R.id.vehiclenumber);
        color = findViewById(R.id.color);
        details = findViewById(R.id.details);
        button5 = findViewById(R.id.button5);
        addvdata = new addvdata();
        reff = FirebaseDatabase.getInstance().getReference().child("addvdata");


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addvdata.setVNO(Vehicle.getText().toString().trim());
                addvdata.setTYPE(Type.getText().toString().trim());
                addvdata.setCOLOUR(color.getText().toString().trim());
                addvdata.setDETAILS(details.getText().toString().trim());
                reff.push().setValue(addvdata);
                Toast.makeText(Addnewvehicle.this,"data inserted sucessfully",Toast.LENGTH_LONG).show();


            }
        });


















    }
}
