package com.vergiliy.wedding.budget.cost;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.R;

import static com.vergiliy.wedding.R.id.textView;

public class CostFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CostActivity activity = (CostActivity) getActivity();
        Cost cost = activity.getCost();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cost, container, false);

        // Get fields
        TextView nameField = view.findViewById(R.id.cost_view_name);
        TextView noteField = view.findViewById(R.id.cost_view_note);
        TextView categoryField = view.findViewById(R.id.cost_view_category);
        TextView amountField = view.findViewById(R.id.cost_view_amount);
        TextView pendingField = view.findViewById(R.id.cost_view_pending);
        TextView paidField = view.findViewById(R.id.cost_view_paid);
        TextView balanceField = view.findViewById(R.id.cost_view_balance);
        TextView updateField = view.findViewById(R.id.cost_view_update);

        nameField.setText(cost.getLocaleName());
        categoryField.setText(activity.getCategories().get(cost.getIdCategory() - 1).getLocaleName());
        noteField.setText(cost.getLocaleNote(R.string.cost_view_note_null));

        amountField.setText(cost.getAmountAsString());
        pendingField.setText(cost.getPendingAsString());
        paidField.setText(cost.getPaidAsString());

        Double balance = cost.getBalance();
        Integer color;
        if (balance < 0) {
            color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        } else if (balance > 0){
            color = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        } else {
            color = balanceField.getCurrentTextColor();
        }
        balanceField.setTextColor(color);
        balanceField.setText(cost.getBalanceAsString());

        updateField.setText(cost.getUpdateAsLocale(R.string.cost_view_update_null));

        return view;
    }
}