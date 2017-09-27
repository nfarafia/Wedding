package com.vergiliy.wedding.coasts;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private static Boolean callActionMode = false;

    // Provide a reference to the views for each data item
    class ViewHolder extends RecyclerView.ViewHolder {

        CoastsActivity context;
        CardView item;
        public TextView name;
        ImageView delete;
        ImageView edit;

        // Create ActionMode callback (Action Bar for Long click by item)
        private ActionMode.Callback ActionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                callActionMode = true;
                MenuInflater menuInflater = context.getMenuInflater();
                menuInflater.inflate(R.menu.action_mode, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // Edit item
                    case R.id.action_edit:
                        item.findViewById(R.id.ic_coast_edit).callOnClick();
                        mode.finish();
                        return true;
                    // Delete item
                    case R.id.action_delete:
                        item.findViewById(R.id.ic_coast_delete).callOnClick();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                callActionMode = false;
                context.getViewPager().getAdapter().notifyDataSetChanged();
            }
        };

        ViewHolder(final View itemView) {
            super(itemView);

            context = (CoastsActivity) itemView.getContext();
            item = (CardView) itemView.findViewById(R.id.coast_list_item);
            name = (TextView) itemView.findViewById(R.id.coast_list_name);
            delete = (ImageView) itemView.findViewById(R.id.ic_coast_delete);
            edit = (ImageView) itemView.findViewById(R.id.ic_coast_edit);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // ActionMode already running
                    if (callActionMode) {
                        return false;
                    }
                    context.startSupportActionMode(ActionModeCallback);
                    item.setCardBackgroundColor(Color.LTGRAY);

                    return true;
                }
            });
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