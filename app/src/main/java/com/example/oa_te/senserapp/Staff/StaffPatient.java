package com.example.oa_te.senserapp.Staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oa_te.senserapp.Admin.AddSensorPatient;
import com.example.oa_te.senserapp.Admin.AdminRealtimePatient;
import com.example.oa_te.senserapp.Admin.PatientActivity;
import com.example.oa_te.senserapp.Admin.UpdateUser;
import com.example.oa_te.senserapp.List.PatientList;
import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.models.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StaffPatient extends AppCompatActivity {
    public static final String PATIENT_NAME = "com.example.it_08.senserapp.patientname";
    public static final String PATIENT_UID = "com.example.it_08.senserapp.patientname";
    Button Finish;
    ListView listViewPatient;
    DatabaseReference databasePatient,databaseAccounts;
    List<Patient> patients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_patient);
        databaseAccounts = FirebaseDatabase.getInstance().getReference("UserAccounts");
        databasePatient = FirebaseDatabase.getInstance().getReference("Patients");
        listViewPatient =findViewById(R.id.listViewStaffPatient);
        Finish = findViewById(R.id.staff_finish_btn);

        patients = new ArrayList<>();

        listViewPatient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Patient patient = patients.get(i);
                String getnamemanage = (patient.getPatientName());
                DatabaseReference dbmanage = FirebaseDatabase.getInstance().getReference();
                Query query = dbmanage.child("Patients").orderByChild("patientName").equalTo(getnamemanage);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Patient patient = data.getValue(Patient.class);
                            String getpatientuid = data.getKey();
                            String getpatientname = patient.getPatientName();
                            getsensoridmanage(getpatientuid,getpatientname);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        listViewPatient.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Patient patient = patients.get(i);
                Intent intent = new Intent(getApplicationContext(), AddSensorPatient.class);
                intent.putExtra(PATIENT_NAME, patient.getPatientName());
                startActivity(intent);
                return true;
            }
        });

        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getsensoridmanage(String getpatientuid, String getpatientname) {
        manageptdialog(getpatientuid,getpatientname);
    }
    private void manageptdialog(final String getpatientuid, final String getpatientname){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.staff_manage, null);
        final TextView showname = dialogView.findViewById(R.id.staff_content_managept);
        final Button realtime  = dialogView.findViewById(R.id.staff_realtime_managept);
        final Button delete  = dialogView.findViewById(R.id.staff_delete_managept);
        final Button close  = dialogView.findViewById(R.id.staff_close_managept);
        dialogBuilder.setView(dialogView);
        final AlertDialog c = dialogBuilder.create();
        c.show();
        showname.setText("จัดการข้อมูลของ "+getpatientname);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.dismiss();
            }
        });

        realtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminRealtimePatient.class);
                intent.putExtra(PATIENT_NAME, getpatientname);
                startActivity(intent);
                c.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmdeletedialog(getpatientuid,getpatientname);
                c.dismiss();
            }
        });

    }
    private void confirmdeletedialog(final String getpatientuid, final String getpatientname) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.confirm_deletept, null);
        final TextView showname = dialogView.findViewById(R.id.content_confirmpt);
        final Button delete  = dialogView.findViewById(R.id.delete_confirmpt);
        final Button close  = dialogView.findViewById(R.id.close_confirmpt);
        dialogBuilder.setView(dialogView);
        final AlertDialog c = dialogBuilder.create();
        c.show();
        showname.setText("ต้องการลบผู้ใช้ "+getpatientname);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletepatient(getpatientuid,getpatientname);
                c.dismiss();
            }
        });

    }

    private void deletepatient(String getpatientuid, String getpatientname) {
        DatabaseReference deleteacc = databaseAccounts.child(getpatientuid);
        deleteacc.removeValue();
        DatabaseReference deletept = databasePatient.child(getpatientuid);
        deletept.removeValue();
        Toast.makeText(this, "ลบข้อมูลผู้ป่วยของ "+getpatientname+" สำเร็จ", Toast.LENGTH_SHORT).show();
    }
    private  void updatepatient(String usertype){
        DatabaseReference updateacc = databaseAccounts.child(usertype);
        updateacc.setValue("");

    }
    @Override
    protected void onStart() {
        super.onStart();
        databasePatient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patients.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Patient patient = postSnapshot.getValue(Patient.class);
                    patients.add(patient);
                }
                PatientList trackListAdapter = new PatientList(StaffPatient.this, patients);
                listViewPatient.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
