package com.example.emailapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class NewMessageActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        intent = getIntent();
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
    }

    public void displayMessage(final String message) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendMessage(View view) throws MessagingException {
        EditText recipientText = findViewById(R.id.recipientText);
        EditText subjectText = findViewById(R.id.subjectText);
        EditText messageText = findViewById(R.id.messageText);

        String recipient = recipientText.getText().toString();
        String subject = subjectText.getText().toString();
        String message = messageText.getText().toString();

        String from = intent.getStringExtra("EMAIL");

        MailHandler mailHandler = new MailHandler(MainActivity.getGmailService());
        MimeMessage mimeMessage = mailHandler.createEmail(recipient, from, subject, message);

        SendEmailThread email = new SendEmailThread(mailHandler, this, mimeMessage, "me");
        email.start();
    }
}
