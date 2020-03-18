package com.anura.anuramotors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Appointments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        Button back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Appointments.this, MainActivity.class));
            }

        });

        Button addAppointment = findViewById(R.id.add_apointment_btn);
        addAppointment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Appointments.this, AddNewAppoinment.class));
            }

        });
    }

    public void appointmentClick(View view) {
        startActivity(new Intent(Appointments.this, appoinmentPage.class));
    }
}
