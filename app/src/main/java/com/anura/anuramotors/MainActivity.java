package com.anura.anuramotors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button appointment = findViewById(R.id.appointment);
        appointment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Appointments.class));
            }

        });

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, registerUser.class));
            }

        });

    }
    public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, Appointments.class));
    }
}
