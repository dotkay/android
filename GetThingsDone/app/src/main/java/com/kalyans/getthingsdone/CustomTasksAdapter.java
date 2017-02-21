package com.kalyans.getthingsdone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by kalyans on 2/12/2017.
 */

public class CustomTasksAdapter extends ArrayAdapter<TodoTask>{

    // constructor
    public CustomTasksAdapter(Context context, ArrayList<TodoTask> tasks) {
        super(context, 0, tasks);
    }

    // get the view form the xml resource
    // inflate the xml into a view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data corresponding to position
        TodoTask todoTask = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }
        TextView tvTaskName = (TextView)convertView.findViewById(R.id.tvTaskName);
        TextView tvTaskPri = (TextView) convertView.findViewById(R.id.tvTaskPri);

        tvTaskPri.setText(todoTask.getStringPriority(todoTask.taskPriority));
        tvTaskName.setText(todoTask.taskText);
        if (tvTaskPri.getText().toString() == "HIGH") {
            tvTaskName.setTextColor(Color.RED);
            tvTaskName.setTypeface(null, Typeface.BOLD);
        }
        else if (tvTaskPri.getText().toString() == "MEDIUM") {
            tvTaskName.setTextColor(Color.rgb(255,165,0)); // orange
            tvTaskName.setTypeface(null, Typeface.NORMAL);
        }
        else if (tvTaskPri.getText().toString() == "LOW") {
            tvTaskName.setTextColor(Color.BLACK);
            tvTaskName.setTypeface(null, Typeface.NORMAL);
        }

        return convertView;
    }

}
