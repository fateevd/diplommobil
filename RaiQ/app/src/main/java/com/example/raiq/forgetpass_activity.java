package com.example.raiq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpass_activity extends AppCompatActivity {

    EditText mailBox;
    Button send_email, gotoAuth;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass_activity);
        mailBox = findViewById(R.id.forgetpass_mail);
        send_email = findViewById(R.id.forgetpass_send_email);
        auth = FirebaseAuth.getInstance();
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mailBox.getText().toString().trim().equals("")) {
                    auth.sendPasswordResetEmail(mailBox.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                startActivity(new Intent(forgetpass_activity.this,forgetpass_succ_activity.class).addFlags(new Intent().FLAG_ACTIVITY_CLEAR_TASK | new Intent().FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            } else if(task.isCanceled()) {
                                Toast.makeText(forgetpass_activity.this, "Неверно указана почта", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(forgetpass_activity.this, "Неверно указана почта или аккаунт с данной почтой не существует", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        gotoAuth = findViewById(R.id.forgetpass_gotoauth);
        gotoAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(forgetpass_activity.this,Authoriz.class).addFlags(new Intent().FLAG_ACTIVITY_CLEAR_TASK | new Intent().FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }
}