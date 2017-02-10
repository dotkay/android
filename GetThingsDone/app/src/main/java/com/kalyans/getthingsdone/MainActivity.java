package com.kalyans.getthingsdone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
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
        itemsAdapter.add(itemText);
        etNewItem.setText("");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // position is the position of the item in the list
        items.remove(position);
        itemsAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // create an Intent to start a new activity (in this case EditItemActivity)
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        String task_text_str = items.get(position);
        int task_text_pos = position;
        i.putExtra("task_text", task_text_str);
        i.putExtra("task_text_pos", task_text_pos);
        // start the activity with an expected result
        // we need the result - the edited item back
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String savedItem = data.getExtras().getString("edited_task_text");
            int saved_item_pos = data.getExtras().getInt("edited_task_pos");
            items.set(saved_item_pos, savedItem);
            itemsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Item " + Integer.toString(saved_item_pos) + " has changed!", Toast.LENGTH_SHORT).show();
        }
    }
}
