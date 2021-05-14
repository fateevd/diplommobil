package com.example.raiq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class forgetpass_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass_activity);
        Button send_email = findViewById(R.id.forgetpass_send_email);
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button gotoAuth = findViewById(R.id.forgetpass_gotoauth);
        gotoAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(forgetpass_activity.this, Authoriz.class));
                finish();
            }
        });
    }
}