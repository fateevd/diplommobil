package com.example.raiq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class regisration extends AppCompatActivity {

    Button createacc;
    TextView backtoauth;
    EditText fam, im, otch, poch, parol;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisration);
        createacc = findViewById(R.id.regist_sozdat_akk);
        backtoauth = findViewById(R.id.regist_auth);
        backtoauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(regisration.this,Authoriz.class).addFlags(Intent.FLAG_RECEIVER_NO_ABORT));
                finish();
            }
        });

        fam = findViewById(R.id.regist_fam);
        im = findViewById(R.id.regist_im);
        otch = findViewById(R.id.regist_otch);
        poch = (EditText) findViewById(R.id.regist_poch);
        parol = (EditText) findViewById(R.id.regist_parol);

        mAuth = FirebaseAuth.getInstance();

    }
    public FirebaseAuth mAuth;
    public void reg(String poch, String parol) {
        mAuth.createUserWithEmailAndPassword(poch, parol)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            /*Toast.makeText(Registration.this, "Учётная запись создана успешно.",
                                    Toast.LENGTH_SHORT).show();*/
                            /*Intent intent = new Intent(Registration.this, Authorization.class);
                            startActivity(intent);*/
                        } else {
                            //ShowMess("Слабый пароль","Введите от 1 до 6 символов");
                        }
                    }
                });
    }
    public void createAcc(View view) {
        try {
            reg(poch.getText().toString(), parol.getText().toString());
            String text1 = poch.getText().toString();
            String text2 = text1.replaceAll("[^A-Za-zА-Яа-я0-9]", "");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("userinfo/"+text2);
            //myRef.setValue(text2);
            myRef = database.getReference("userinfo/" + text2 + "/Account");
            myRef.setValue("0");
            myRef = database.getReference("userinfo/" + text2 + "/Email");
            myRef.setValue(poch.getText().toString());
            myRef = database.getReference("userinfo/" + text2 + "/FirstName");
            myRef.setValue(im.getText().toString());
            myRef = database.getReference("userinfo/" + text2 + "/LastName");
            myRef.setValue(fam.getText().toString());
            myRef = database.getReference("userinfo/" + text2 + "/MiddleName");
            myRef.setValue(otch.getText().toString());
            myRef = database.getReference("userinfo/" + text2 + "/Password");
            myRef.setValue(parol.getText().toString());
            Intent intent = new Intent(regisration.this, Authoriz.class);
            startActivity(intent);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
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