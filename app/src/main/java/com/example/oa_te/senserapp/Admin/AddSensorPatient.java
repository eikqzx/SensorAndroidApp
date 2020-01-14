package com.example.oa_te.senserapp.Admin;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.List.SensorList;
import com.example.oa_te.senserapp.models.Patient;
import com.example.oa_te.senserapp.models.Sensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddSensorPatient extends AppCompatActivity {
    private List<Sensor> sensors;
    private TextView showname, sensorstatus;
    private ListView ListviewSensor;
    private Button DeleteSensor, Close;
    String showsensor;
    DatabaseReference databaseSensor, databasePatient;
    private String uid = "";
    private String sensorid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor_patient);
        Intent intent = getIntent();
        final String getname = intent.getStringExtra(PatientActivity.PATIENT_NAME);
        databaseSensor = FirebaseDatabase.getInstance().getReference("sensors");
        databasePatient = FirebaseDatabase.getInstance().getReference("Patients");

        DatabaseReference getnameRF = FirebaseDatabase.getInstance().getReference();
        Query query = getnameRF.child("Patients").orderByChild("patientName").equalTo(getname);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Patient patient = data.getValue(Patient.class);
                    sensorid = patient.getIdsensor();
                    uid = data.getKey();
                    String nulx = "";
                    if (sensorid.equals(nulx)) {
                        sensorstatus.setText("ยังไม่มีอุปกรณ์ติดตั้ง");
                        sensorstatus.setTextColor(Color.RED);
                        DeleteSensor.setClickable(false);
                    } else {
                        showsensor(sensorid);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        showname = findViewById(R.id.ptName);
        ListviewSensor = findViewById(R.id.ptSensor);
        DeleteSensor = findViewById(R.id.SensorAdd);
        Close = findViewById(R.id.ptClose);
        sensors = new ArrayList<>();
        showname.setText("ตั้งค่าอุปกรณ์ Sensor ของ : " + getname);
        sensorstatus = findViewById(R.id.ptCurrent);

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }

        });

        DeleteSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDeleleSensorDialog(uid,getname);
            }
        });


        ListviewSensor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String getusersname = getname;
                Sensor sensor = sensors.get(i);
                String chkstatus = (sensor.getStatus());
                String xx = "ไม่พร้อมใช้งาน";
                if (chkstatus.equals(xx)){
                    readycheck();
                }else{
                    ShowAddSensorDialog(sensor.getId(), getusersname,uid);
                }
            }
        });
    }


    private void ShowDeleleSensorDialog(final String uid, final String getname){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_sensor, null);
        dialogBuilder.setView(dialogView);
        final TextView content = dialogView.findViewById(R.id.delete_content);
        final Button Delete = dialogView.findViewById(R.id.delete_dia);
        final Button Cancel = dialogView.findViewById(R.id.canceldelete_dia);

        content.setText("คุณต้องการถอนการติดตั้งอุปกรณ์ sensor ของ " + getname + " หรือไม่");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteSensorx(uid,getname);
                b.dismiss();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }

    private void readycheck() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.managerealtime_null, null);
        final TextView content  = dialogView.findViewById(R.id.content_realnull);
        final Button close  = dialogView.findViewById(R.id.close_realnull);
        dialogBuilder.setView(dialogView);
        final AlertDialog c = dialogBuilder.create();
        c.show();
        content.setText("ไม่สามารถติดตั้งอุปกรณ์นี้ได้เนื่องจากอยู่ในสถานะไม่พร้อมใช้งาน");
        close.setText("ปิด");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.dismiss();
            }
        });
    }

    private void DeleteSensorx(String uid, String getname) {
        FirebaseDatabase.getInstance().getReference().child("Patients").child(uid).child("idsensor").setValue("");
        Toast.makeText(AddSensorPatient.this,"ถอนการติดตั้งอุปกรณ์ของ "+getname+" สำเร็จ", Toast.LENGTH_SHORT).show();
    }



    private void ShowAddSensorDialog(final String id, final String getusersname, final String uid) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.confirm_addsensor, null);
        dialogBuilder.setView(dialogView);

        final TextView content = dialogView.findViewById(R.id.content_addsensor);
        final Button Add = dialogView.findViewById(R.id.confirm_addsensor);
        final Button Cancel = dialogView.findViewById(R.id.cancel_addsensor);

        content.setText("ต้องการติดตั้ง sensor นี้ให้กับ " + getusersname + " หรือไม่");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSensors(id,getusersname,uid);
                b.dismiss();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }

    private void AddSensors(String id, String getusersname, String uid) {
        FirebaseDatabase.getInstance().getReference().child("Patients").child(uid).child("idsensor").setValue(id);
        Toast.makeText(AddSensorPatient.this, "ติดตั้ง sensor ให้กับ " + getusersname + " สำเร็จ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseSensor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sensors.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Sensor sensor = postSnapshot.getValue(Sensor.class);
                    sensors.add(sensor);
                }
                SensorList sensorAdapter = new SensorList(AddSensorPatient.this, sensors);
                ListviewSensor.setAdapter(sensorAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showsensor(String sensorid) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("sensors").child(sensorid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Sensor sensor = dataSnapshot.getValue(Sensor.class);
                    showsensor = (sensor.getName());
                    sensorstatus.setText("มีอุปกรณ์ติดตั้ง " + showsensor);
                    sensorstatus.setTextColor(Color.GREEN);
                    DeleteSensor.setClickable(true);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
