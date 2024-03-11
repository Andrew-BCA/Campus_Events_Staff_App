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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
                    titleTextView.setText("Event Name: "+data.getEvent());

                    TextView departTextView = new TextView(EventViewActivity.this);
                    departTextView.setText("Organizing Dept.: "+data.getDept());

                    TextView linkTextView = new TextView(EventViewActivity.this);
                    linkTextView.setText("Registration Date: "+data.getRegDate());

                    TextView dateTextView = new TextView(EventViewActivity.this);
                    dateTextView.setText("Event Date: " + data.getDate());


                    // Create a delete button for each event
                    Button deleteButton = new Button(EventViewActivity.this);
                    deleteButton.setText("Delete");
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Implement code to delete the event from the database
                            String eventId = snapshot.getKey();
                            deleteEvent(eventId);
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

    private void deleteEvent(String eventId) {
        // Delete event from "events" node
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
        eventsRef.child(eventId).removeValue();

        // Delete event's participants from "participants" node for each department
        DatabaseReference participantsRef = FirebaseDatabase.getInstance().getReference("participants");
        participantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot deptSnapshot : dataSnapshot.getChildren()) {
                    String deptKey = deptSnapshot.getKey();
                    DatabaseReference eventParticipantsRef = deptSnapshot.child(eventId).getRef();
                    eventParticipantsRef.removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        // Delete event's brochure from storage
        deleteBrochure(eventId);
    }


    private void deleteBrochure(String eventId) {
        // Construct the brochure reference based on eventId
        StorageReference brochureRef = FirebaseStorage.getInstance().getReference().child("brochures/" + eventId + "_brochure.pdf");

        // Delete the brochure
        brochureRef.delete().addOnSuccessListener(aVoid -> {
            // Brochure deleted successfully
            // You can add any additional handling here if needed
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });
    }
}
