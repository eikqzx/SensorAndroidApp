package com.example.oa_te.senserapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.models.Patient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UpdateUser extends AppCompatActivity {
    public static final String PATIENT_NAME = "com.example.it_08.senserapp.patientname";
    public static final String PATIENT_UID = "com.example.it_08.senserapp.patientname";
    Button cancel,confirm;
    DatabaseReference databasePatient,databaseAccounts;
    List<Patient> patients;
    Spinner spinneruserype;

    private TextView Showname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        databaseAccounts = FirebaseDatabase.getInstance().getReference("UserAccounts");
        databasePatient = FirebaseDatabase.getInstance().getReference("Patients");
        spinneruserype = findViewById(R.id.spinner_usertype);
        confirm = findViewById(R.id.confirmbtn);
        cancel = findViewById(R.id.cancelbtn);
        Showname = findViewById(R.id.testtext);
        patients = new ArrayList<>();
        final String getuid = getIntent().getStringExtra(PatientActivity.PATIENT_UID);
        final String getname = getIntent().getStringExtra(PatientActivity.PATIENT_NAME);
        final String selectedype = spinneruserype.getSelectedItem().toString();
        Showname.setText(getuid);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String selectedtype = spinneruserype.getSelectedItem().toString();
                updatepatient(selectedtype);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });
    }
    private  void updatepatient(String username){
        final String getuid = getIntent().getStringExtra(PatientActivity.PATIENT_UID);
        DatabaseReference updateacc = databaseAccounts.child(getuid).child("usertype");
        DatabaseReference updatepatient = databasePatient.child(getuid).child("userType");
        updateacc.setValue(username);
        updatepatient.setValue(username);
        finish();
    }
}
