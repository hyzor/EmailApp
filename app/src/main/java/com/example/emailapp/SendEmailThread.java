package com.example.emailapp;

import android.util.Log;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class SendEmailThread extends Thread {
    private Mail mail;
    private NewMessageActivity activity;

    public SendEmailThread(Mail mail, NewMessageActivity activity) {
        this.mail = mail;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            if (mail.send()) {
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
        } catch (Exception e) {
            e.printStackTrace();
            activity.displayMessage("Unexpected error occured!");
        }
    }
}