package com.example.campuseventsstaff;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewStudentsDetails extends AppCompatActivity {

    private TextView userDetailsTextView;
    private EditText rollNoEditText, deptEditText;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students_details);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        userDetailsTextView = findViewById(R.id.userDetailsTextView);
        rollNoEditText = findViewById(R.id.editTextRollNo);
        deptEditText = findViewById(R.id.editTextDept);

        // Call method to retrieve and display user details when a button is clicked
        findViewById(R.id.buttonRetrieve).setOnClickListener(v -> displayUserDetails());
    }

    private void displayUserDetails() {
        final String rollNo = rollNoEditText.getText().toString().trim().toUpperCase();
        final String dept = deptEditText.getText().toString().trim().toUpperCase();

        databaseReference.child(dept).child(rollNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Students user = dataSnapshot.getValue(Students.class);
                    if (user != null) {
                        // Build the string to display user details
                        StringBuilder userDetails = new StringBuilder();
                        userDetails.append("Username: ").append(user.getUsername()).append("\n");
                        userDetails.append("Roll Number: ").append(rollNo).append("\n");
                        userDetails.append("Department: ").append(user.getDept()).append("\n");
                        userDetails.append("Email: ").append(user.getEmail()).append("\n");
                        userDetails.append("Mobile: ").append(user.getMobile()).append("\n");

                        // Display the user details in the TextView
                        userDetailsTextView.setText(userDetails.toString());
                    }
                } else {
                    // User with the specified Rollno and Dept not found
                    userDetailsTextView.setText("User details not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                userDetailsTextView.setText("Error: " + databaseError.getMessage());
            }
        });
    }
}
