package com.vergiliy.wedding.budget.payment;

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
import com.vergiliy.wedding.budget.cost.CostActivity;

import java.util.List;

public class PaymentRecyclerAdapter extends RecyclerView.Adapter<PaymentRecyclerAdapter.ViewHolder> {

    private CostActivity context;
    private List<Payment> list;

    public static ActionMode actionMode = null;
    private int position;

    // Provide a reference to the views for each data item
    class ViewHolder extends RecyclerView.ViewHolder {

        CostActivity context;
        CardView item;
        public TextView name, amount, date;
        ImageView icon, complete_enable, complete_disable, edit, delete;

        // Create ActionMode callback (Action Bar for Long click by item)
        private ActionMode.Callback ActionModeCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = context.getMenuInflater();
                menuInflater.inflate(R.menu.action_mode_payment, menu);

                // Get current Payment
                final Payment payment = list.get(position);

                // Show button to enable/disable complete
                if (payment.getComplete()) {
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
                        item.findViewById(R.id.ic_payment_complete_enable).callOnClick();
                        mode.finish();
                        return true;
                    // Disable complete item
                    case R.id.action_complete_disable:
                        item.findViewById(R.id.ic_payment_complete_disable).callOnClick();
                        mode.finish();
                        return true;
                    // Edit item
                    case R.id.action_edit:
                        item.findViewById(R.id.ic_payment_edit).callOnClick();
                        mode.finish();
                        return true;
                    // Delete item
                    case R.id.action_delete:
                        item.findViewById(R.id.ic_payment_delete).callOnClick();
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

            context = (CostActivity) itemView.getContext();
            item = itemView.findViewById(R.id.payment_card_item);
            icon = itemView.findViewById(R.id.payment_card_icon);
            name = itemView.findViewById(R.id.payment_card_name);
            date = itemView.findViewById(R.id.payment_card_date);
            amount = itemView.findViewById(R.id.payment_card_amount);
            complete_enable = itemView.findViewById(R.id.ic_payment_complete_enable);
            complete_disable = itemView.findViewById(R.id.ic_payment_complete_disable);
            edit = itemView.findViewById(R.id.ic_payment_edit);
            delete = itemView.findViewById(R.id.ic_payment_delete);

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
                    item.findViewById(R.id.ic_payment_edit).callOnClick();
                }
            });
        }
    }

    // Listener clicks on Complete button
    private class CompleteButtonListener implements View.OnClickListener {

        private Payment payment = null;
        private boolean complete;

        // Get payment from main class BudgetRecyclerAdapter
        CompleteButtonListener(Payment payment, boolean complete) {
            this.payment = payment;
            this.complete = complete;
        }

        @Override
        public void onClick(View view) {
            // Update field complete_enable from db_payment
            payment.setComplete(complete);
            context.getDbPayment().update(payment);

            // Update current fragment
            context.getViewPager().getAdapter().notifyDataSetChanged();
        }
    }

    // Listener clicks on Delete button
    private class DeleteButtonListener implements View.OnClickListener {

        private Payment payment = null;

        // Get payment from main class BudgetRecyclerAdapter
        DeleteButtonListener(Payment payment) {
            this.payment = payment;
        }

        @Override
        public void onClick(View view) {
            // Delete row from db_payment
            context.getDbPayment().delete(payment);

            // Update current fragment
            context.getViewPager().getAdapter().notifyDataSetChanged();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    PaymentRecyclerAdapter(Context context, List<Payment> list) {
        this.context = (CostActivity) context;
        this.list = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PaymentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.payment_list_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Payment payment = list.get(position);
        holder.name.setText(payment.getLocaleName());
        holder.amount.setText(payment.getAmountAsString());
        holder.date.setText(payment.getDateAsLocaleWithComplete(R.string.payment_card_date_null));

        // Creating a strikethrough text in TextView
        if (payment.getComplete()) {
            holder.icon.setImageResource(R.drawable.ic_complete);
        }

        holder.complete_enable.setOnClickListener(new CompleteButtonListener(payment, true));
        holder.complete_disable.setOnClickListener(new CompleteButtonListener(payment, false));
        holder.edit.setOnClickListener(new PaymentDialogListener(payment));
        holder.delete.setOnClickListener(new DeleteButtonListener(payment));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }
}