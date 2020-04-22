package com.anura.anuramotors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class userDetails extends AppCompatActivity {
    FirebaseAuth fAuth;
    private Context mContext;
    private TextView mName;
    private TextView mAddress;
    private TextView mMobile;
    private String key;
    private FirebaseFirestore  db;

//    private DocumentReference userRef = db.collection("User").document("phoneNumber");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);




        mName = (TextView) findViewById(R.id.userName);
        mAddress = (TextView) findViewById(R.id.userAddress);
        mMobile = (TextView) findViewById(R.id.userMobile);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        key = fAuth.getCurrentUser().getPhoneNumber();
        DocumentReference documentReference = db.collection("User").document(key);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                mName.setText(documentSnapshot.getString("name"));
                mAddress.setText(documentSnapshot.getString("address"));
                mMobile.setText(documentSnapshot.getString("phoneNumber"));
            }
        });
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
