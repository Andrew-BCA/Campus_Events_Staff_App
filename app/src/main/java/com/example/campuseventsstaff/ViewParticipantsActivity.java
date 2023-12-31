package com.example.campuseventsstaff;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("MissingInflatedId")
public class ViewParticipantsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParticipantsAdapter adapter;
    private List<Participants> participantsList;
    private EditText searchEditText,searchEventText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_participants);

        FirebaseApp.initializeApp(this);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("participants");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        participantsList = new ArrayList<>();
        adapter = new ParticipantsAdapter(participantsList);
        recyclerView.setAdapter(adapter);

        searchEditText = findViewById(R.id.searchEditText);
        searchEventText = findViewById(R.id.searchEventText);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                participantsList.clear();
                for (DataSnapshot eventDeptSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot eventSnapshot : eventDeptSnapshot.getChildren()) {
                        for (DataSnapshot participantSnapshot : eventSnapshot.getChildren()) {
                            Participants data = participantSnapshot.getValue(Participants.class);
                            participantsList.add(data);
                        }
                    }
                }
                filterData(searchEditText.getText().toString());
                filterData(searchEventText.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors, add your code here
            }
        });

        // Add a TextChangedListener to the search EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Filter data based on the search query
                filterData(editable.toString());
            }
        });

        searchEventText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Filter data based on the search query
                filterEventData(editable.toString());
            }
        });
    }

    private void filterData(String query) {
        List<Participants> filteredList = new ArrayList<>();

        for (Participants participant : participantsList) {
            if (participant.getEventDept().toLowerCase().contains(query.toLowerCase()) ) {
                filteredList.add(participant);
            }
        }

        // Update the RecyclerView with the filtered data
        adapter.setData(filteredList);
        adapter.notifyDataSetChanged();
    }

    private void filterEventData(String query) {
        List<Participants> filteredList = new ArrayList<>();

        for (Participants participant : participantsList) {
            if (participant.getEventName().toLowerCase().contains(query.toLowerCase()) ) {
                filteredList.add(participant);
            }
        }

        // Update the RecyclerView with the filtered data
        adapter.setData(filteredList);
        adapter.notifyDataSetChanged();
    }
}
