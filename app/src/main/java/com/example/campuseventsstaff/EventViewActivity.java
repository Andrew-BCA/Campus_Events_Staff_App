package com.example.campuseventsstaff;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@SuppressLint("MissingInflatedId")
public class EventViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        FirebaseApp.initializeApp(this);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");

        // Reference to LinearLayout to dynamically add TextViews
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear existing views before adding new ones
                linearLayout.removeAllViews();

                // Iterate through all events
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Events data = snapshot.getValue(Events.class);

                    // Create a LinearLayout to hold each event's information and delete button
                    LinearLayout eventLayout = new LinearLayout(EventViewActivity.this);
                    eventLayout.setOrientation(LinearLayout.VERTICAL);

                    // Create TextViews dynamically for each event
                    TextView titleTextView = new TextView(EventViewActivity.this);
                    titleTextView.setText(data.getEvent());

                    TextView departTextView = new TextView(EventViewActivity.this);
                    departTextView.setText(data.getDept());

                    TextView linkTextView = new TextView(EventViewActivity.this);
                    linkTextView.setText(data.getRegDate());

                    TextView dateTextView = new TextView(EventViewActivity.this);
                    dateTextView.setText(data.getDate());


                    // Create a delete button for each event
                    Button deleteButton = new Button(EventViewActivity.this);
                    deleteButton.setText("Delete");
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Implement code to delete the event from the database
                            databaseReference.child(snapshot.getKey()).removeValue();
                        }
                    });

                    // Add TextViews and delete button to the eventLayout
                    eventLayout.addView(titleTextView);
                    eventLayout.addView(departTextView);
                    eventLayout.addView(dateTextView);
                    eventLayout.addView(linkTextView);
                    eventLayout.addView(deleteButton);

                    // Add eventLayout to the main LinearLayout
                    linearLayout.addView(eventLayout);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors, add your code here
            }
        });
    }
}
