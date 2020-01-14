package com.example.oa_te.senserapp.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oa_te.senserapp.Admin.AdminMain;
import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.authentication.Permission;
import com.example.oa_te.senserapp.models.Patient;
import com.example.oa_te.senserapp.models.Sensor;
import com.example.oa_te.senserapp.models.UserAccounts;
import com.example.oa_te.senserapp.users.UserMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PatientList extends ArrayAdapter<Patient> {
    private Activity context;
    private List<Patient> patients;
    public List<UserAccounts> userAccounts;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String getsensor,getsex,getnamesensor,getidsensor,getUsertype;
    public PatientList(Activity context, List<Patient> patients) {
        super(context, R.layout.layout_sensor_list, patients);
        this.context = context;
        this.patients = patients;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        View listViewItem = inflater.inflate(R.layout.layout_test_listview, null, true);
        ImageView userSex = listViewItem.findViewById(R.id.user_pic);
        TextView textPatientName = listViewItem.findViewById(R.id.user_name);
        TextView textPatientAge = listViewItem.findViewById(R.id.user_age);
        final TextView textSensorname = listViewItem.findViewById(R.id.user_sensorname);
        TextView textPatientPhone = listViewItem.findViewById(R.id.user_phone);
        final TextView textPatientDevice = listViewItem.findViewById(R.id.user_device);

        Patient patient = patients.get(position);
       // UserAccounts userAccounts = userAccountss.get(position);
        getsensor = (patient.getIdsensor());
        getidsensor = (patient.getIdsensor());
        getsex = (patient.getSex());
        String nul = "";
        TextView textusertype = listViewItem.findViewById(R.id.usertype);
        //UserAccounts useraccount = userAccountss.get(position);

        textusertype.setText("ระดับผู้ใช้งาน: " + patient.getUserType());
        if (getsensor.equals(nul)){
            textPatientDevice.setText("ไม่มีอุปกรณ์ติดตั้ง");
            textPatientDevice.setTextColor(Color.RED);
        }else{
            textPatientDevice.setText("มีอุปกรณ์ติดตั้ง");
            textPatientDevice.setTextColor(Color.GREEN);
        }
         textPatientName.setText("ชื่อผู้ใช้ :"+patient.getPatientName());
         textPatientAge.setText("อายุ :" + patient.getAge());
        String checksex = "เพศหญิง";
         if (getsex.equals(checksex)){
             userSex.setImageDrawable(userSex.getResources().getDrawable(R.drawable.userwomen, context.getApplicationContext().getTheme()));
         }else{
             userSex.setImageDrawable(userSex.getResources().getDrawable(R.drawable.usermen, context.getApplicationContext().getTheme()));
         }
        textPatientPhone.setText("เบอร์โทรศัพท์ :"+patient.getPhone());
         String nnull = "";
         if(getidsensor.equals(nnull)) {
             textSensorname.setVisibility(View.INVISIBLE);
         }else{
             DatabaseReference search = FirebaseDatabase.getInstance().getReference();
             Query query = search.child("sensors").orderByChild("id").equalTo(getidsensor);
             query.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     for (DataSnapshot data : dataSnapshot.getChildren()) {
                         Sensor sensor = data.getValue(Sensor.class);
                         String getsname = sensor.getName();
                         textSensorname.setText("ชื่ออุปกรณ์ที่ติดตั้งอยู่ : "+getsname);
                     }
                 }
                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });

         }
        return listViewItem;
    }
}
