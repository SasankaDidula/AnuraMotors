package com.anura.anuramotors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anura.anuramotors.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

public class userEdit extends AppCompatActivity {

    private EditText uName, uAddress, uMobile;
    private Button save;
    private FirebaseAuth fAuth;
    private FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        uName = findViewById(R.id.name_e);
        uAddress = findViewById(R.id.address_e);
        uMobile = findViewById(R.id.mNo_e);
        save = findViewById(R.id.update_e);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();


        DatabaseReference dbRef = db.getReference(fAuth.getUid());


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                uName.setText(user.getName());
                uAddress.setText(user.getAddress());
                uMobile.setText(user.getPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(userEdit.this, databaseError.getCode(), Toast.LENGTH_LONG).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = uName.getText().toString();
                String address = uAddress.getText().toString();
                String mobile = uMobile.getText().toString();

                User user = new User(name, address, mobile);
                dbRef.setValue(user);
                finish();
            }
        });
        Button back = findViewById(R.id.back_e);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(userEdit.this, userDetails.class));
            }

        });
    }
}
