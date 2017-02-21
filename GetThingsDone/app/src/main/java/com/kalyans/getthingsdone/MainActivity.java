package com.kalyans.getthingsdone;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.kalyans.getthingsdone.TodoTask.Priority.HIGH;
import static com.kalyans.getthingsdone.TodoTask.Priority.LOW;
import static com.kalyans.getthingsdone.TodoTask.Priority.MEDIUM;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    ArrayList<TodoTask> items;
    CustomTasksAdapter itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 200;
    String Priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(this);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<>();
        items = db.getAllTasks();
        itemsAdapter = new CustomTasksAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        // this click listener takes the user to the EditItemActivity
        // in order to edit the chosen task
        lvItems.setOnItemClickListener(this);
        // this long click listener deletes the chosen task
        lvItems.setOnItemLongClickListener(this);
    }

    // event handler for the ADD button.
    // In the activity_main.xml, you will see an entry
    //          android:onClick="onAddItem"
    public void onAddItem(View view) {

        DatabaseHandler db = new DatabaseHandler(this);

        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        RadioGroup priGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedPriority = priGroup.getCheckedRadioButtonId();
        TodoTask.Priority itemPri = HIGH;

        // no radio button was clicked, default to HIGH priority
        if (selectedPriority == -1) {
            findViewById(R.id.priorityHigh).setSelected(true);
        }
        // if an item priority was indeed chosen (radio button clicked)
        else {
            RadioButton selectedButton = (RadioButton)findViewById(selectedPriority);
            switch(selectedButton.getId()) {
                case R.id.priorityHigh:
                    itemPri = HIGH;
                    break;
                case R.id.priorityMedium:
                    itemPri = MEDIUM;
                    break;
                case R.id.priorityLow:
                    itemPri = LOW;
                    break;
            }
        }
        TodoTask task_to_add = new TodoTask(itemText, itemPri);
        db.addTask(task_to_add);
        itemsAdapter.add(task_to_add);
        etNewItem.setText("");
        priGroup.clearCheck();
        Log.e("INFO: call write gc", Integer.toString(itemsAdapter.getCount()));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        DatabaseHandler db = new DatabaseHandler(this);

        // position is the position of the item in the list
        TodoTask rem_item = items.get(position);
        items.remove(position);
        itemsAdapter.notifyDataSetChanged();

        db.removeTask(rem_item);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // create an Intent to start a new activity (in this case EditItemActivity)
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        TodoTask task = items.get(position);
        String task_text_str = task.getTaskName();
        String task_text_pri = task.getStringPriority(task.taskPriority);
        i.putExtra("task_text", task_text_str);
        i.putExtra("task_text_pos", position);
        i.putExtra("task_pri", task_text_pri);
        // start the activity with an expected result
        // we need the result - the edited item back
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            DatabaseHandler db = new DatabaseHandler(this);

            String savedItem = data.getExtras().getString("edited_task_text");
            int saved_item_pos = data.getExtras().getInt("edited_task_pos");
            String saved_item_pri = data.getExtras().getString("edited_task_pri");
            TodoTask.Priority saved_task_pri = HIGH;
            switch (saved_item_pri) {
                case "HIGH":
                    saved_task_pri = HIGH;
                    break;
                case "MEDIUM":
                    saved_task_pri = MEDIUM;
                    break;
                case "LOW":
                    saved_task_pri = LOW;
                    break;
            }
            TodoTask edited_todo_task = new TodoTask(savedItem, saved_task_pri);
            items.set(saved_item_pos, edited_todo_task);
            db.updateTask(edited_todo_task);
            itemsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Item " + Integer.toString(saved_item_pos) + " has changed!", Toast.LENGTH_SHORT).show();
        }
    }
}