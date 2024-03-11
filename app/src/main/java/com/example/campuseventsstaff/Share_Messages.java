package com.example.campuseventsstaff;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Share_Messages extends AppCompatActivity {

    EditText subject, message;
    Button upload, sharemessage;
    private FirebaseDatabase database;
    private DatabaseReference tasksRef;
    private FirebaseStorage storage;
    private StorageReference brochureRef;
    private static final int PICK_BROCHURE_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_messages);

        upload = findViewById(R.id.upload_file);
        subject = findViewById(R.id.sub);
        message = findViewById(R.id.message);
        sharemessage = findViewById(R.id.share);

        database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference("messages");

        // Retrieve the roll number from SharedPreferences
        String username = getSharedPreferences("user_info", MODE_PRIVATE)
                .getString("username", "default_value_if_not_found");


        sharemessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sub = subject.getText().toString().trim();
                String mess = message.getText().toString().trim();

                if (!sub.isEmpty() && !mess.isEmpty() && !username.isEmpty()) {
                    // Push the new task to Firebase

                    Message M = new Message(username, mess, sub);
                    String messageId = tasksRef.push().getKey();
                    if (messageId != null) {
                        tasksRef.child(username).child(sub).setValue(M);
                    }

                    Toast.makeText(Share_Messages.this, "Message Shared", Toast.LENGTH_SHORT).show();
                }
            }
        });

        storage = FirebaseStorage.getInstance();
        brochureRef = storage.getReference().child("files");

        upload.setOnClickListener(new View.OnClickListener() {
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
            StorageReference fileRef = brochureRef.child(subject.getText().toString() + "_brochure.pdf");
            UploadTask uploadTask = fileRef.putFile(brochureUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Brochure uploaded successfully
                Toast.makeText(Share_Messages.this, "File Uploaded", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                Toast.makeText(Share_Messages.this, "Failed to upload brochure", Toast.LENGTH_SHORT).show();
            });
        }
    }

}
