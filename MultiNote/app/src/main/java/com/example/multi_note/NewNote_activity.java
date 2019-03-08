package com.example.multi_note;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewNote_activity extends AppCompatActivity {

    private String formattedDate;

    private static final String TAG = "NewNote_activity";
    EditText title;
    EditText description;

    private Notes notes;
    Intent data;
    ArrayList<Notes> notearraylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: in create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note_activity);

        title = (EditText)findViewById(R.id.title);
        description =(EditText) findViewById(R.id.description);

        data = getIntent();
        notes = (Notes) data.getSerializableExtra("Notes");
        if(notes!=null)
        {
            Log.d(TAG, "onCreate: "+ notes.getTitle());
            title.setText(notes.getTitle());
            description.setText(notes.getDescription());
        }
    }

    protected void onResume() {
        Log.d(TAG, "onResume: Resume");
        super.onResume();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newnote_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_menu:
                Log.d(TAG, "onOptionsItemSelected: ");
                formattedDate=new SimpleDateFormat("EEE MMM  d, HH:mm a").format(Calendar.getInstance().getTime());
                notes.setTitle(title.getText().toString());
                notes.setDescription(description.getText().toString());
                notes.setDate(formattedDate);
                data.putExtra("noteOBJ", notes);
                setResult(RESULT_OK, data);

                finish();
            default:

        }
        return super.onOptionsItemSelected(item);
    }
    protected void onPause(){
        Log.d(TAG, "onPause: Pause");


        notes.setDate(new SimpleDateFormat("EEE MMM  d, HH:mm a").format(Calendar.getInstance().getTime()));
        super.onPause();
    }

    public void onBackPressed()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onBackPressed: BACK");
                notes.setTitle(title.getText().toString());
                notes.setDescription(description.getText().toString());
                notes.setDate(new SimpleDateFormat("EEE MMM  d, HH:mm a").format(Calendar.getInstance().getTime()));
                data.putExtra("noteOBJ", notes);
                setResult(RESULT_OK, data);

                Log.d(TAG, "onClick: be");
                finish();
                Log.d(TAG, "onClick: af");
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setMessage("Save Your Note  'Set DVR'?");
        builder.setTitle("Your Note is Not Saved !");

        AlertDialog dialog = builder.create();
        dialog.show();


    }

}
