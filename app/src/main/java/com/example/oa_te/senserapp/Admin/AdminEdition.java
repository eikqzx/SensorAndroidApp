package com.example.oa_te.senserapp.Admin;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.List.SensorList;
import com.example.oa_te.senserapp.models.Sensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminEdition extends AppCompatActivity {
    //we will use these constants later to pass the artist name and sensorId to another activity
    public static final String SENSOR_ROOM = "com.example.it_08.senserapp.room";
    public static final String SENSOR_ID = "com.example.it_08.senserapp.senserid";
    public static final String SENSOR_NAME = "com.example.it_08.senserapp.name";
    //view objects
    EditText editTextSensor , editTextPulse, editTextTemp;
    Spinner spinnerRoom;
    Button buttonAddSensor;
    ListView listViewSensor;

    List<Sensor> sensors;
    DatabaseReference databaseSensor;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseSensor = FirebaseDatabase.getInstance().getReference("sensors");

        editTextSensor = findViewById(R.id.editTextSenser);
        spinnerRoom = findViewById(R.id.spinnerRoom);

        listViewSensor = findViewById(R.id.listViewSenser);


        buttonAddSensor = findViewById(R.id.buttonAddSenser);
        editTextPulse = findViewById(R.id.editTextPulse);
        editTextTemp = findViewById(R.id.editTextTemp);


        sensors = new ArrayList<>();
        buttonAddSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSensor();

                listViewSensor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Sensor sensor = sensors.get(i);
                        String  d = sensor.getId();
                        showUpdateDeleteDialog(sensor.getId(), sensor.getName(),sensor.getPulse(),sensor.getTemp());

                        return true;
                    }
                });

            }
        });


        listViewSensor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Sensor sensor = sensors.get(i);

                Intent intent = new Intent(getApplicationContext(), PatientActivity.class);


                intent.putExtra(SENSOR_ID, sensor.getId());
                intent.putExtra(SENSOR_ROOM, sensor.getRoom());
                intent.putExtra(SENSOR_NAME, sensor.getName());

                startActivity(intent);
            }
        });


        listViewSensor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sensor sensor = sensors.get(i);
                showUpdateDeleteDialog(sensor.getId(), sensor.getName(),sensor.getPulse(),sensor.getTemp());
                return true;
            }
        });
    }


    /*     This method is saving a new artist to the Firebase Realtime Database     * */
    private void addSensor() {
        //getting the values to save
        String name = editTextSensor.getText().toString().trim();
        String room = spinnerRoom.getSelectedItem().toString();
        String pulse = editTextPulse.getText().toString().trim();
        String temp = editTextTemp.getText().toString().trim();
        String status = "พร้อมใช้งาน";
        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {
            id = databaseSensor.push().getKey();

            Sensor sensor = new Sensor(id ,name ,room ,pulse,temp,status);

            //Saving the Artist
            databaseSensor.child(id).setValue(sensor);

            //setting edit text to blank again
            editTextSensor.setText("");

            //displaying a success toast
            Toast.makeText(this, "Sensor added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a Sensor", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseSensor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                sensors.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Sensor sensor = postSnapshot.getValue(Sensor.class);
                    sensors.add(sensor);
                }

                SensorList sensorAdapter = new SensorList(AdminEdition.this, sensors);

                //attaching adapter to the listView
                listViewSensor.setAdapter(sensorAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private boolean update(String id, String device, String room, String pulse, String temp, String status) {

        String name = device;
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("sensors").child(this.id);

        //updating artist
       Sensor sensor = new Sensor(id, name , room ,pulse , temp,status);
        dR.setValue(sensor);
        Toast.makeText(getApplicationContext(), "Updated Complete", Toast.LENGTH_LONG).show();
        return true;

    }
    private void showUpdateDeleteDialog(final String id,final String name,final String pulse, String temp) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_sensor, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextSensor =  dialogView.findViewById(R.id.etNameSensor);
        final Spinner spinnerRoom =  dialogView.findViewById(R.id.spinnerRoom);
        final EditText editTextPulse =  dialogView.findViewById(R.id.etPulse);
        final EditText editTextTemp =  dialogView.findViewById(R.id.etTemp);
        final Button buttonUpdate =  dialogView.findViewById(R.id.buttonConfirmSensor);
        final Button buttonCancel =  dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle(name);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String device = editTextSensor.getText().toString().trim();
                Toast.makeText(getApplicationContext(), device, Toast.LENGTH_LONG).show();
                String pulse = editTextPulse.getText().toString().trim();
                Toast.makeText(getApplicationContext(), device, Toast.LENGTH_LONG).show();
                String temp = editTextTemp.getText().toString().trim();
                Toast.makeText(getApplicationContext(), device, Toast.LENGTH_LONG).show();
                String room = spinnerRoom.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), room, Toast.LENGTH_LONG).show();
                String status = "พร้อมใช้งาน";
                if (!TextUtils.isEmpty(device)) {
                    update(id , device,room,pulse,temp,status);
                    b.dismiss();
                }
            }
        });
    }

    private boolean deleteSensor(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("sensors").child(id);
        dR.removeValue();
        DatabaseReference drPT = FirebaseDatabase.getInstance().getReference("Patients").child(id);
        drPT.removeValue();
        Toast.makeText(getApplicationContext(), "Sensor Deleted", Toast.LENGTH_LONG).show();

        return true;
    }
}
