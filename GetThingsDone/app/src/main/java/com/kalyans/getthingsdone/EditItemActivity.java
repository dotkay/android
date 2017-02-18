package com.kalyans.getthingsdone;

import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditItemActivity extends AppCompatActivity {

    int task_text_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        EditText etItem = (EditText) findViewById(R.id.etEditItem);
        String task_text_str = getIntent().getStringExtra("task_text");
        //String task_text_pri = "HIGH";

        String task_text_pri = getIntent().getStringExtra("task_pri");
        // findViewById(R.id.etPriorityHigh).setSelected(true);
        switch (task_text_pri) {
            case "HIGH":
                findViewById(R.id.etPriorityHigh).setSelected(true);
                break;
            case "MEDIUM":
                findViewById(R.id.etPriorityMedium).setSelected(true);
                break;
            case "LOW":
                findViewById(R.id.etPriorityLow).setSelected(true);
                break;
        }
        task_text_pos = getIntent().getIntExtra("task_text_pos", 0);
        etItem.setText(task_text_str);
        etItem.setSelection(task_text_str.length());
    }

    public void onSubmit(View v) {
        EditText newEtText = (EditText) findViewById(R.id.etEditItem);

        Intent data = new Intent();
        data.putExtra("edited_task_text", newEtText.getText().toString());
        data.putExtra("edited_task_pos", this.task_text_pos);
        RadioGroup newRG = (RadioGroup) findViewById(R.id.EditRadioGroup);
        int new_pri = newRG.getCheckedRadioButtonId();
        RadioButton new_pri_but = (RadioButton)findViewById(new_pri);
        new_pri_but.setSelected(true);
        data.putExtra("edited_task_pri", new_pri_but.getText().toString());
        setResult(RESULT_OK, data);
        this.finish();
    }

    // event handler for the SAVE button
    // in activity_edit_item.xml, you will see an entry
    //             android:onClick="onSaveItem"
    public void onSaveItem(View view) {
        EditText newEtText = (EditText) findViewById(R.id.etEditItem);
        String savedEtText = newEtText.getText().toString();
        onSubmit(view);
    }
}
