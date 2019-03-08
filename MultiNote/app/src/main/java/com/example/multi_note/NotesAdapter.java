package com.example.multi_note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = "NotesAdapter";

    private ArrayList<Notes> notesArrayList;
    private MainActivity mainactivity;

    public NotesAdapter(MainActivity ma, ArrayList<Notes> notesarrayList) {
        notesArrayList = notesarrayList;
        mainactivity = ma;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list_item, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: onclick");
                int position = mainactivity.getRecyclerView().getChildAdapterPosition(view);
                mainactivity.domodify(view, position);
            }

        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View itemV) {
                try {
                    Log.d(TAG, "onLongClick: tryyyy");
                    final int position = mainactivity.getRecyclerView().getChildAdapterPosition(itemV);
                    Log.d(TAG, "onLongClick: here " + position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);
                    builder.setMessage("Delete Note 'Set DVR'?");
                    builder.setMessage("Delete Note 'Set DVR'?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                mainactivity.doremovenote(itemV, position);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } catch (Exception e) {

                    Log.d(TAG, "onLongClick: In catch");
                    e.printStackTrace();
                }

                return true;
            }
        });


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notes notes = notesArrayList.get(position);
        holder.title.setText(notes.getTitle());
        holder.date.setText(notes.getDate());
        holder.description.setText(notes.getDescription());
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    public void notesrefresh(ArrayList<Notes> notesArrayList)

    {
        this.notesArrayList=notesArrayList;
        notifyDataSetChanged();
    }

}

