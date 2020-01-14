package com.example.oa_te.senserapp.users;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.authentication.LoginActivity;
import com.example.oa_te.senserapp.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class UserMainActivity extends AppCompatActivity implements View.OnClickListener {


        private static final Pattern NUMBER_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[1-99])" +         //at least 1 digit
                        "(!(?=.*[a-zA-Z]))" +      //any letter
                        "$");

    private ImageView profile,realtime,contact,password;
    private TextView userEmail,userName;
    private Button userLogout;

    private Toolbar toolbarName;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String getsensoruid;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "การเข้าสู่ระบบล้มเหลว กรุณาลองใหม่อีกครั้ง ", Toast.LENGTH_LONG).show();
        }


        FirebaseUser User = firebaseAuth.getCurrentUser();
        profile = findViewById(R.id.usProfile);
        realtime = findViewById(R.id.usRealtime);
        password = findViewById(R.id.usChange);
        contact = findViewById(R.id.usContract);

        profile.setOnClickListener(this);
        realtime.setOnClickListener(this);
        contact.setOnClickListener(this);
        password.setOnClickListener(this);

        userEmail = findViewById(R.id.textEmail);
        userName = findViewById(R.id.textNameUser);
        userLogout = findViewById(R.id.btnLogout);
        toolbarName = findViewById(R.id.toolbarUser);

        userEmail.setText(User.getEmail());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference  = firebaseDatabase.getReference().child("Patients").child(firebaseAuth.getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                userName.setText("สวัสดี " + patient.getPatientName());
                getsensoruid = (patient.getIdsensor());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        realtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x = "";
                if (getsensoruid.equals(x)) {
                    ShowNullSensor();
                } else {
                    Intent goRealtime = new Intent(UserMainActivity.this, UserRealtime.class);
                    startActivity(goRealtime);
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, UserProfile.class);
            startActivity(intent);
        }
        });
        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserMainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.usProfile:
                Intent goProfile = new Intent(this,UserProfile.class);
                startActivity(goProfile);
                break;
            case R.id.usChange:
                ShowChangeDialog();
                break;
                case R.id.usContract:
                Intent goContact = new Intent(this,UserContact.class);
                startActivity(goContact);
                break;
        }
    }


    public void ShowChangeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.change_pass, null);
        dialogBuilder.setView(dialogView);

        final Button ConfirmDialog = dialogView.findViewById(R.id.cf_dialog);
        final Button CancelDialog = dialogView.findViewById(R.id.cc_dialog);
        final EditText Change = dialogView.findViewById(R.id.etChangePass);

        final AlertDialog c = dialogBuilder.create();
        c.show();
        ConfirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 final String password = Change.getText().toString().trim();
                    if (password.isEmpty()) {
                        Change.setError("กรุณากรอกรหัสผ่าน");
                        view.requestFocus();
                        return;
                    }else if (NUMBER_PATTERN.matcher(password).matches()) {
                        Change.setError("กรุณากรอกรหัสผ่านให้ถูกต้อง");
                        view.requestFocus();
                        return;
                    } else if (password.length()< 6 || password.length() > 13) {
                        Change.setError("รหัสผ่านของคุณต้องไม่น้อยกว่า 6 ตัวและไม่มากกว่า 13 ตัว");
                        view.requestFocus();
                        Toast.makeText(UserMainActivity.this, "การใส่รหัสผ่านผิดพลาด", Toast.LENGTH_SHORT).show();
                        return ;
                }  else {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    firebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserMainActivity.this, "ท่านได้เปลี่ยนรหัสผ่านสำเร็จ", Toast.LENGTH_SHORT).show();
                                c.dismiss();

                            }
                        }
                    });
                }
            }
        });

        CancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.dismiss();
            }
        });
    }


    private void ShowNullSensor() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.realtime_null, null);
        dialogBuilder.setView(dialogView);

        final Button ConfirmDialog = dialogView.findViewById(R.id.cf_dialog);
        final AlertDialog c = dialogBuilder.create();
        c.show();
        ConfirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.dismiss();
                return;
            }
        });
    }
}
