package com.example.emailapp;

import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class SendEmailThread extends Thread {
    private MailHandler mailHandler;
    private NewMessageActivity activity;
    private MimeMessage mimeMessage;
    private String userId;

    public SendEmailThread(MailHandler mailHandler, NewMessageActivity activity, MimeMessage mimeMessage, String userId) {
        this.mailHandler = mailHandler;
        this.activity = activity;
        this.mimeMessage = mimeMessage;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            if (mailHandler.sendMessage(userId, mimeMessage) != null) {
                activity.displayMessage("Email sent!");
                activity.finish();
            } else {
                activity.displayMessage("Email failed to send!");
            }
        } catch (AuthenticationFailedException e) {
            Log.e(SendEmailThread.class.getName(), "Bad account details!");
            e.printStackTrace();
            activity.displayMessage("Authentication failed!");
        } catch (MessagingException e) {
            Log.e(SendEmailThread.class.getName(), "Email failed!");
            e.printStackTrace();
            activity.displayMessage("Email failed to send!");
        } catch (UserRecoverableAuthIOException e) {
            activity.startActivityForResult(e.getIntent(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            activity.displayMessage("Unexpected error occured!");
        }
    }
}