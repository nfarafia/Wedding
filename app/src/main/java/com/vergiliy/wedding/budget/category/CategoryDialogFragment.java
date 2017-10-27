package com.vergiliy.wedding.budget.category;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vergiliy.wedding.BaseClass;
import com.vergiliy.wedding.BaseDialogFragment;
import com.vergiliy.wedding.R;

import static com.vergiliy.wedding.helpers.BaseHelper.hideKeyboardWhenLostFocus;

public class CategoryDialogFragment extends BaseDialogFragment {

    private CategoryActivity context;
    private EditText nameField;
    private Category category;

    // Listener clicks on Edit button
    private class PositiveButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            final String name = nameField.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(context, R.string.category_dialog_error,
                        Toast.LENGTH_LONG).show();
            } else {
                Category item = new Category(context);

                // Update name if it modified
                if (category == null || TextUtils.isEmpty(category.getLocaleName()) ||
                        !category.getLocaleName().equals(name)) {
                    item.setName(BaseClass.LANGUAGE_DEFAULT, name);
                } else {
                    item.setName(BaseClass.LANGUAGE_DEFAULT, category.getName());
                }

                if (category != null) {
                    item.setId(category.getId());
                    context.getDbCategory().update(item);
                } else {
                    context.getDbCategory().add(item);
                }

                if (dialog != null) {
                    dialog.dismiss();
                }

                // Update RecyclerView
                context.fillRecyclerView();
            }
        }
    }

    public static CategoryDialogFragment newInstance(Category category) {
        CategoryDialogFragment fragment = new CategoryDialogFragment();
        fragment.setCategory(category);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (CategoryActivity) super.context;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_title = inflater.inflate(R.layout.dialog_title, nullGroup);
        View dialog_body = inflater.inflate(R.layout.category_diallog_add, nullGroup);

        // Get Title field
        TextView titleView =  dialog_title.findViewById(R.id.dialog_title);

        // Get Body fields
        nameField = dialog_body.findViewById(R.id.category_edit_name);

        // Get service buttons
        Button cancelButton = dialog_body.findViewById(R.id.dialog_button_cancel);
        Button yesButton = dialog_body.findViewById(R.id.dialog_button_yes);


        if (category != null) {
            nameField.setText(category.getLocaleName());
            titleView.setText(R.string.category_dialog_title_edit);
            yesButton.setText(R.string.dialog_button_edit);
        } else {
            titleView.setText(R.string.category_dialog_title_add);
            yesButton.setText(R.string.dialog_button_add);
        }

        // Create new alert dialog
        dialog = new AlertDialog.Builder(context, R.style.DialogFullScreen)
                .setCustomTitle(dialog_title)
                .setView(dialog_body)
                .create();

        // Don't close dialog, when click outside
        dialog.setCanceledOnTouchOutside(false);

        // Hide keyboard after clicking outside
        hideKeyboardWhenLostFocus(context, dialog_body);
        hideKeyboardWhenLostFocus(context, dialog_title);

        yesButton.setOnClickListener(new PositiveButtonListener());
        cancelButton.setOnClickListener(new NegativeButtonListener());

        return dialog;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}