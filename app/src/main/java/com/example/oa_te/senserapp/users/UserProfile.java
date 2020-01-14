package com.example.oa_te.senserapp.users;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.authentication.LoginActivity;
import com.example.oa_te.senserapp.models.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    TextView usEmail, usName, usAge, usPhone, usSex,back,ShowName;
    Button update;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "การเข้าสู่ระบบล้มเหลว กรุณาลองใหม่อีกครั้ง ", Toast.LENGTH_LONG).show();

        }

        update = findViewById(R.id.UpdateBtn);
        firebaseUser = firebaseAuth.getCurrentUser();
        usEmail = findViewById(R.id.tvShowEmail);
        usName = findViewById(R.id.tvShowName);
        usAge = findViewById(R.id.tvShowAge);
        usPhone = findViewById(R.id.tvShowPhone);
        usSex = findViewById(R.id.tvShowSex);
        back = findViewById(R.id.textBack);
        usEmail.setText("ข้อมูลส่วนตัวของ : " + firebaseUser.getEmail());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Patients").child(firebaseAuth.getCurrentUser().getUid());


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                assert patient != null;
                usName.setText(patient.getPatientName());
                String getsensorid = (patient.getIdsensor());
                usAge.setText(String.valueOf(patient.getAge()));
                usPhone.setText(patient.getPhone());
                usSex.setText(patient.getSex());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, UserMainActivity.class);
                startActivity(intent);
            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this,EditProfile.class);
                startActivity(intent);
            }
        });

    }
    


}