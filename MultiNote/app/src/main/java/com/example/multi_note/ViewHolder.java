package com.example.multi_note;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/** Created by Mayur Mehta */

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView date;
    public TextView description;

    public ViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        date = (TextView) view.findViewById(R.id.date);
        description = (TextView) view.findViewById(R.id.description);
    }
}
