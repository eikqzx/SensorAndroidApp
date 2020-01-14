package com.example.oa_te.senserapp.Admin;

import android.content.Intent;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.models.Patient;
import com.example.oa_te.senserapp.models.Sensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminRealtimePatient extends AppCompatActivity {
    private WebView ADwebViewPulse, ADwebViewTemp;
    private String ADgetPulseUrl,ADgetTempUrl,getname;
    String getsensor ="";
    TextView ADshowSensor;
    private Button ADrefresh,back,note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_realtime_pt);
        Intent intent = getIntent();
        final String getname = intent.getStringExtra(PatientActivity.PATIENT_NAME);
        ADrefresh = findViewById(R.id.amRefreshBtn);
        ADshowSensor = findViewById(R.id.amShowDeviceRT);

        back = findViewById(R.id.amback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ADrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ADwebViewPulse.loadUrl(ADgetPulseUrl);
                ADwebViewTemp.loadUrl(ADgetTempUrl);

            }
        });
        DatabaseReference getnameRF = FirebaseDatabase.getInstance().getReference();
        Query query = getnameRF.child("Patients").orderByChild("patientName").equalTo(getname);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Patient patient = data.getValue(Patient.class);
                    String sensoruid = patient.getIdsensor();
                    String nulxx ="";

                    if (sensoruid.equals(nulxx)) {
                        shownulldialog();
                    } else {
                        Admingetsensorid(sensoruid);
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CallWebView() {
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                String webnulll ="";
                if(ADwebViewPulse.equals(webnulll)){
                    shownulldialog();
                }else{
                    ADwebViewPulse.loadUrl(ADgetPulseUrl);
                    ADwebViewTemp.loadUrl(ADgetTempUrl);
                    ha.postDelayed(this, 15000);
                }

            }
        }, 15000);
    }

    private void Admingetsensorid(String sensoruid) {

            DatabaseReference databaseSensor = FirebaseDatabase.getInstance().getReference().child("sensors").child(sensoruid);
            databaseSensor.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Sensor sensor = dataSnapshot.getValue(Sensor.class);
                    ADgetPulseUrl = (sensor.getPulse());
                    ADgetTempUrl = (sensor.getTemp());
                    getsensor = (sensor.getName());

                        ADwebViewTemp = findViewById(R.id.amWvTemp);
                        ADwebViewPulse = findViewById(R.id.amWvPulse);
                         ADshowSensor.setText("อุปกรณ์ที่ติดตั้ง : "+ getsensor);


                        ADwebViewPulse.setWebViewClient(new WebViewClient());
                        ADwebViewPulse.loadUrl(ADgetPulseUrl);

                        WebSettings web1 = ADwebViewPulse.getSettings();
                        web1.setJavaScriptEnabled(true);
                    web1.setSupportZoom(true);
                    web1.setBuiltInZoomControls(true);
                    web1.setUseWideViewPort(true);

                        ADwebViewTemp.setWebViewClient(new WebViewClient());
                        ADwebViewTemp.loadUrl(ADgetTempUrl);
                        WebSettings web2 = ADwebViewTemp.getSettings();
                        web2.setJavaScriptEnabled(true);
                    web2.setUseWideViewPort(true);
                    web2.setBuiltInZoomControls(true);
                    web2.setSupportZoom(true);
                        CallWebView();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void shownulldialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.managerealtime_null, null);
        final Button close  = dialogView.findViewById(R.id.close_realnull);
        dialogBuilder.setView(dialogView);
        final AlertDialog c = dialogBuilder.create();
        c.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.dismiss();
                finish();
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (ADwebViewPulse.canGoBack()) {
            ADwebViewPulse.goBack();
        } else if(ADwebViewTemp.canGoBack()){
            ADwebViewTemp.goBack();

        } else {
            super.onBackPressed();
        }
    }
}
