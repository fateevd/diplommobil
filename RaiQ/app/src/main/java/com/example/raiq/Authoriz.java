package com.example.raiq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authoriz extends AppCompatActivity {

    Button auth, gowww;
    TextView registr, gotoforgetpass;
    Toast toast;
    EditText  emailEdit;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
        {
            Intent intent = new Intent(Authoriz.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void signIn(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Authoriz.this,MainActivity.class);
                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            ShowMess("Ошибка входа","Проверьте правильно ли указаны все данные");
                        }
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authoriz);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA},1);
            }
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }
        }
        final SharedPreferences sett = this.getApplicationContext().getSharedPreferences("Settings", 0);
        if (sett.getBoolean("DayThem",false) == true)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        toast = new Toast(getApplicationContext());
        gotoforgetpass = findViewById(R.id.auth_change_password);
        //Смена пароля на сайте
        gotoforgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Authoriz.this, forgetpass_activity.class));
                finish();
            }
        });
        emailEdit=findViewById(R.id.auth_mail);
        auth = findViewById(R.id.auth_comein);
        registr = findViewById(R.id.auth_registr);
        //Вход в аккаунт и открытие главного активити
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    {
                        signIn(email.getText().toString(), password.getText().toString());
                    }
                    else
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},1);
                    }
                }
                else {
                    signIn(email.getText().toString(), password.getText().toString());
                }
            }
        });
        //Открытие активити регистрации
        registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Authoriz.this,regisration.class).addFlags(Intent.FLAG_RECEIVER_NO_ABORT));
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.auth_mail);
        password = (EditText) findViewById(R.id.auth_password);
    }
    EditText email;
    EditText password;
    private FirebaseAuth mAuth;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        toast.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        toast.cancel();
    }

    public void ShowMess(String Head, String message)
    {
        LayoutInflater inflater = getLayoutInflater();
        View lay = inflater.inflate(R.layout.activity_notif_message, (ViewGroup) findViewById(R.id.notif_message_root));
        TextView Message_type = lay.findViewById(R.id.notif_message_lable);
        TextView Message_text = lay.findViewById(R.id.notif_message_text);
        Message_type.setText(Head);
        Message_text.setText(message);
        toast.setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(lay);
        toast.show();
    }
}