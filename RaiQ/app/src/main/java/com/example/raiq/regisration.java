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

import java.util.HashMap;

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
                startActivity(new Intent(regisration.this,Authoriz.class).addFlags(new Intent().FLAG_ACTIVITY_CLEAR_TASK | new Intent().FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });

        fam = findViewById(R.id.regist_fam);
        im = findViewById(R.id.regist_im);
        poch = (EditText) findViewById(R.id.regist_poch);
        parol = (EditText) findViewById(R.id.regist_parol);

        mAuth = FirebaseAuth.getInstance();

    }
    public FirebaseAuth mAuth;
    String id;
    public String reg(String poch, String parol) {
        mAuth.createUserWithEmailAndPassword(poch, parol)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            id = user.getUid();
                            /*Toast.makeText(Registration.this, "Учётная запись создана успешно.",
                                    Toast.LENGTH_SHORT).show();*/
                            /*Intent intent = new Intent(Registration.this, Authorization.class);
                            startActivity(intent);*/
                        } else {
                            id = "";
                            //ShowMess("Слабый пароль","Введите от 1 до 6 символов");
                        }
                    }
                });
        return id;
    }
    public void createAcc(View view) {
        try {
            String id = reg(poch.getText().toString(), parol.getText().toString());
            if(!id.equals("")){
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("user/"+id);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Email",poch.getText().toString());
                hashMap.put("Famyli",fam.getText().toString());
                hashMap.put("Name",im.getText().toString());
                myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(regisration.this, Authoriz.class).addFlags(new Intent().FLAG_ACTIVITY_CLEAR_TASK | new Intent().FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }
                    }
                });
            }
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