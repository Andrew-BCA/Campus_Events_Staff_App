package com.example.campuseventsstaff;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, DeptEditText;
    private Button registerButton;

    // Firebase
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("staff");

        // Initialize views
        usernameEditText = findViewById(R.id.editTextUsername);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.buttonRegister);
        DeptEditText = findViewById(R.id.editTextDept);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String Dept = DeptEditText.getText().toString().trim();

        // Check if fields are not empty
        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !Dept.isEmpty()) {
            // Encrypt the password
            String encryptedPassword = encryptPassword(password);

            // Create a unique user ID using email
            String userId = username;

            // Create User object with encrypted password
            User user = new User(username, email, encryptedPassword, Dept);

            // Add user to the database
            databaseReference.child(Dept).child(userId).setValue(user);

            // You can add additional logic here, like showing a success message
            Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);

        } else {
            // Display an error message if any field is empty
            // You can customize this part based on your requirements
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to encrypt password using SHA-256 algorithm
    private String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convert bytes to hexadecimal representation
            StringBuilder builder = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                builder.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
