package com.example.campuseventsstaff;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Share_Mail extends AppCompatActivity {

    EditText subject, tomail, content;
    Button send, attachButton;
    Uri attachmentUri; // URI of the attachment file

    private static final int ATTACHMENT_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_mail);

        subject = findViewById(R.id.subject);
        tomail = findViewById(R.id.tomail);
        content = findViewById(R.id.content);
        send = findViewById(R.id.sendmail);
        attachButton = findViewById(R.id.attachButton);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, ATTACHMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ATTACHMENT_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            attachmentUri = data.getData();
            String fileName = getFileName(attachmentUri);
            Toast.makeText(this, "Selected File: " + fileName, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void sendEmail() {
        final String email = tomail.getText().toString().trim();
        final String recipientEmail = email;
        final String sub = subject.getText().toString();
        final String con = content.getText().toString();

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
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(sub);
            message.setText(con);

            if (attachmentUri != null) {
                try {
                    // Get the real path of the attachment URI
                    String realPath = getRealPathFromUri(attachmentUri);

                    // Create a DataSource for the attachment
                    DataSource source = new FileDataSource(realPath);

                    // Create a MimeBodyPart for the attachment
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.setDataHandler(new DataHandler(source));
                    attachmentPart.setFileName(getFileName(attachmentUri));

                    // Create a MimeMultipart to hold both the attachment and the text part
                    MimeMultipart multipart = new MimeMultipart();
                    multipart.addBodyPart(attachmentPart);

                    // Set the text content of the message
                    MimeBodyPart textPart = new MimeBodyPart();
                    textPart.setText(con);
                    multipart.addBodyPart(textPart);

                    // Set the multipart content as the message content
                    message.setContent(multipart);

                }  catch (MessagingException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "MessagingException: Failed to attach file", Toast.LENGTH_SHORT).show();
            }

            }


            Transport.send(message);
            Toast.makeText(getApplicationContext(), "Email sent successfully", Toast.LENGTH_SHORT).show();
            subject.setText("");
            content.setText("");
            tomail.setText("");
        } catch (MessagingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to send email", Toast.LENGTH_SHORT).show();
        }
    }


    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return uri.getPath();
    }
}
