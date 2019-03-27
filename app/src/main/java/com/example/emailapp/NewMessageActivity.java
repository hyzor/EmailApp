package com.example.emailapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
    }

    public void displayMessage(final String message) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendMessage(View view) {
        EditText recipientText = findViewById(R.id.recipientText);
        EditText subjectText = findViewById(R.id.subjectText);
        EditText messageText = findViewById(R.id.messageText);

        String recipient = recipientText.getText().toString();
        String subject = subjectText.getText().toString();
        String message = messageText.getText().toString();
        String from = "hyzore@gmail.com";
        String pass = "fbX#UmA1!B2g%E8E";

        Mail mail = new Mail(from, pass);
        mail.set_from(from);
        mail.setBody(message);
        mail.set_to(new String[] { recipient });
        mail.set_subject(subject);

        SendEmailThread email = new SendEmailThread(mail, this);
        email.start();
    }
}
