package com.example.campuseventsstaff;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    TextView welcome;
    ImageView add,viewevents,viewpart,share,studentdetails,sharemail;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcome = findViewById(R.id.welcome);
        add = findViewById(R.id.add_event);
        viewevents = findViewById(R.id.view_event);
        share = findViewById(R.id.share_messages);
        viewpart = findViewById(R.id.view_participants);
        studentdetails = findViewById(R.id.viewStudentsdetails);
        sharemail = findViewById(R.id.sharemail);

        // Retrieve the roll number from SharedPreferences
        String username = getSharedPreferences("user_info", MODE_PRIVATE)
                .getString("uname", "default_value_if_not_found");

        welcome.setText(username);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,EventActivity.class);
                startActivity(i);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Share_Messages.class);
                startActivity(i);
            }
        });

        viewpart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, ViewParticipantsActivity.class);
                startActivity(i);
            }
        });

        viewevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,EventViewActivity.class);
                startActivity(i);
            }
        });

        studentdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, ViewStudentsDetails.class);
                startActivity(i);
            }
        });

        sharemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Share_Mail.class);
                startActivity(i);
            }
        });

    }
}