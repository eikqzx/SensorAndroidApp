package com.example.oa_te.senserapp.Admin;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.List.SensorList;
import com.example.oa_te.senserapp.models.Sensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SensorActivity extends AppCompatActivity {
    private List<Sensor> sensors;

    private Button close,addsensor;
    DatabaseReference databaseSensor;
    private String id;
    ListView listViewSensor;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sensor_manage);

    databaseSensor = FirebaseDatabase.getInstance().getReference("sensors");
    sensors = new ArrayList<>();
    addsensor = findViewById(R.id.SensorAdd);
    listViewSensor = findViewById(R.id.sensor_list);
    close = findViewById(R.id.close_sensormanage);

    close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();

        }
    });

    addsensor.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addSensor();

        }
    });


    listViewSensor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Sensor sensor = sensors.get(i);
            showUpdateDialog(sensor.getId(),sensor.getName(),sensor.getPulse(),sensor.getTemp(),sensor.getStatus(),sensor.getRoom());
        }
    });


    listViewSensor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            Sensor sensor = sensors.get(i);
            showDeleteDialog(sensor.getId(),sensor.getName());
            return true;
        }
    });
}


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseSensor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sensors.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Sensor sensor = postSnapshot.getValue(Sensor.class);
                    sensors.add(sensor);
                }

                SensorList sensorAdapter = new SensorList(SensorActivity.this, sensors);
                listViewSensor.setAdapter(sensorAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void update(String id, String name, String room, String pulse, String temp, String status) {
        String checkstatus = "ไม่พร้อมใช้งาน";
        if(status.equals(checkstatus)){
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("sensors").child(id);
            Sensor sensor = new Sensor(id, name , room , pulse , temp,status);
            dR.setValue(sensor);
            removeusersensor(id);

            Toast.makeText(getApplicationContext(), "อัพเดทข้อมูลสำเร็จ, สถานะ :"+status, Toast.LENGTH_LONG).show();


        }else{

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("sensors").child(id);
        Sensor sensor = new Sensor(id, name , room , pulse , temp,status);
        dR.setValue(sensor);
        Toast.makeText(getApplicationContext(), "อัพเดทข้อมูลสำเร็จ", Toast.LENGTH_LONG).show();

        }
    }

    private void removeusersensor(String id) {
        DatabaseReference dRpt = FirebaseDatabase.getInstance().getReference();
        Query query = dRpt.child("Patients").orderByChild("idsensor").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    String uid = data.getKey();
                    FirebaseDatabase.getInstance().getReference().child("Patients").child(uid).child("idsensor").setValue("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showDeleteDialog(final String id, String name){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.senser_delete, null);
        dialogBuilder.setView(dialogView);

        final TextView nname =  dialogView.findViewById(R.id.content_dialog);
        final Button Delete =  dialogView.findViewById(R.id.cc_dialog);
        final Button Cancel =  dialogView.findViewById(R.id.cancel_dialog);

       nname.setText("ต้องการลบ : "+ name);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSensor(id);
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

   private void addSensor() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_sensor, null);
        dialogBuilder.setView(dialogView);

        final TextView room = dialogView.findViewById(R.id.showroom);
        final EditText editTextSensor =  dialogView.findViewById(R.id.etNameSensor);
        final Spinner spinnerRoom =  dialogView.findViewById(R.id.spinnerRoom);
        final EditText editTextPulse =  dialogView.findViewById(R.id.etPulse);
        final EditText editTextTemp =  dialogView.findViewById(R.id.etTemp);
        final Button buttonAdd =  dialogView.findViewById(R.id.buttonConfirmSensor);
        final Button buttonCancel =  dialogView.findViewById(R.id.buttonCancel);
        final TextView x = dialogView.findViewById(R.id.fx);
        final TextView xx = dialogView.findViewById(R.id.showstaus);
        final Switch xxx = dialogView.findViewById(R.id.switchstatus);
        x.setVisibility(View.INVISIBLE);
       xx.setVisibility(View.INVISIBLE);
       room.setVisibility(View.INVISIBLE);
       xxx.setVisibility(View.INVISIBLE);



        final AlertDialog b = dialogBuilder.create();

        b.show();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextSensor.getText().toString().trim();
                String room = spinnerRoom.getSelectedItem().toString();
                String pulse = editTextPulse.getText().toString().trim();
                String temp = editTextTemp.getText().toString().trim();
                String Readys = "พร้อมใช้งาน";
                String status = Readys;
                if (!TextUtils.isEmpty(name)) {

                    id = databaseSensor.push().getKey();
                    Sensor sensor = new Sensor(id,name,room,pulse,temp,status);
                    databaseSensor.child(id).setValue(sensor);
                    editTextSensor.setText("");
                    Toast.makeText(getApplicationContext(), "การเพิ่ม Sensor สำเร็จ", Toast.LENGTH_LONG).show();
                    b.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "การเพิ่ม Sensor ล้มเหลว", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
   }


    private void showUpdateDialog(final String id, String name, String pulse, String temp, String status, String room) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_sensor, null);
        dialogBuilder.setView(dialogView);

        final TextView Showname = dialogView.findViewById(R.id.showname_dialog);
        final EditText editTextSensor =  dialogView.findViewById(R.id.etNameSensor);
        final Spinner spinnerRoom =  dialogView.findViewById(R.id.spinnerRoom);
        final EditText editTextPulse =  dialogView.findViewById(R.id.etPulse);
        final TextView Showroom = dialogView.findViewById(R.id.showroom);
        final EditText editTextTemp =  dialogView.findViewById(R.id.etTemp);
        final Switch Status = dialogView.findViewById(R.id.switchstatus);
        final TextView showStatus = dialogView.findViewById(R.id.showstaus);
        final Button buttonUpdate =  dialogView.findViewById(R.id.buttonConfirmSensor);
        final Button buttonCancel =  dialogView.findViewById(R.id.buttonCancel);


        Showroom.setText(room);
        Showname.setText(" แก้ไขข้อมูลของ "+name);
        editTextSensor.setText(name);
        editTextPulse.setText(pulse);
        editTextTemp.setText(temp);
        String stch = "พร้อมใช้งาน";
        if(status.equals(stch)) {
            Status.setChecked(true);
        }else{
            Status.setChecked(false);
        }

        if(Status.isChecked()){
            showStatus.setText("สถานะเดิม : พร้อมใช้งาน");
        }else{
            showStatus.setText("สถานะเดิม : ไม่พร้อมใช้งาน");
            showStatus.setTextColor(Color.RED);
        }


        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Status.isChecked()){
                    showStatus.setText("พร้อมใช้งาน");
                }else{
                    showStatus.setText("ไม่พร้อมใช้งาน");
                }
                String name = editTextSensor.getText().toString().trim();
                String room = spinnerRoom.getSelectedItem().toString();
                String status = showStatus.getText().toString().trim();
                String pulse = editTextPulse.getText().toString().trim();
                String temp = editTextTemp.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    update(id, name,room,pulse,temp,status);
                    b.dismiss();
                }          }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

    }

    private void deleteSensor(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("sensors").child(id);
        dR.removeValue();
        removeusersensor(id);

    }
}

/*    private void deleteSensor(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("sensors").child(id);
        dR.removeValue();

        DatabaseReference dRpt = FirebaseDatabase.getInstance().getReference();
        Query query = dRpt.child("Patients").orderByChild("idsensor").equalTo(id);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data:dataSnapshot.getChildren()){
                    String uid = data.getKey();
                    Toast.makeText(SensorActivity.this,uid, Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("Patients").child(uid).child();

                    Toast.makeText(getApplicationContext(), "ลบข้อมูลสำเร็จ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}*/