package com.example.oa_te.senserapp.Staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oa_te.senserapp.Admin.AdminMain;
import com.example.oa_te.senserapp.Admin.PatientActivity;
import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.authentication.LoginActivity;
import com.example.oa_te.senserapp.models.UserAccounts;
import com.example.oa_te.senserapp.users.UserMainActivity;
import com.example.oa_te.senserapp.users.UserProfile;
import com.example.oa_te.senserapp.users.UserRealtime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StaffMain extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private Button staffPatientbtn,staffLogout;
    private ImageView staffprofile;
    private TextView staffEmail,staffName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        staffPatientbtn = findViewById(R.id.StaffPatientbtn);
        staffLogout = findViewById(R.id.StaffLogout);
        staffEmail = findViewById(R.id.StaffEmail);
        staffName = findViewById(R.id.StaffName);
        staffprofile = findViewById(R.id.staffpro);

        staffEmail.setText(firebaseUser.getEmail());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference  = firebaseDatabase.getReference().child("UserAccounts").child(firebaseAuth.getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserAccounts userAccounts = dataSnapshot.getValue(UserAccounts.class);
                staffName.setText("สวัสดีผู้ดูแล " + userAccounts.getusersname()+"   ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        staffprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(StaffMain.this, StaffProfile.class);
                startActivity(intent1);
            }
        });
        staffPatientbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffMain.this, StaffPatient.class);
                startActivity(intent);
            }
        });

        staffLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.getInstance().signOut();
                Intent intent = new Intent(StaffMain.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


    }
}
