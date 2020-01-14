package com.example.oa_te.senserapp.authentication;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.oa_te.senserapp.Admin.AdminMain;
import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.Staff.StaffMain;
import com.example.oa_te.senserapp.models.UserAccounts;
import com.example.oa_te.senserapp.users.UserMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Permission extends AppCompatActivity {
private FirebaseAuth firebaseAuth;
private FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("UserAccounts").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserAccounts userAccounts = dataSnapshot.getValue(UserAccounts.class);
                String check = "แอดมิน";
                String check1 = "ผู้ดูแล";
                String status = (userAccounts.getUsertype());
                if (status.equals(check)){
                    startActivity(new Intent(Permission.this, AdminMain.class));
                    finish();
                }else if(status.equals(check1)){
                    startActivity(new Intent(Permission.this, StaffMain.class));
                    finish();
                }else{
                    startActivity(new Intent(Permission.this, UserMainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
