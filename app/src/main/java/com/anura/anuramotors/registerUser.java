package com.anura.anuramotors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anura.anuramotors.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registerUser extends AppCompatActivity {

    EditText rName, rEmail, rAddress, rPassword, rMobile;
    Button btnReg;
    FirebaseAuth rFirebaseAuth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        fStore=FirebaseFirestore.getInstance();
        rFirebaseAuth = FirebaseAuth.getInstance();
        rAddress = (EditText)findViewById(R.id.address_reg);
        rName = (EditText)findViewById(R.id.name_reg);

        rMobile = (EditText)findViewById(R.id.mobile_reg);

        btnReg = findViewById(R.id.btn_add);
        CollectionReference documentReference = fStore.collection("User");
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = rAddress.getText().toString();
                String name = rName.getText().toString();

                String mobile = rMobile.getText().toString();


                if (TextUtils.isEmpty(name)) {
                    rPassword.setError("password is required");
                    return;
                }
                if (TextUtils.isEmpty(mobile)) {
                    rEmail.setError("email is required");
                    return;
                } else if (!(name.isEmpty() && mobile.isEmpty())) {

                    User user = new User();
                    user.setAddress(address);
                    user.setName(name);

                    user.setPhoneNumber(mobile);
                    documentReference.document(user.getPhoneNumber()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(registerUser.this, "added ", Toast.LENGTH_LONG);
                            startActivity(new Intent(registerUser.this,MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(registerUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }});

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(registerUser.this, MainActivity.class));
            }

        });

    }
}
