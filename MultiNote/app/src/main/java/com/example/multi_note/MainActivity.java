package com.example.multi_note;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int NEWNOTE_REQUESTCODE = 1;
    private static final int MODIFY_REQUESTCODE=2;

    private static final String TAG = "MainActivity";

    private ArrayList<Notes> notearraylist = new ArrayList<>();
    private Notes notes;
    private String formattedDate;
    EditText title;
    EditText description;
    ArrayList<Notes> notesArrayList;
    private int position = -1;

    private RecyclerView recyclerView;

    private NotesAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        noteAdapter = new NotesAdapter(this, notearraylist);

        recyclerView.setAdapter(noteAdapter);
        LinearLayoutManager linearlayout=new LinearLayoutManager(this);
        linearlayout.setReverseLayout(true);
        linearlayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearlayout);

        /** Add JSON part here */
        JSonAsyncTask jSonAsyncTask=new JSonAsyncTask(this);
        jSonAsyncTask.execute(getString(R.string.filename));   //notes.json//

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }


    public void fetchAsynTaskData(ArrayList<Notes> arrayList)
    {
        notearraylist=arrayList;
        noteAdapter.notesrefresh(notearraylist);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.info_menu:
                Log.d(TAG, "onOptionsItemSelected: ");
                Intent intent = new Intent(this, Info_activity.class);
                intent.putExtra("MultiNote", "Multi-Note");
                intent.putExtra("author", " ©  2019 ,  Mayur Mehta");
                intent.putExtra("version", " Version 7.0 ");
                startActivity(intent);
                break;

            case R.id.add_menu:
                Log.d(TAG, "onOptionsItemSelected: ");
                Intent intent1 = new Intent(this, NewNote_activity.class);
                notes = new Notes();
                intent1.putExtra("Notes", notes);
                startActivityForResult(intent1, NEWNOTE_REQUESTCODE);
                break;

            default:

        }
        return true;
    }


    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEWNOTE_REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: result ok");
                notes = (Notes)data.getSerializableExtra("noteOBJ");
                try {
                    Log.d(TAG, "onActivityResult: in try");
                    if(notes.getTitle().toString().equals(""))
                    {
                        Log.d(TAG, "onActivityResult: if");
                        Toast.makeText(this,"Un-titled activity was not saved !",Toast.LENGTH_SHORT).show();}
                    else{
                        Log.d(TAG, "onActivityResult: else");
                        saveNotes(notearraylist,false,false);}
                }catch (IOException e) {
                    e.printStackTrace();
                }
                noteAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == MODIFY_REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: result ok");
                notes = (Notes)data.getSerializableExtra("noteOBJ");
                try {
                    Log.d(TAG, "onActivityResult: in try");
                    notearraylist.set(this.position, notes);
                    saveNotes(notearraylist,true,true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                noteAdapter.notifyDataSetChanged();
            }
        }
    }

    private void saveNotes(ArrayList<Notes> arrayList, Boolean isDelete,Boolean isNotedit) throws IOException {

        Log.d(TAG, "saveNotes: Saving Notes");
        try {
            if(arrayList!=null) {
                notearraylist=arrayList;}

            Log.d(TAG, "saveNotes: note");
            Log.d(TAG, "saveNotes: size1 = " + notearraylist.size());
            if (notes == null) {
                Log.d(TAG, "saveNotes: null obj");
            }
            else
            {
                if (!isDelete || !isNotedit) {
                    Log.d(TAG, "saveNotes: "+notes.getTitle());
                    notearraylist.add(notes);
                }
            }
            Log.d(TAG, "saveNotes: size2 = " + notearraylist.size());

            FileOutputStream fileoutputstram;
            Log.d(TAG, "saveNotes: to create file");
            fileoutputstram = getApplicationContext().openFileOutput(getString(R.string.filename), Context.MODE_PRIVATE);
            Log.d(TAG, "saveNotes: file created");
            JsonWriter jsonwriter;
            jsonwriter = new JsonWriter(new OutputStreamWriter(fileoutputstram, getString(R.string.encoding)));
            jsonwriter.setIndent("  ");
            jsonwriter.beginArray();
            for (int i = 0; i < notearraylist.size(); i++) {
                jsonwriter.beginObject();
                jsonwriter.name("title").value((notearraylist.get(i).getTitle()));
                jsonwriter.name("date").value((notearraylist.get(i).getDate()));
                jsonwriter.name("description").value(notearraylist.get(i).getDescription());
                jsonwriter.endObject();
            }
            jsonwriter.endArray();
            jsonwriter.close();
            Log.d(TAG, "saveNotes: file created");

            StringWriter stringwriter = new StringWriter();
            jsonwriter = new JsonWriter(stringwriter);
            jsonwriter.setIndent("  ");
            jsonwriter.beginArray();
            for (int i = 0; i < notearraylist.size(); i++) {
                jsonwriter.beginObject();
                jsonwriter.name("title").value((notearraylist.get(i).getTitle()));
                jsonwriter.name("date").value((notearraylist.get(i).getDate()));
                jsonwriter.name("description").value(notearraylist.get(i).getDescription());
                jsonwriter.endObject();

            }
            jsonwriter.endArray();
            jsonwriter.close();
            Log.d(TAG, "saveNotes: JSon created" + stringwriter.toString());


        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "saveNotes: Unsupported Encoding");
            e.printStackTrace();
        }
        catch (Exception e)
        {
            Log.d(TAG, "saveNotes: Exception e");
            e.printStackTrace();
        }
    }

    public void doremovenote(View v, int position) throws IOException {
        Log.d(TAG, "doremovenote: remove"+ position);
        notearraylist.remove(position);
        //noteAdapter.notifyDataSetChanged();
        Log.d(TAG, "doremovenote: size= "+ notearraylist.size());
        saveNotes(notearraylist,true,true);
        Log.d(TAG, "doremovenote: s= "+notearraylist.size());
        noteAdapter.notifyDataSetChanged();
    }

    public void domodify(View v,int position){
        Log.d(TAG, "domodify: in modify");
        notes=notearraylist.get(position);
        this.position = position;
        Intent intent=new Intent(this,NewNote_activity.class);
        intent.putExtra("Notes",notes);
        startActivityForResult(intent,MODIFY_REQUESTCODE);
    }

    @Override
    protected void onPause() {
        try {
            saveNotes(notearraylist,true,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
    }



}
