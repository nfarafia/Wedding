package com.vergiliy.wedding.tasks;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.R;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[] adapterData;

    // Provide a reference to the views for each data item
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView textview;
        private ViewHolder(TextView v) {
            super(v);
            textview = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    RecyclerAdapter(String[] data) {
        adapterData = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView textview = (TextView) LayoutInflater
                .from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        return new ViewHolder(textview);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int currentPosition = holder.getAdapterPosition();
        holder.textview.setText(adapterData[currentPosition]);

        holder.textview.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = view.getResources()
                        .getString(R.string.test_tasks_item_click, currentPosition);
                Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return adapterData.length;
    }
}