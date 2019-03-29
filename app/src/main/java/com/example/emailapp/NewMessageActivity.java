package com.example.emailapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.util.ExtraConstants;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class NewMessageActivity extends AppCompatActivity {
    private Intent intent;

    @NonNull
    public static Intent createIntent(@NonNull Context context, @NonNull String email) {
        return new Intent().setClass(context, NewMessageActivity.class)
                .putExtra(ExtraConstants.EMAIL, email);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        intent = getIntent();
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

        String from = intent.getStringExtra(ExtraConstants.EMAIL);

        MailHandler mailHandler = new MailHandler(SignedInActivity.getGmailService());
        MimeMessage mimeMessage = mailHandler.createEmail(recipient, from, subject, message);

        SendEmailThread email = new SendEmailThread(mailHandler, this, mimeMessage, "me");
        email.start();
    }
}
