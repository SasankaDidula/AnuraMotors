package com.anura.anuramotors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anura.anuramotors.common.common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.anura.anuramotors.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class userRegistration extends AppCompatActivity {
    EditText mName;
    EditText mAddress;
    EditText mMobile;
    Button btnAdd;

    DocumentReference dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);


        mName = (EditText) findViewById(R.id.name_reg);
        mAddress = (EditText) findViewById(R.id.address_reg);
        mMobile = (EditText) findViewById(R.id.mobile_reg);

        btnAdd = (Button) findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addUser();
                startActivity(new Intent(userRegistration.this, userDetails.class));
            }

        });
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(userRegistration.this, MainActivity.class));
            }

        });
    }

    private void addUser() {
        String name = mName.getText().toString().trim();
        String address = mAddress.getText().toString().trim();
        String phoneNumber = mMobile.getText().toString().trim();


        if (!TextUtils.isEmpty(name)) {

            User user = new User(name, address, phoneNumber);
            dbUsers = FirebaseFirestore.getInstance()
                    .collection("User")
                    .document(phoneNumber);


            dbUsers.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getBaseContext(), "User added", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), "you should enter name", Toast.LENGTH_LONG).show();
                }
            });


        }
    }
}