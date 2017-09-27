package com.vergiliy.wedding.coasts;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vergiliy.wedding.R;

import java.util.List;

class CoastsRecyclerAdapter extends RecyclerView.Adapter<CoastsRecyclerAdapter.ViewHolder> {

    private CoastsActivity context;
    private List<Coast> list;
    private CoastsDatabase database;

    // Provide a reference to the views for each data item
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        ImageView delete;
        ImageView edit;

        ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.coast_list_name);
            delete = (ImageView) itemView.findViewById(R.id.ic_coast_delete);
            edit = (ImageView) itemView.findViewById(R.id.ic_coast_edit);
        }
    }

    // Listener clicks on Delete button
    private class DeleteButtonListener implements View.OnClickListener {

        private Coast coast = null;

        // Get coast from main class CoastsRecyclerAdapter
        DeleteButtonListener(Coast coast) {
            this.coast = coast;
        }

        @Override
        public void onClick(View view) {
            // Delete row from database
            database.delete(coast.getId());

            // Update current fragment
            context.getViewPager().getAdapter().notifyDataSetChanged();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    CoastsRecyclerAdapter(Context context, List<Coast> list) {
        this.context = (CoastsActivity) context;
        this.list = list;
        database = new CoastsDatabase(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CoastsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.coasts_list_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Coast coast = list.get(position);

        holder.name.setText(coast.getName());
        holder.edit.setOnClickListener(new CoastProcessing(coast));
        holder.delete.setOnClickListener(new DeleteButtonListener(coast));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }
}