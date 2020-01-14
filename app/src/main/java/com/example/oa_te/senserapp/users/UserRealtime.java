package com.example.oa_te.senserapp.users;

import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.Staff.StaffMain;
import com.example.oa_te.senserapp.models.Patient;
import com.example.oa_te.senserapp.models.Sensor;
import com.example.oa_te.senserapp.models.UserAccounts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;


public class UserRealtime extends AppCompatActivity {
    private WebView webViewPulse, webViewTemp;
    private String getPulseUrl,getTempUrl,getidsensor,getname,getsensor,user;
    private Button btnback,refresh;
    TextView Showname,ShowDeivice;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph);
        firebaseAuth = FirebaseAuth.getInstance();
        ShowDeivice = findViewById(R.id.ShowDeviceRT);
        firebaseDatabase= FirebaseDatabase.getInstance();
        refresh = findViewById(R.id.RefreshBtn);
        firebaseDatabase = FirebaseDatabase.getInstance();

       DatabaseReference databaseReferencePatient = firebaseDatabase.getReference().child("Patients").child(firebaseAuth.getCurrentUser().getUid());
        databaseReferencePatient.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                getidsensor = (patient.getIdsensor());
                String checknull = "";
                if (getidsensor.equals(checknull)) {
                    ifnull();
                }else {
                    getSensorID(getidsensor);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserRealtime.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        btnback = findViewById(R.id.back);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(UserRealtime.this, UserMainActivity.class));
                    finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webViewPulse.loadUrl(getPulseUrl);
                webViewTemp.loadUrl(getTempUrl);
            }
        });

    }

    public void CallWebView() {
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                String webnull = "";
                if(webViewPulse.equals(webnull)){
                    ifnull();
            }else {
                    webViewPulse.loadUrl(getPulseUrl);
                    webViewTemp.loadUrl(getTempUrl);
                    ha.postDelayed(this, 15000);
                }
            }
        }, 15000);
    }

    private void ifnull(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.managerealtime_null, null);
        final TextView content = dialogView.findViewById(R.id.content_realnull);
        final Button close  = dialogView.findViewById(R.id.close_realnull);
        dialogBuilder.setView(dialogView);
        final AlertDialog c = dialogBuilder.create();
        c.show();
        content.setText("ไม่สามารถดูข้อมูล Realtime ได้เนื่องจากข้อมูลมีการเปลี่ยนแปลง หากยังไม่สามารถเข้าดูข้อมูล Realtime เกิน 5 นาทีกรุณาติดต่อแอดมิน");
        content.setTextSize(17);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.dismiss();
                finish();
            }
        });
    }

    private void getSensorID(final String getidsensor) {
        DatabaseReference databaseReferenceSensor = firebaseDatabase.getReference().child("sensors").child(getidsensor);
        databaseReferenceSensor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Sensor sensor = dataSnapshot.getValue(Sensor.class);
                getPulseUrl = (sensor.getPulse());
                getTempUrl = (sensor.getTemp());
                getsensor = (sensor.getName());

                ShowDeivice.setText(getsensor);
                webViewTemp = findViewById(R.id.WvTemp);
                webViewPulse = findViewById(R.id.WvPulse);

                webViewPulse.setWebViewClient(new WebViewClient());
                webViewPulse.loadUrl(getPulseUrl);

                WebSettings web1 = webViewPulse.getSettings();
                web1.setJavaScriptEnabled(true);
                web1.setSupportZoom(true);
                web1.setBuiltInZoomControls(true);
                web1.setUseWideViewPort(true);

                webViewTemp.setWebViewClient(new WebViewClient());
                webViewTemp.loadUrl(getTempUrl);
                WebSettings web2 = webViewTemp.getSettings();
                web2.setJavaScriptEnabled(true);
                web2.setUseWideViewPort(true);
                web2.setBuiltInZoomControls(true);
                web2.setSupportZoom(true);

                CallWebView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserRealtime.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (webViewPulse.canGoBack()) {
            webViewPulse.goBack();
        } else if(webViewTemp.canGoBack()){
            webViewTemp.goBack();

        } else {
            super.onBackPressed();
        }
    }

}
