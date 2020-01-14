package com.example.oa_te.senserapp.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.models.Sensor;

import java.util.List;

public class SensorList extends ArrayAdapter<Sensor> {
    private Activity context;
    private List<Sensor> sensors;

    public SensorList(Activity context, List<Sensor> sensors) {
        super(context, R.layout.layout_sensor_list, sensors);
        this.context = context;
        this.sensors = sensors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_sensor_list, null, true);
        TextView textViewSenser = listViewItem.findViewById(R.id.textViewSenser);
        TextView textViewRoom = listViewItem.findViewById(R.id.textViewRoom);
        TextView textViewStatus = listViewItem.findViewById(R.id.textViewStatus);
        Sensor sensor = sensors.get(position);
        textViewSenser.setText(("   ")+sensor.getName());
        textViewRoom.setText(("    ")+sensor.getRoom());
        textViewStatus.setText(("    ")+sensor.getStatus());
        return listViewItem;

    }
}

