package com.example.campuseventsstaff;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EventActivity extends AppCompatActivity {

    private EditText event, dept, date, regdate,addinfo;
    private Button addButton, uploadBrochureButton;
    // Firebase
    private FirebaseDatabase database;
    private DatabaseReference tasksRef;
    private FirebaseStorage storage;
    private StorageReference brochureRef;
    private static final int PICK_BROCHURE_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        event = findViewById(R.id.eventName);
        dept = findViewById(R.id.deptName);
        date = findViewById(R.id.eventDate);
        regdate = findViewById(R.id.regDate);
        addinfo = findViewById(R.id.addinfo);
        addButton = findViewById(R.id.addButton);
        uploadBrochureButton = findViewById(R.id.uploadBrochureButton);

        String username = getSharedPreferences("user_info", MODE_PRIVATE)
                .getString("uname", "default_value_if_not_found");

        String deptartment = getSharedPreferences("user_info", MODE_PRIVATE)
                .getString("dept", "default_value_if_not_found");

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference("events").child(deptartment).child(username);
        storage = FirebaseStorage.getInstance();
        brochureRef = storage.getReference().child("brochures");




        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newevent = event.getText().toString().trim();
                String newdept = dept.getText().toString().trim();
                String newdate = date.getText().toString().trim();
                String newreg = regdate.getText().toString().trim();
                String addinfos = addinfo.getText().toString().trim();

                Events E = new Events(newevent, newdept, newdate, newreg,addinfos);
                if (!newevent.isEmpty() && !newdept.isEmpty() && !newdate.isEmpty() && !newreg.isEmpty()) {
                    // Push the new task to Firebase
                    tasksRef.child(newevent).setValue(E);
                    event.setText("");
                    dept.setText("");
                    date.setText("");
                    regdate.setText("");
                    addinfo.setText("");

                    Toast.makeText(EventActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        uploadBrochureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent();
        intent.setType("application/pdf"); // You can specify other MIME types for different types of files
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Brochure"), PICK_BROCHURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_BROCHURE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri brochureUri = data.getData();
            uploadBrochure(brochureUri);
        }
    }

    private void uploadBrochure(Uri brochureUri) {
        if (brochureUri != null) {
            StorageReference fileRef = brochureRef.child(event.getText().toString().trim()+".pdf");
            UploadTask uploadTask = fileRef.putFile(brochureUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Brochure uploaded successfully
                Toast.makeText(EventActivity.this, "Brochure Uploaded", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                Toast.makeText(EventActivity.this, "Failed to upload brochure", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
