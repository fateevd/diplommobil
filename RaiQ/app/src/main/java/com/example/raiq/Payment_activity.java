package com.example.raiq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Payment_activity extends AppCompatActivity {

    TextView name,address,text;
    Button disslike,like,back,buy;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int countLikes = 0;
    int countDisslikes = 0;

    public void GetLikes(DatabaseReference ref, final int key)
    {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (key == 0)
                {
                    countLikes = snapshot.getValue(Integer.class);
                }
                else if(key == 1)
                {
                    countDisslikes = snapshot.getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument_info);

        disslike = findViewById(R.id.monument_button_disslike);
        like = findViewById(R.id.monument_button_like);
        back = findViewById(R.id.monument_button_back);
        buy = findViewById(R.id.monument_button_pay);
        database = FirebaseDatabase.getInstance();
        name = findViewById(R.id.monument_name);
        address = findViewById(R.id.monument_address);
        text = findViewById(R.id.monument_text);

        final TextView likesView, disslikesView;
        likesView = findViewById(R.id.monument_likes_text);
        disslikesView = findViewById(R.id.monument_disslikes_text);

        try
        {
            final String id = Nav.url.substring(Nav.url.indexOf("id=")+3, Nav.url.indexOf(";cost=")).replace("&gt;",">");
            String s2 = Nav.url.substring(Nav.url.indexOf("name=")+5, Nav.url.indexOf(";adress="));
            String s1 = Nav.url.substring(Nav.url.indexOf("Описание=")+9, Nav.url.indexOf(";image="));
            String linkImg = Nav.url.substring(Nav.url.indexOf("image=")+6, Nav.url.indexOf(";id=")).replace("&lt;/br&gt;","");
            String s3 = Nav.url.substring(Nav.url.indexOf("adress=")+7, Nav.url.indexOf(";Описание="));
            String cost = Nav.url.substring(Nav.url.indexOf("cost=")+7, Nav.url.lastIndexOf(";"));

            if(!cost.equals("0"))
            {
                buy.setVisibility(View.VISIBLE);
                buy.setText("Приобрести за "+cost+" руб.");
                address.setVisibility(View.GONE);
            }

            name.setText(s2);
            address.setText(s3);
            text.setText(s1);

            ImageSlider imageSlider = findViewById(R.id.monument_images);
            List<SlideModel> slideModels = new ArrayList<>();
            slideModels.add(new SlideModel(linkImg));
            imageSlider.setImageList(slideModels,true);


            final String email = Nav.email.replaceAll("[^A-Za-zА-Яа-я0-9]", "");

            myRef = database.getReference("qrCodes/"+id+"/dislikes");
            GetLikes(myRef,1);
            myRef = database.getReference("qrCodes/"+id+"/likes");
            GetLikes(myRef,0);

            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    opDil();
                }
            });


            disslike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (disslike.getBackground().getConstantState() == Payment_activity.this.getResources().getDrawable(R.drawable.button_disslike_active).getConstantState())
                    {
                        disslike.setBackground(getResources().getDrawable(R.drawable.button_disslike_dissactive));
                        like.setBackground(getResources().getDrawable(R.drawable.button_like_dissactive));
                        myRef = database.getReference("qrCodes/"+id+"/dislikes");
                        GetLikes(myRef,1);
                        myRef.setValue(countDisslikes-1);
                        myRef = database.getReference("Monuments/"+email+"/"+id+"/status");
                        myRef.setValue("none");
                        disslikesView.setText("\uD83D\uDC4E "+(countDisslikes-1));
                    }
                    else if(like.getBackground().getConstantState() == Payment_activity.this.getResources().getDrawable(R.drawable.button_like_active).getConstantState())
                    {
                        disslike.setBackground(getResources().getDrawable(R.drawable.button_disslike_active));
                        like.setBackground(getResources().getDrawable(R.drawable.button_like_dissactive));
                        myRef = database.getReference("qrCodes/"+id+"/likes");
                        GetLikes(myRef,0);
                        myRef.setValue(countLikes-1);
                        myRef = database.getReference("qrCodes/"+id+"/dislikes");
                        GetLikes(myRef,1);
                        myRef.setValue(countDisslikes+1);
                        myRef = database.getReference("Monuments/"+email+"/"+id+"/status");
                        myRef.setValue("false");
                        likesView.setText("❤ "+(countLikes-1));
                        disslikesView.setText("\uD83D\uDC4E "+(countDisslikes-1));
                    }
                    else
                    {
                        disslike.setBackground(getResources().getDrawable(R.drawable.button_disslike_active));
                        like.setBackground(getResources().getDrawable(R.drawable.button_like_dissactive));
                        myRef = database.getReference("qrCodes/"+id+"/dislikes");
                        GetLikes(myRef,1);
                        myRef.setValue(countDisslikes+1);
                        myRef = database.getReference("Monuments/"+email+"/"+id+"/status");
                        myRef.setValue("false");
                        disslikesView.setText("\uD83D\uDC4E "+(countDisslikes+1));
                    }
                }
            });

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (like.getBackground().getConstantState() == Payment_activity.this.getResources().getDrawable(R.drawable.button_like_active).getConstantState())
                    {
                        like.setBackground(getResources().getDrawable(R.drawable.button_like_dissactive));
                        disslike.setBackground(getResources().getDrawable(R.drawable.button_disslike_dissactive));
                        myRef = database.getReference("qrCodes/"+id+"/likes");
                        GetLikes(myRef,0);
                        myRef.setValue(countLikes-1);
                        myRef = database.getReference("Monuments/"+email+"/"+id+"/status");
                        myRef.setValue("none");
                        likesView.setText("❤ "+(countLikes-1));
                    }
                    else if(disslike.getBackground().getConstantState() == Payment_activity.this.getResources().getDrawable(R.drawable.button_disslike_active).getConstantState())
                    {
                        disslike.setBackground(getResources().getDrawable(R.drawable.button_disslike_dissactive));
                        like.setBackground(getResources().getDrawable(R.drawable.button_like_active));
                        myRef = database.getReference("qrCodes/"+id+"/likes");
                        GetLikes(myRef,0);
                        myRef.setValue(countLikes+1);
                        myRef = database.getReference("qrCodes/"+id+"/dislikes");
                        GetLikes(myRef,1);
                        myRef.setValue(countDisslikes-1);
                        myRef = database.getReference("Monuments/"+email+"/"+id+"/status");
                        myRef.setValue("true");
                        likesView.setText("❤ "+(countLikes+1));
                        disslikesView.setText("\uD83D\uDC4E "+(countDisslikes-1));
                    }
                    else
                    {
                        myRef = database.getReference("qrCodes/"+id+"/likes");
                        GetLikes(myRef,0);
                        myRef.setValue(countLikes+1);
                        myRef = database.getReference("Monuments/"+email+"/"+id+"/status");
                        myRef.setValue("true");
                        like.setBackground(getResources().getDrawable(R.drawable.button_like_active));
                        disslike.setBackground(getResources().getDrawable(R.drawable.button_disslike_dissactive));
                        likesView.setText("❤ "+(countLikes+1));
                    }
                }
            });

            likesView.setText("❤ "+countLikes);
            disslikesView.setText("\uD83D\uDC4E "+countDisslikes);

            myRef =  FirebaseDatabase.getInstance().getReference("Monuments/"+email+"/"+id+"/status");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null)
                    {
                        if(dataSnapshot.getValue(String.class).equals("none"))
                        {
                            like.setBackground(getResources().getDrawable(R.drawable.button_like_dissactive));
                            disslike.setBackground(getResources().getDrawable(R.drawable.button_disslike_dissactive));
                        }
                        else if(dataSnapshot.getValue(String.class).equals("true"))
                        {
                            like.setBackground(getResources().getDrawable(R.drawable.button_like_active));
                            disslike.setBackground(getResources().getDrawable(R.drawable.button_disslike_dissactive));
                        }
                        else if(dataSnapshot.getValue(String.class).equals("false"))
                        {
                            like.setBackground(getResources().getDrawable(R.drawable.button_like_dissactive));
                            disslike.setBackground(getResources().getDrawable(R.drawable.button_disslike_active));
                        }
                    }
                    else
                    {
                        like.setBackground(getResources().getDrawable(R.drawable.button_like_dissactive));
                        disslike.setBackground(getResources().getDrawable(R.drawable.button_disslike_dissactive));
                        myRef.setValue("none");
                    }

                }
                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
        catch (Exception e)
        {
            //setTitle(e.getMessage());
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment_activity.this,MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        //Загрузка изображений в слайдер
        /*
        ImageSlider imageSlider = findViewById(R.id.payment_info_imageSlider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("",""));
        imageSlider.setImageList(slideModels,true);
        */


         /*database = FirebaseDatabase.getInstance();
         myRef = database.getReference("qrCodes/"+id+"/desc");
         myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                tv1.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError error) { }});*/

    }
    public void pay(View view) {
        Toast.makeText(this, "Оплата прошла успешно", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void opDil() {
        try {
            final Dialog dialog = new Dialog(Payment_activity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.message_activity);

            final TextView text = dialog.findViewById(R.id.message_text);
            final ImageView image = dialog.findViewById(R.id.message_image);

            Random rand = new Random();
            rand.nextInt(2);
            if(rand.nextInt(2) == 1)
            {
                text.setText("Оплата прошла успешно");
                image.setImageResource(R.drawable.true_pay);
            }
            else
            {
                text.setText("Оплата не прошла успешно");
                image.setImageResource(R.drawable.false_pay);
            }
            dialog.show();
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}