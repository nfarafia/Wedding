package com.vergiliy.wedding.budget;

import android.content.Context;
import android.content.Intent;
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
import com.vergiliy.wedding.budget.cost.Cost;
import com.vergiliy.wedding.budget.cost.CostActivity;
import com.vergiliy.wedding.budget.cost.CostDialogListener;

import java.util.List;

public class BudgetRecyclerAdapter extends RecyclerView.Adapter<BudgetRecyclerAdapter.ViewHolder> {

    private BudgetActivity context;
    private List<Cost> list;

    public static ActionMode actionMode = null;
    private int position;

    // Provide a reference to the views for each data item
    class ViewHolder extends RecyclerView.ViewHolder {

        BudgetActivity context;
        CardView item;
        TextView name, amount, pending, paid;
        ImageView edit, delete;

        // Create ActionMode callback (Action Bar for Long click by item)
        private ActionMode.Callback ActionModeCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = context.getMenuInflater();
                menuInflater.inflate(R.menu.action_mode_cost, menu);
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
            item = itemView.findViewById(R.id.cost_card_item);
            name = itemView.findViewById(R.id.cost_card_name);
            amount = itemView.findViewById(R.id.cost_card_amount);
            pending = itemView.findViewById(R.id.cost_card_pending);
            paid = itemView.findViewById(R.id.cost_card_paid);
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
                    // Close ActionMode if it was open
                    if (actionMode != null) {
                        actionMode.finish();
                    }

                    // Start new Activity
                    Intent intent = new Intent(context, CostActivity.class);
                    Cost cost = list.get(getAdapterPosition()); // Get clicked cost id
                    intent.putExtra("id", cost.getId());
                    context.startActivity(intent);
                }
            });
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
            // Delete row from db_cost
            context.getDbCost().delete(cost.getId());

            // Update current fragment
            context.getViewPager().getAdapter().notifyDataSetChanged();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    BudgetRecyclerAdapter(Context context, List<Cost> list) {
        this.context = (BudgetActivity) context;
        this.list = list;
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
        holder.amount.setText(cost.getAmountAsString());
        holder.pending.setText(cost.getPendingAsString());
        holder.paid.setText(cost.getPaidAsString());
        holder.edit.setOnClickListener(new CostDialogListener(cost));
        holder.delete.setOnClickListener(new DeleteButtonListener(cost));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }
}