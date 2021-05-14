package com.example.raiq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link lk#newInstance} factory method to
 * create an instance of this fragment.
 */
public class lk extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public lk() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment lk.
     */
    // TODO: Rename and change types and number of parameters
    TextView fio, email;
    public static lk newInstance(String param1, String param2) {
        lk fragment = new lk();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Switch theme, notif;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setTranslationZ(getView(), 100f);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    String text2;
    String pas;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lk, container, false);
        final ImageButton show_hide_pass = view.findViewById(R.id.settings_password_change_see);
        final ImageButton pass_okey = view.findViewById(R.id.settings_password_change_okey);
        final EditText password = view.findViewById(R.id.settings_password);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final SharedPreferences sett = getContext().getApplicationContext().getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = sett.edit();

        final Button exit = view.findViewById(R.id.settings_exit_acc);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Authoriz.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });

        fio=view.findViewById(R.id.settings_FIO);
        email=view.findViewById(R.id.settings_mail);
        String text1 = Nav.email;
        text2 = text1.replaceAll("[^A-Za-zА-Яа-я0-9]", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef =  database.getReference("userinfo/"+text2+"/FirstName");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fio.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        myRef = database.getReference("userinfo/"+text2+"/Password");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pas=dataSnapshot.getValue(String.class);
                password.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        pass_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("userinfo/"+text2+"/Password");

                myRef.setValue(password.getText().toString());
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(Nav.email, pas);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            } else {
                                            }
                                        }
                                    });
                                } else {
                                }
                            }
                        });
            }
        });

        theme = view.findViewById(R.id.settings_theme);
        notif = view.findViewById(R.id.settings_notif);

        if (sett.getBoolean("Notif",false) == true)
        {
            notif.setChecked(true);
        }
        else{
            notif.setChecked(false);
        }

        if (sett.getBoolean("DayThem",false) == true)
        {
            theme.setChecked(true);
        }
        else{
            theme.setChecked(false);
        }
        show_hide_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_hide_pass.getDrawable().getConstantState() == lk.this
                        .getResources().getDrawable(R.drawable.see_pass)
                        .getConstantState())
                {
                    show_hide_pass.setImageDrawable(getResources().getDrawable(R.drawable.pass_show));
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.length());
                }
                else if (show_hide_pass.getDrawable().getConstantState() == lk.this
                        .getResources().getDrawable(R.drawable.pass_show)
                        .getConstantState())
                {
                    show_hide_pass.setImageDrawable(getResources().getDrawable(R.drawable.see_pass));
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.length());
                }
            }
        });
        theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true)
                {
                    editor.putBoolean("DayThem",true);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else
                {
                    editor.putBoolean("DayThem",false);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true)
                {
                    editor.putBoolean("Notif",true);
                    editor.apply();
                }
                else
                {
                    editor.putBoolean("Notif",false);
                    editor.apply();
                }
            }
        });
        return view;
    }
}