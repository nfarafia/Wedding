package com.vergiliy.wedding.checklist.task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.BaseClass;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.category.Category;

public class TaskFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TaskActivity activity = (TaskActivity) getActivity();
        Task task = activity.getTask();
        String currency = activity.getLocaleClass().getCurrency();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        // Get fields
        TextView nameField = view.findViewById(R.id.task_view_name);
        TextView noteField = view.findViewById(R.id.task_view_note);
        TextView categoryField = view.findViewById(R.id.task_view_category);
        TextView amountField = view.findViewById(R.id.task_view_amount);
        TextView pendingField = view.findViewById(R.id.task_view_pending);
        TextView paidField = view.findViewById(R.id.task_view_paid);
        TextView balanceField = view.findViewById(R.id.task_view_balance);
        TextView updateField = view.findViewById(R.id.task_view_update);

        nameField.setText(task.getLocaleName());
        Category category = BaseClass
                .findObjectInListById(activity.getCategories(), task.getIdCategory());
        String categoryName = category != null ?
                category.getLocaleName() : getString(R.string.task_view_category_null);
        categoryField.setText(categoryName);
        noteField.setText(task.getLocaleNote(R.string.task_view_note_null));
        amountField.setText(getString(R.string.currency, task.getAmountAsString(), currency));
        pendingField.setText(getString(R.string.currency, task.getPendingAsString(), currency));
        paidField.setText(getString(R.string.currency, task.getPaidAsString(), currency));

        Double balance = task.getBalance();
        Integer color;
        if (balance < 0) {
            color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        } else if (balance > 0){
            color = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        } else {
            color = balanceField.getCurrentTextColor();
        }
        balanceField.setTextColor(color);
        balanceField.setText(getString(R.string.currency, task.getBalanceAsString(), currency));

        updateField.setText(task.getUpdateAsLocale(R.string.task_view_update_null));

        return view;
    }
}