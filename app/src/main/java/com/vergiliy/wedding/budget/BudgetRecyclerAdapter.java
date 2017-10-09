package com.vergiliy.wedding.budget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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

class BudgetRecyclerAdapter extends RecyclerView.Adapter<BudgetRecyclerAdapter.ViewHolder> {

    private BudgetActivity context;
    private List<Cost> list;
    private CostDatabase database;

    static ActionMode actionMode = null;
    private int position;

    // Provide a reference to the views for each data item
    class ViewHolder extends RecyclerView.ViewHolder {

        BudgetActivity context;
        CardView item;
        public TextView name;
        ImageView complete_enable, complete_disable, edit, delete;

        // Create ActionMode callback (Action Bar for Long click by item)
        private ActionMode.Callback ActionModeCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = context.getMenuInflater();
                menuInflater.inflate(R.menu.action_mode, menu);

                // Get current Cost
                final Cost cost = list.get(position);

                // Show button to enable/disable complete
                if (cost.getComplete()) {
                    menu.findItem(R.id.action_complete_disable).setVisible(true);
                } else {
                    menu.findItem(R.id.action_complete_enable).setVisible(true);
                }

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // Enable complete item
                    case R.id.action_complete_enable:
                        item.findViewById(R.id.ic_cost_complete_enable).callOnClick();
                        mode.finish();
                        return true;
                    // Disable complete item
                    case R.id.action_complete_disable:
                        item.findViewById(R.id.ic_cost_complete_disable).callOnClick();
                        mode.finish();
                        return true;
                    // Edit item
                    case R.id.action_edit:
                        item.findViewById(R.id.ic_cost_edit).callOnClick();
                        mode.finish();
                        return true;
                    // Delete item
                    case R.id.action_delete:
                        item.findViewById(R.id.ic_cost_delete).callOnClick();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                context.getViewPager().getAdapter().notifyDataSetChanged();
            }
        };

        ViewHolder(final View itemView) {
            super(itemView);

            context = (BudgetActivity) itemView.getContext();
            item = itemView.findViewById(R.id.cost_list_item);
            name = itemView.findViewById(R.id.cost_list_name);
            complete_enable = itemView.findViewById(R.id.ic_cost_complete_enable);
            complete_disable = itemView.findViewById(R.id.ic_cost_complete_disable);
            edit = itemView.findViewById(R.id.ic_cost_edit);
            delete = itemView.findViewById(R.id.ic_cost_delete);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // ActionMode already running
                    if (actionMode != null) {
                        return false;
                    }
                    position = getAdapterPosition(); // Save current position to Tag
                    actionMode = context.startSupportActionMode(ActionModeCallback);
                    item.setCardBackgroundColor(Color.LTGRAY);

                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, CostActivity.class));
                }
            });
        }
    }

    // Listener clicks on Complete button
    private class CompleteButtonListener implements View.OnClickListener {

        private Cost cost = null;
        private boolean complete;

        // Get cost from main class BudgetRecyclerAdapter
        CompleteButtonListener(Cost cost, boolean complete) {
            this.cost = cost;
            this.complete = complete;
        }

        @Override
        public void onClick(View view) {
            // Update field complete_enable from db_main
            cost.setComplete(complete);
            database.update(cost);

            // Update current fragment
            context.getViewPager().getAdapter().notifyDataSetChanged();
        }
    }

    // Listener clicks on Delete button
    private class DeleteButtonListener implements View.OnClickListener {

        private Cost cost = null;

        // Get cost from main class BudgetRecyclerAdapter
        DeleteButtonListener(Cost cost) {
            this.cost = cost;
        }

        @Override
        public void onClick(View view) {
            // Delete row from db_main
            database.delete(cost.getId());

            // Update current fragment
            context.getViewPager().getAdapter().notifyDataSetChanged();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    BudgetRecyclerAdapter(Context context, List<Cost> list) {
        this.context = (BudgetActivity) context;
        this.list = list;
        database = new CostDatabase(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BudgetRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.cost_list_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Cost cost = list.get(position);
        holder.name.setText(cost.getLocaleName());

        // Creating a strikethrough text in TextView
        if (cost.getComplete())
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.complete_enable.setOnClickListener(new CompleteButtonListener(cost, true));
        holder.complete_disable.setOnClickListener(new CompleteButtonListener(cost, false));
        holder.edit.setOnClickListener(new CostProcessing(cost));
        holder.delete.setOnClickListener(new DeleteButtonListener(cost));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    // Get current ActionMode
    public ActionMode getActionMode() {
        return actionMode;
    }
}