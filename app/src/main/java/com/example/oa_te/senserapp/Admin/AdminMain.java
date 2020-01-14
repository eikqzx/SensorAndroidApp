package com.example.oa_te.senserapp.Admin;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.authentication.LoginActivity;
import com.example.oa_te.senserapp.models.UserAccounts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminMain extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private Button Patientbtn,Sensorbtn,logoutbtn;
    private TextView AdminEmail,AdminName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Patientbtn = findViewById(R.id.AdminPatientbtn);
        Sensorbtn = findViewById(R.id.AdminSensorbtn);
        logoutbtn = findViewById(R.id.AdminLogout);
        AdminEmail = findViewById(R.id.AdminEmail);
        AdminName = findViewById(R.id.AdminName);

        AdminEmail.setText(firebaseUser.getEmail());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference  = firebaseDatabase.getReference().child("UserAccounts").child(firebaseAuth.getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserAccounts userAccounts = dataSnapshot.getValue(UserAccounts.class);
                AdminName.setText("สวัสดีแอดมิน " + userAccounts.getusersname()+"   ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Patientbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMain.this, PatientActivity.class);
                startActivity(intent);
            }
        });


        Sensorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMain.this, SensorActivity.class);
                startActivity(intent);
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminMain.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }


}
