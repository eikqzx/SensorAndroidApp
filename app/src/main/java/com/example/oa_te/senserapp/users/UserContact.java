package com.example.oa_te.senserapp.users;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oa_te.senserapp.R;

public class UserContact extends AppCompatActivity {

    private Button BtnFb, BtnLine  ,BtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contract);

        BtnFb =  findViewById(R.id.contract_fb);
        BtnLine =  findViewById(R.id.contract_line);
        BtnBack =  findViewById(R.id.contract_back);


        BtnLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri lineIntent = Uri.parse("https://line.me/R/ti/g/ynhEFh6VOo");
                Intent IntentLine = new Intent(Intent.ACTION_VIEW,lineIntent);
                IntentLine.setPackage("jp.never.line.android");
                try {
                    startActivity(IntentLine);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://line.me/R/ti/g/ynhEFh6VOo")));

                }
            }
        });

        BtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri fbIntent = Uri.parse("https://www.facebook.com/krupeung.saowakhon");
                Intent IntentFb = new Intent(Intent.ACTION_VIEW,fbIntent);
                startActivity(IntentFb);
            }
        });
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            finish();
            }
        });


    }


}
