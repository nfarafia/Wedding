package com.vergiliy.wedding.budget.category;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vergiliy.wedding.R;

import java.util.List;

class CategoryRecyclerAdapter
        extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {

    private CategoryActivity context;
    private List<Category> list;

    static ActionMode actionMode = null;

    // Provide a reference to the views for each data item
    class ViewHolder extends RecyclerView.ViewHolder {

        CategoryActivity context;
        CardView item;
        public final TextView name;
        final ImageView reorder, edit, delete;

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
                        item.findViewById(R.id.ic_category_edit).callOnClick();
                        mode.finish();
                        return true;
                    // Delete item
                    case R.id.action_delete:
                        item.findViewById(R.id.ic_category_delete).callOnClick();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;

                // Update RecyclerView
                context.fillRecyclerView();
            }
        };

        ViewHolder(final View itemView) {
            super(itemView);

            context = (CategoryActivity) itemView.getContext();
            item = itemView.findViewById(R.id.category_card_item);
            name = itemView.findViewById(R.id.category_card_name);
            reorder = itemView.findViewById(R.id.category_card_reorder);
            edit = itemView.findViewById(R.id.ic_category_edit);
            delete = itemView.findViewById(R.id.ic_category_delete);

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
                    item.findViewById(R.id.ic_category_edit).callOnClick();
                }
            });
        }
    }

    // Listener clicks on Delete button
    private class DeleteButtonListener implements View.OnClickListener {

        private Category category = null;

        // Get category from main class CategoryRecyclerAdapter
        DeleteButtonListener(Category category) {
            this.category = category;
        }

        @Override
        public void onClick(View view) {

            // Get count costs in the deleted category
            int count = context.getDbCost().getCountByIdCategory(category.getId());

            if (count == 0) {
                // Delete row from db_category
                context.getDbCategory().delete(category.getId());

                // Update RecyclerView
                context.fillRecyclerView();
            // Show message if count costs bigger then 0
            } else {
                String message;
                if (count == 1) {
                    message = context.getString(R.string.category_delete_alert_one);
                } else {
                    message = context.getString(R.string.category_delete_alert_several, count);
                }

                // Show message
                new AlertDialog.Builder(context)
                        .setTitle(R.string.dialog_alert)
                        .setMessage(message)
                        .show();
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    CategoryRecyclerAdapter(Context context, List<Category> list) {
        this.context = (CategoryActivity) context;
        this.list = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoryRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Category category = list.get(position);
        holder.name.setText(category.getLocaleName());
        holder.edit.setOnClickListener(new CategoryDialogListener(category));
        holder.delete.setOnClickListener(new DeleteButtonListener(category));
        holder.reorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    context.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }
}