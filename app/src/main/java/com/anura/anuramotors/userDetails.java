package com.anura.anuramotors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class userDetails extends AppCompatActivity {

    private Context mContext;
    private TextView mName;
    private TextView mAddress;
    private TextView mMobile;
    private String key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        mName = (TextView) findViewById(R.id.userName);
        mAddress = (TextView) findViewById(R.id.userAddress);
        mMobile = (TextView) findViewById(R.id.userMobile);

        Button edit_r_btn = findViewById(R.id.edit_r_btn);
        edit_r_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(userDetails.this, userEdit.class));
            }

        });

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(userDetails.this, userRegistration.class));
            }

        });
    }
}
