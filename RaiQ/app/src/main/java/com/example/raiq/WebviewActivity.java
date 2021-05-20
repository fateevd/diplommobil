package com.example.raiq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.raiq.model.qrCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WebviewActivity extends AppCompatActivity {

    WebView webView;
    FirebaseUser currentUser;
    DatabaseReference reference;
    String link;
    String fromwhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument_info);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        link = getIntent().getStringExtra("URL");
        fromwhere = getIntent().getStringExtra("History");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("qr-generation-324f0.web.app")) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });
        webView.loadUrl(!link.isEmpty()?link:"https://www.google.com");
        if(fromwhere.equals("Yes")){
            AddHistory(link);
        }
    }

    public void AddHistory(String address){
        String cutString = "https://qr-generation-324f0.web.app/skaner.html?";
        try{
            if (address.contains(cutString)){
                int index = address.indexOf(cutString);
                String key = address.substring(index+cutString.length());
                reference = FirebaseDatabase.getInstance().getReference("qr/"+key);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            try{
                                qrCode qrCode = snapshot.getValue(qrCode.class);
                                DatabaseReference addhisrory = FirebaseDatabase.getInstance().getReference("ScanHistory/"+currentUser.getUid()).push();
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("URL", link);
                                hashMap.put("Address", qrCode.getAddress());
                                hashMap.put("Name", qrCode.getName());
                                hashMap.put("ScanTime", ServerValue.TIMESTAMP);
                                addhisrory.setValue(hashMap);
                            } catch (Exception ex){
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } catch (Exception ex){
        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}