package com.example.raiq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class forgetpass_succ_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass_succ_activity);
        Button gotoAuth = findViewById(R.id.forgetpass_succ_gotoauth);
        gotoAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(forgetpass_succ_activity.this,Authoriz.class));
                finish();
            }
        });
    }
}