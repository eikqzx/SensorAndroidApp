package com.example.oa_te.senserapp.Staff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.models.Patient;
import com.example.oa_te.senserapp.users.EditProfile;
import com.example.oa_te.senserapp.users.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StaffEditProfile extends AppCompatActivity {
    private Button cancel, save;
    private TextView ShowName;
    private EditText name, age, phone,email;
    private Spinner sex;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    boolean editnamechecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_edit_profile);
        save = findViewById(R.id.SaveStaffUpdate);
        cancel = findViewById(R.id.StaffCancelUpdate);
        firebaseAuth = FirebaseAuth.getInstance();
        ShowName = findViewById(R.id.staffname);
        email = findViewById(R.id.etupdateemail);
        name = findViewById(R.id.etUpdateStaffName);
        sex = findViewById(R.id.updateStaffSpinnerSex);
        age = findViewById(R.id.etUpdateSatffAge);
        phone = findViewById(R.id.etStaffUpdatePhone);
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("Patients").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                ShowName.setText("แก้ไขข้อมูลส่วนตัวของ "+patient.getPatientName());
                email.setText(firebaseUser.getEmail());
                name.setText(patient.getPatientName());
                age.setText(String.valueOf(patient.getAge()));
                phone.setText(patient.getPhone());
                sex.getSelectedItem().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StaffEditProfile.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checking();
                String getname = name.getText().toString();
                int getage = Integer.valueOf(age.getText().toString());
                String getsex = sex.getSelectedItem().toString();
                String getphone = phone.getText().toString();
                if(editnamechecked != true){


                }else {
                    ShowConfirmDialog(getname,getage,getsex,getphone);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffEditProfile.this, StaffProfile.class);
                startActivity(intent);
            }
        });
    }

    private void checking() {
        String getname = name.getText().toString();
        databaseReference.child("UserAccounts").orderByChild("usersname").equalTo(getname).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            name.setError("ชื่อของท่านมีผู้ใช้งานอื่นใช้แล้ว");
                            name.requestFocus();
                            editnamechecked = false;
                            Toast.makeText(StaffEditProfile.this, "ชื่อของท่านมีซ้ำในระบบ, กรุณาเปลี่ยนชื่อใหม่", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            name.setError(null);
                            editnamechecked = true;
                            Toast.makeText(StaffEditProfile.this, "ชื่อของท่านใช้ได้ กรุณากดยืนยันอีกครั้ง", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                }
        );

    }

    public void ShowConfirmDialog(final String getname, final int getage, final String getsex, final String getphone) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_user_confirm, null);
        dialogBuilder.setView(dialogView);
        final Button ConfirmDialog = dialogView.findViewById(R.id.cf_dialog);
        final Button CancelDialog = dialogView.findViewById(R.id.cancel_dia);
        final AlertDialog c = dialogBuilder.create();
        c.show();

        ConfirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth uAuth = FirebaseAuth.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Patients").child(uAuth.getCurrentUser().getUid());
                Map edituser = new HashMap();
                edituser.put("patientName", getname);
                edituser.put("age", getage);
                edituser.put("sex", getsex);
                edituser.put("phone", getphone);

                databaseReference.updateChildren(edituser);
                DatabaseReference databaseReferencex = firebaseDatabase.getReference().child("UserAccounts").child(uAuth.getCurrentUser().getUid());
                Map editname = new HashMap();
                editname.put("usersname", getname);
                databaseReferencex.updateChildren(editname);

                Toast.makeText(StaffEditProfile.this,"อัพเดทข้อมูลสำเร็จ ! ", Toast.LENGTH_SHORT).show();
                c.dismiss();
                finish();
            }

        });

        CancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.dismiss();
            }
        });
    }

}
