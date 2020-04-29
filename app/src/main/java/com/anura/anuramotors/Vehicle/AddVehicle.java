package com.anura.anuramotors.Vehicle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.anura.anuramotors.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.anura.anuramotors.data.model.vehicle;

public class AddVehicle extends AppCompatActivity {

    DatabaseReference dbref;
    Spinner spinner1, spinner2, spinner3;
    EditText etTopic;
    private Button btnAdd;
    vehicle vehicle;
    FirebaseDatabase database;
    FirebaseStorage storage;
   // Button Assbtn, importAssbtn;
    Uri pdfUri;
    ProgressDialog progressDialog;

    private void clearContrals(){

        spinner1.setSelection(0);
        spinner2.setSelection(0);
        spinner3.setSelection(0);
        etTopic.setText("");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        vehicle = new vehicle();
        spinner1 = findViewById(R.id.yearSpinner);
        spinner2 = findViewById(R.id.SemSpinner);
        spinner3 = findViewById(R.id.ModuleSpinner);
        etTopic = findViewById(R.id.topic);
        btnAdd = findViewById(R.id.addBtn);


        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();





        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dbref = FirebaseDatabase.getInstance().getReference().child("Velicle");

                if (pdfUri != null){
                    uploadFile(pdfUri);
                }
                else {
                   // Toast.makeText(AddVehicle.this,"Select a file", Toast.LENGTH_SHORT).show();
                }

                try {
                    if (TextUtils.isEmpty(spinner1.getSelectedItem().toString()))
                        Toast.makeText(getApplicationContext(),"  Select model... ", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(spinner2.getSelectedItem().toString()))
                        Toast.makeText(getApplicationContext(), " Select number...  ", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(spinner3.getSelectedItem().toString()))
                        Toast.makeText(getApplicationContext(), "   Select colour...  ",Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(etTopic.getText().toString() ))
                        Toast.makeText(getApplicationContext(), "  Insert a vehicle number  ",Toast.LENGTH_SHORT).show();

                    else{
                        vehicle.setVNO(spinner1.getSelectedItem().toString().trim());
                        vehicle.setTYPE(spinner2.getSelectedItem().toString().trim());
                        vehicle.setCOLOUR(spinner3.getSelectedItem().toString().trim());
                        vehicle.setAssTitle(etTopic.getText().toString().trim());


                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


                        dbref = FirebaseDatabase.getInstance().getReference().child("Velicle");
                        dbref.child("Vehicle Register").child(vehicle.getAssTitle()).setValue(vehicle);

                        Toast.makeText(getApplicationContext(), " Vehicle Add Successfully  ",Toast.LENGTH_SHORT).show();
                        clearContrals();

                        Intent i = new Intent(getApplicationContext(), RetriveVehicle.class);
                        startActivity(i);
                        finish();
                    }

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

}

            }
        });

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddVehicle.this,
            android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.year));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(myAdapter);

                spinner2= findViewById(R.id.SemSpinner);

                ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(AddVehicle.this,
        android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.semester));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(myAdapter1);

        spinner3 = findViewById(R.id.ModuleSpinner);

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(AddVehicle.this,
        android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.module));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(myAdapter2);

        }

        private void uploadFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File...");
        progressDialog.setProgressStyle(0);
        progressDialog.show();



        final String fileName = System.currentTimeMillis() + "";
        StorageReference storageReference = storage.getReference();

        storageReference.child("Uploads").child(fileName).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        String url = taskSnapshot.getUploadSessionUri().toString();

        DatabaseReference reference = database.getReference();
        reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

        if (task .isSuccessful()){
        Toast.makeText(AddVehicle.this," Upload successfully!",Toast.LENGTH_SHORT).show();
        }
        else{
        Toast.makeText(AddVehicle.this,"not upload Successfully!",Toast.LENGTH_SHORT).show();
        }
        }
        });


        }
        }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

        Toast.makeText(AddVehicle.this," not upload Successfully!",Toast.LENGTH_SHORT).show();


        }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

        int currentProgress = (int) (100* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
        progressDialog.setProgress(currentProgress);
        }
        });
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 && grantResults [0] == PackageManager.PERMISSION_GRANTED){
        selectPdf();

        }
        else {
        Toast.makeText(AddVehicle.this,"PLease Provide permission!",Toast.LENGTH_SHORT).show();
        }
        }

        private void selectPdf() {
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 86);
        }
        }
