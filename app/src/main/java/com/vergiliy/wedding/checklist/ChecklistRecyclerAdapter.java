package com.vergiliy.wedding.checklist;

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
import com.vergiliy.wedding.checklist.task.Task;
import com.vergiliy.wedding.checklist.task.TaskActivity;
import com.vergiliy.wedding.checklist.task.TaskDialogListener;

import java.util.List;

public class ChecklistRecyclerAdapter extends RecyclerView.Adapter<ChecklistRecyclerAdapter.ViewHolder> {

    private ChecklistActivity context;
    private List<Task> list;

    public static ActionMode actionMode = null;

    // Provide a reference to the views for each data item
    class ViewHolder extends RecyclerView.ViewHolder {

        ChecklistActivity context;
        CardView item;
        TextView name, amount, pending, paid;
        ImageView edit, delete;

        // Create ActionMode callback (Action Bar for Long click by item)
        private ActionMode.Callback ActionModeCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = context.getMenuInflater();
                menuInflater.inflate(R.menu.action_mode_main, menu);
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
                        item.findViewById(R.id.ic_task_edit).callOnClick();
                        mode.finish();
                        return true;
                    // Delete item
                    case R.id.action_delete:
                        item.findViewById(R.id.ic_task_delete).callOnClick();
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

            context = (ChecklistActivity) itemView.getContext();

            item = itemView.findViewById(R.id.task_card_item);
            name = itemView.findViewById(R.id.task_card_name);
            amount = itemView.findViewById(R.id.task_card_amount);
            pending = itemView.findViewById(R.id.task_card_pending);
            paid = itemView.findViewById(R.id.task_card_paid);
            edit = itemView.findViewById(R.id.ic_task_edit);
            delete = itemView.findViewById(R.id.ic_task_delete);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    // ActionMode already running
                    if (actionMode != null) {
                        return false;
                    }
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
                    Intent intent = new Intent(context, TaskActivity.class);
                    Task task = list.get(getAdapterPosition()); // Get clicked task id
                    intent.putExtra("id", task.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    // Listener clicks on Delete button
    private class DeleteButtonListener implements View.OnClickListener {

        private Task task = null;

        // Get task from main class ChecklistRecyclerAdapter
        DeleteButtonListener(Task task) {
            this.task = task;
        }

        @Override
        public void onClick(View view) {
            // Delete row from db_task
            context.getDbTask().delete(task);

            // Update current fragment
            context.getViewPager().getAdapter().notifyDataSetChanged();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    ChecklistRecyclerAdapter(Context context, List<Task> list) {
        this.context = (ChecklistActivity) context;
        this.list = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChecklistRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Task task = list.get(position);
        holder.name.setText(task.getLocaleName());
        holder.amount.setText(context.getString(R.string.currency, task.getAmountAsString(),
                context.getLocaleClass().getCurrency()));
        holder.pending.setText(task.getPendingAsString());
        holder.paid.setText(task.getPaidAsString());
        holder.edit.setOnClickListener(new TaskDialogListener(task));
        holder.delete.setOnClickListener(new DeleteButtonListener(task));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }
}