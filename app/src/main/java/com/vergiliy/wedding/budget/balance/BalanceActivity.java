package com.vergiliy.wedding.budget.balance;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.vergiliy.wedding.BaseActivity;
import com.vergiliy.wedding.BaseClass;
import com.vergiliy.wedding.R;
import com.vergiliy.wedding.budget.category.Category;
import com.vergiliy.wedding.budget.category.CategoryDatabase;
import com.vergiliy.wedding.budget.cost.CostDatabase;

import java.util.List;

import static com.vergiliy.wedding.R.string.count;

public class BalanceActivity extends BaseActivity {

    private CostDatabase db_cost;
    private CategoryDatabase db_category;

    TextView totalField, usedField, amountField, pendingField, paidField, balanceField,
            costsTotalField, paymentsTotalField, paymentsPendingField, paymentsPaidField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_budget_balance_title);
        setContentView(R.layout.activity_budget_balance);
        // Change activities with animation
        overridePendingTransition(R.anim.create_slide_in, R.anim.create_slide_out);

        db_cost = new CostDatabase(this);
        db_category = new CategoryDatabase(this);

        // Get fields
        final Spinner categoryField = (Spinner) findViewById(R.id.budget_balance_category);
        totalField = (TextView) findViewById(R.id.budget_balance_total);
        usedField = (TextView) findViewById(R.id.budget_balance_used);
        amountField = (TextView) findViewById(R.id.budget_balance_amount);
        pendingField = (TextView) findViewById(R.id.budget_balance_pending);
        paidField = (TextView) findViewById(R.id.budget_balance_paid);
        balanceField = (TextView) findViewById(R.id.budget_balance_balance);
        costsTotalField = (TextView) findViewById(R.id.budget_balance_total_costs);
        paymentsTotalField = (TextView) findViewById(R.id.budget_balance_total_payments);
        paymentsPendingField = (TextView) findViewById(R.id.budget_balance_total_payments_pending);
        paymentsPaidField = (TextView) findViewById(R.id.budget_balance_total_payments_paid);

        // Create default item (all category)
        Category item = new Category(this);
        item.setName(BaseClass.LANGUAGE_DEFAULT, R.string.budget_balance_category_all);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Return to previous activity; call method onBackPressed
            case android.R.id.home:
                onBackPressed();
                return true;
            // If another id, call super method
            default:
                return super.onOptionsItemSelected(item);
        }
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
        if (db_cost != null) {
            db_cost.close();
        }
        if (db_category != null) {
            db_category.close();
        }
    }

    private void updateBalance(Integer id_category) {
        Integer color;

        Balance balance = db_cost.getBalance(id_category);

        Double total = 5123.45;
        totalField.setText(balance.getDoubleAsString(total));

        Double used = (balance.getPending() + balance.getPaid()) * 100 / total;
        if (used < 0) {
            color = ContextCompat.getColor(this, R.color.colorPrimary);
        } else if (used > 0){
            color = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        } else {
            color = balanceField.getCurrentTextColor();
        }
        usedField.setTextColor(color);
        usedField.setText(getString(R.string.percent, balance.getDoubleAsString(used)));

        amountField.setText(balance.getAmountAsString());
        pendingField.setText(balance.getPendingAsString());
        paidField.setText(balance.getPaidAsString());
        costsTotalField.setText(balance.getCoatsTotalAsString());
        paymentsTotalField.setText(balance.getPaymentsTotalAsString());
        paymentsPendingField.setText(balance.getPaymentsPendingAsString());
        paymentsPaidField.setText(balance.getPaymentsPaidAsString());

        Double balanceValue = balance.getBalance();
        if (balanceValue < 0) {
            color = ContextCompat.getColor(this, R.color.colorPrimary);
        } else if (balanceValue > 0){
            color = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        } else {
            color = balanceField.getCurrentTextColor();
        }
        balanceField.setTextColor(color);
        balanceField.setText(balance.getBalanceAsString());
    }
}
