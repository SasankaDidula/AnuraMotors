package com.anura.anuramotors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class yourvehicle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourvehicle);

        Button addAppointment = findViewById(R.id.submit_r);
        addAppointment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(yourvehicle.this, Addnewvehicle.class));
            }

        });
    }
}
