package com.example.campuseventsstaff;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventActivity extends AppCompatActivity {

    private EditText event,dept,date,regdate;
    private Button addButton,viewevent,viewpart;
    // Firebase
    private FirebaseDatabase database;
    private DatabaseReference tasksRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        event = findViewById(R.id.eventName);
        dept = findViewById(R.id.deptName);
        date = findViewById(R.id.eventDate);
        regdate = findViewById(R.id.regDate);
        addButton = findViewById(R.id.addButton);
        viewevent = findViewById(R.id.vieweventButton);
        viewpart = findViewById(R.id.viewregButton);


        viewevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventActivity.this,EventViewActivity.class);
                startActivity(i);
            }
        });

        viewpart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventActivity.this, ViewParticipantsActivity.class);
                startActivity(i);
            }
        });

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference("events");


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newevent = event.getText().toString().trim();
                String newdept = dept.getText().toString().trim();
                String newdate = date.getText().toString().trim();
                String newreg = regdate.getText().toString().trim();

                Events E = new Events(newevent,newdept,newdate,newreg);
                if (!newevent.isEmpty()&&!newdept.isEmpty()&&!newdate.isEmpty()&&!newreg.isEmpty()) {
                    // Push the new task to Firebase
                    tasksRef.child(newevent).setValue(E);
                    event.setText("");

                    Toast.makeText(EventActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}