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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class userEdit extends AppCompatActivity {

    private EditText uName, uAddress, uMobile;
    private Button save, delete;
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
        delete = findViewById(R.id.delete_e);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();


        DocumentReference dbRef = FirebaseFirestore.getInstance().collection("User").document(fAuth.getCurrentUser().getPhoneNumber());


        dbRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot querySnapshot = task.getResult();
                User user = querySnapshot.toObject(User.class);
                uName.setText(user.getName());
                uAddress.setText(user.getAddress());
                uMobile.setText(user.getPhoneNumber());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(userEdit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = uName.getText().toString();
                String address = uAddress.getText().toString();
                String mobile = uMobile.getText().toString();

                User user = new User(name, address, mobile);
                dbRef.set(user);

                finish();
                Toast.makeText(userEdit.this, "updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(userEdit.this, userDetails.class));
            }
        });
        dbRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(userEdit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbRef.delete();
                Toast.makeText(userEdit.this, "Deactivated", Toast.LENGTH_SHORT).show();
                finish();

                startActivity(new Intent(userEdit.this, MainActivity.class));
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
