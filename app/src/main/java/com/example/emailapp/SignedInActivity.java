package com.example.emailapp;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class SignedInActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView userText;
    private TextView userDetailText;
    private Button signOutBtn;
    private static final HttpTransport TRANSPORT = AndroidHttp.newCompatibleTransport();
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static Gmail gmailService;
    private String email;
    private String uid;

    private static final String[] SCOPES = {
            GmailScopes.GMAIL_LABELS,
            GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_INSERT,
            GmailScopes.GMAIL_MODIFY,
            GmailScopes.GMAIL_READONLY,
            GmailScopes.MAIL_GOOGLE_COM
    };

    @NonNull
    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, SignedInActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    private void updateUI() {
        userText.setText(email);
        userDetailText.setText(uid);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(MainActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_signed_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userText = findViewById(R.id.userText);
        userDetailText = findViewById(R.id.userDetailText);
        signOutBtn = findViewById(R.id.signOutBtn);

        signOutBtn.setOnClickListener(this);
        fab.setOnClickListener(this);

        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);

        if (response != null) {
            email = response.getEmail();
        } else {
            email = currentUser.getEmail();
            uid = currentUser.getUid();
        }

        // Create credentials from this Google account to authorize Gmail service
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                SignedInActivity.this, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        credential.setSelectedAccount(new Account(email, "com.google"));
        gmailService = new Gmail.Builder(TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(getResources().getString(R.string.app_name))
                .build();

        updateUI();
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(SignedInActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.signOutBtn) {
            signOut();
        } else if (viewId == R.id.fab) {
            startNewMessageActivity(email);
        }
    }

    public static Gmail getGmailService() {
        return gmailService;
    }

    private void startNewMessageActivity(String email) {
        startActivity(NewMessageActivity.createIntent(this, email));
    }
}
