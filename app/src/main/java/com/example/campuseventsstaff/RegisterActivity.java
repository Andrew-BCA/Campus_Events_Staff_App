package com.example.campuseventsstaff;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
        String username = usernameEditText.getText().toString().trim().toUpperCase();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String Dept = DeptEditText.getText().toString().trim().toUpperCase();

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
            sendEmail();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
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

    private void sendEmail() {
        final String email = emailEditText.getText().toString().trim();
        final String recipientEmail = email;
        final String Rollno = usernameEditText.getText().toString().trim();

        // Replace with your Gmail
        final String senderEmail = "andrewpatrick011@gmail.com";
        final String senderPassword = "pvdf cmzm akqm bmrh";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Registration Successfull");
            message.setText("Dear,"+Rollno +"\n Your Registration process for RegistriX is successfully completed....");

            // Perform email sending in a background thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(getApplicationContext(), "Email sent successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Failed to send email", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } catch (MessagingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to send email", Toast.LENGTH_SHORT).show();
        }
    }
}
