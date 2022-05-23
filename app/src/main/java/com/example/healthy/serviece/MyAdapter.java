package com.example.healthy.serviece;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthy.R;

public class MyAdapter extends ArrayAdapter<String> {
    Context context;
    String[] distances;
    String[] calories;
    public MyAdapter(Context context, String[] array,String[] distances,String[] calories){
        super(context, R.layout.activity_main,R.id.date,array);
        this.context=context;
        this.distances=distances;
        this.calories=calories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.activity_main,parent,false);

        TextView distance=row.findViewById(R.id.distance);
        distance.setText(distances[position]);

        TextView calorie=row.findViewById(R.id.calories);
        calorie.setText(calories[position]);

        return row;
    }
}