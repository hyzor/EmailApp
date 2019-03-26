package com.example.emailapp;

import android.app.Activity;
import android.content.Intent;
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

    public void sendMessage(View view) {
        EditText recipientText = findViewById(R.id.recipientText);
        EditText subjectText = findViewById(R.id.subjectText);
        EditText messageText = findViewById(R.id.messageText);

        String recipient = recipientText.getText().toString();
        String subject = subjectText.getText().toString();
        String message = messageText.getText().toString();

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[] { recipient });
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);
        email.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(email, "Choose e-mail client"));
            setResult(Activity.RESULT_OK, email);
            finish();
            Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No e-mail client found!", Toast.LENGTH_SHORT).show();
        }
    }
}
