package com.vergiliy.wedding.checklist.summary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.BaseClass;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.category.Category;
import com.vergiliy.wedding.category.CategoryDatabase;
import com.vergiliy.wedding.checklist.task.TaskDatabase;
import com.vergiliy.wedding.helpers.BaseHelper;
import com.vergiliy.wedding.setting.SettingFragmentsActivity;

import java.util.List;

public class SummaryActivity extends BaseActivity {

    private TaskDatabase db_task;
    private CategoryDatabase db_category;

    Spinner categoryField;
    TextView totalField, usedField, amountField, pendingField, paidField, balanceField,
            tasksTotalField, subtasksTotalField, subtasksPendingField, subtasksPaidField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_checklist_balance_title);
        setContentView(R.layout.activity_checklist_summary);
        // Change activities with animation
        overridePendingTransition(R.anim.create_slide_in, R.anim.create_slide_out);

        db_task = new TaskDatabase(this);
        db_category = new CategoryDatabase(this);

        // Get fields
        categoryField = (Spinner) findViewById(R.id.checklist_summary_category);
        totalField = (TextView) findViewById(R.id.checklist_summary_total);
        usedField = (TextView) findViewById(R.id.checklist_summary_used);
        amountField = (TextView) findViewById(R.id.checklist_summary_amount);
        pendingField = (TextView) findViewById(R.id.checklist_summary_pending);
        paidField = (TextView) findViewById(R.id.checklist_summary_paid);
        balanceField = (TextView) findViewById(R.id.checklist_summary_balance);
        tasksTotalField = (TextView) findViewById(R.id.checklist_summary_total_tasks);
        subtasksTotalField = (TextView) findViewById(R.id.checklist_summary_total_subtasks);
        subtasksPendingField = (TextView) findViewById(R.id.checklist_summary_total_subtasks_pending);
        subtasksPaidField = (TextView) findViewById(R.id.checklist_summary_total_subtasks_paid);

        // Create default item (all category)
        Category item = new Category(this);
        item.setName(BaseClass.LANGUAGE_DEFAULT, R.string.category_all);

        // Add default item
        List<Category> categories = db_category.getAll();
        categories.add(0, item);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Category> adapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, categories);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryField.setAdapter(adapter);

        // Update balance after choose category
        categoryField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category) categoryField.getSelectedItem();
                updateBalance(category.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Show back button in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        // Refresh TabLayout when return from CategoryActivity
        if (data.getStringExtra("class").equals(SettingFragmentsActivity.class.getSimpleName())) {
            // Update statistics
            Category category = (Category) categoryField.getSelectedItem();
            updateBalance(category.getId());
        }
    }

    // Create top context menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checklist_summary_action_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Return to previous activity; call method onBackPressed
            case android.R.id.home:
                onBackPressed();
                break;
            // Open SettingsWeddingFragment to edit budget
            case R.id.menu_action_setting:
                Intent intent = new Intent(getApplicationContext(), SettingFragmentsActivity.class);
                intent.putExtra("position", 1); // Transfer id
                startActivityForResult(intent, 1);
                break;
            // If another id, call super method
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Show previous activity with animation
        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
    }

    // Close Database connection when activity destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db_task != null) {
            db_task.close();
        }
        if (db_category != null) {
            db_category.close();
        }
    }

    private void updateBalance(Integer id_category) {
        Integer color;
        Summary balance = db_task.getBalance(id_category);
        String currency = getLocaleClass().getCurrency();

        final double budget = BaseHelper.parseDouble(preferences.getString("budget", "0"), 0);

        if (budget > 0) {
            totalField.setText(getString(R.string.currency,
                    BaseClass.getDoubleAsString(budget), currency));
            double used = 0.0;
            if (budget > 0) {
                used = (balance.getPending() + balance.getPaid()) * 100 / budget;
            }
            if (used > 100) {
                color = ContextCompat.getColor(this, R.color.colorPrimary);
            } else if (used > 0 && used < 100){
                color = ContextCompat.getColor(this, R.color.colorPrimaryDark);
            } else {
                color = balanceField.getCurrentTextColor();
            }
            usedField.setTextColor(color);
            usedField.setText(getString(R.string.percent, BaseClass.getDoubleAsString(used)));
        } else {
            totalField.setText(getString(R.string.checklist_summary_total_none));
            usedField.setText(getString(R.string.checklist_summary_used_none));
        }

        amountField.setText(getString(R.string.currency, balance.getAmountAsString(), currency));
        pendingField.setText(getString(R.string.currency, balance.getPendingAsString(), currency));
        paidField.setText(getString(R.string.currency, balance.getPaidAsString(), currency));
        tasksTotalField.setText(balance.getTasksTotalAsString());
        subtasksTotalField.setText(balance.getSubtasksTotalAsString());
        subtasksPendingField.setText(balance.getSubtasksPendingAsString());
        subtasksPaidField.setText(balance.getSubtasksPaidAsString());

        Double balanceValue = balance.getBalance();
        if (balanceValue < 0) {
            color = ContextCompat.getColor(this, R.color.colorPrimary);
        } else if (balanceValue > 0){
            color = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        } else {
            color = balanceField.getCurrentTextColor();
        }
        balanceField.setTextColor(color);
        balanceField.setText(getString(R.string.currency, balance.getBalanceAsString(), currency));
    }
}
