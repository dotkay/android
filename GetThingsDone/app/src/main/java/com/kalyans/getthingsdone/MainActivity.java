package com.kalyans.getthingsdone;

import android.content.Intent;
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
    HashMap filehash = new HashMap<String, String>();
    ArrayList<String> alines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();
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
        itemsAdapter.add(task_to_add);
        etNewItem.setText("");
        priGroup.clearCheck();
        //writeItems();
        Log.e("INFO: call write gc", Integer.toString(itemsAdapter.getCount()));
        writeItem(task_to_add);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // position is the position of the item in the list
        TodoTask rem_item = items.get(position);
        // removeItem(position);
        //removeItem(rem_item);
        items.remove(position);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // create an Intent to start a new activity (in this case EditItemActivity)
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        TodoTask task = items.get(position);
        String task_text_str = task.taskText;
        String task_text_pri = task.taskPriority.toString();
        int task_text_pos = position;
        i.putExtra("task_text", task_text_str);
        i.putExtra("task_text_pos", task_text_pos);
        i.putExtra("task_pri", task_text_pri);
        // start the activity with an expected result
        // we need the result - the edited item back
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
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
            // items.set(saved_item_pos, savedItem);
            items.set(saved_item_pos, edited_todo_task);
            itemsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Item " + Integer.toString(saved_item_pos) + " has changed!", Toast.LENGTH_SHORT).show();
        }
    }


//    private void readItems() {
//        File filesDirectory = getFilesDir();
//        File todoFile = new File(filesDirectory, "todo.txt");
//        try {
//            items = new ArrayList<TodoTask>(FileUtils.readLines(todoFile));
//        }
//        catch (IOException e) {
//            items = new ArrayList<TodoTask>();
//            e.printStackTrace();
//        }
//    }
//
    private void readItems() {
        File filesDirectory = getFilesDir();
        File todoFile = new File(filesDirectory, "tfile1.txt");
        int count = 0;
        try {
            // ArrayList<String> alines = new ArrayList<String>(FileUtils.readLines(todoFile));
            alines = new ArrayList<String>(FileUtils.readLines(todoFile));
            Toast.makeText(this, "itemcount " + Integer.toString(alines.size()) + "!!", Toast.LENGTH_SHORT).show();
            int alength = alines.size();
            Log.e("INFO: alines size(): ", Integer.toString(alines.size()));
            Iterator itr = alines.iterator();
            // for (int i=0; i < alength; i++) {
            while (itr.hasNext()) {
                count++;
                String str = (String) itr.next();
                String[] tparts = str.split(":", 2);
                String atext = tparts[0];
                String apri_str = tparts[1];
                TodoTask.Priority apri = HIGH;
                switch(apri_str) {
                    case "HIGH":
                        apri = HIGH;
                        break;
                    case "MEDIUM":
                        apri = MEDIUM;
                        break;
                    case "LOW":
                        apri = LOW;
                        break;
                }
                TodoTask atask = new TodoTask(atext, apri);
                Log.e("INFO: reading: ", atext);
                items.add(atask);
            }
        } catch (IOException e) {
            ArrayList<TodoTask> alines = new ArrayList<TodoTask>();
            Log.e("INFO: ", "IO exception in readItems()");
            e.printStackTrace();
        }
    }

    private void writeItems() {
        File filesDirectory = getFilesDir();
        File todoFile = new File(filesDirectory, "tfile1.txt");
        Iterator itr = items.iterator();
        int count = 0;
        Toast.makeText(this, Integer.toString(items.size()), Toast.LENGTH_SHORT).show();
        while (itr.hasNext()) {
            count++;
            TodoTask atask = (TodoTask) itr.next();
            String atext = atask.taskText;
            String apri = atask.taskPriority.toString();
            String atask_str = atext + ":" + apri + '\n';
            try {
                FileUtils.writeStringToFile(todoFile, atask_str, Charsets.UTF_8, false);
                Log.e("INFO: writing: ", atext);
            } catch (IOException e) {
                Log.e("INFO: ", "IO exception in writeItems()");
                e.printStackTrace();
            }
        }
        Log.e("INFO: items written: ", Integer.toString(count));
    }

    private void writeItem(TodoTask atask) {
        File filesDirectory = getFilesDir();
        File todoFile = new File(filesDirectory, "tfile1.txt");
        String atext = atask.taskText;
        String apri = atask.taskPriority.toString();
        String atask_str = atext + ":" + apri + "\n";
        filehash.put(atext, atask_str);
        try {
            FileUtils.writeStringToFile(todoFile, atask_str, Charsets.UTF_8, true);
        } catch (IOException e) {
            Log.e("INFO: ", "IO exception in writeItems()");
            e.printStackTrace();
        }
    }

    private void removeItem(TodoTask t) {
        File filesDirectory = getFilesDir();
        File todoFile = new File(filesDirectory, "tfile1.txt");
        File tempFile = new File(filesDirectory, "tmp.txt");
        String remove_task_str = (String) filehash.get(t.taskText);
        Log.e("INFO: removing at: ", t.taskText + " @ " + Integer.toString(itemsAdapter.getPosition(t)));
        long line_count = 0;
        try {
            LineIterator litr = FileUtils.lineIterator(todoFile);
            while(litr.hasNext()) {
                line_count++;
                String line = litr.nextLine();
                if (!line.equals(remove_task_str)) {
                    FileUtils.writeStringToFile(tempFile, line, Charsets.UTF_8, false);
                }
                else {
                    Log.e("INFO: removing", line);
                }
            }
            tempFile.renameTo(todoFile);
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    private void removeItem(int position) {
//        File filesDirectory = getFilesDir();
//        File todoFile = new File(filesDirectory, "tfile.txt");
//        File tempFile = new File(filesDirectory, "tmp.txt");
//        TodoTask remove_task = items.get(position);
//        String remove_task_str = (String) filehash.get(remove_task);
//        Log.e("INFO: removing: ", remove_task.taskText);
//        long line_count = 0;
//        try {
//            LineIterator litr = FileUtils.lineIterator(todoFile);
//            while(litr.hasNext()) {
//                line_count++;
//                String line = litr.nextLine();
//                if (line.equals(remove_task_str)) {
//                    Log.e("INFO: line: ", line);
//                    continue;
//                }
//                FileUtils.writeStringToFile(tempFile, line, Charsets.UTF_8, false);
//
//            }
//            tempFile.renameTo(todoFile);
//            tempFile.delete();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    private void writeItems() {
//        File filesDirectory = getFilesDir();
//        File todoFile = new File(filesDirectory, "todo.txt");
//        // File priFile = new File(filesDirectory, "pri.txt");
//        try {
//            FileUtils.writeLines(todoFile, items);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}