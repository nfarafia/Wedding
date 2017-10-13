package com.vergiliy.wedding.budget.cost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.R;

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
        TextView categoryField = view.findViewById(R.id.cost_view_category);
        TextView noteField = view.findViewById(R.id.cost_view_note);
        TextView amountField = view.findViewById(R.id.cost_view_amount);
        TextView updateField = view.findViewById(R.id.cost_view_update);

        nameField.setText(cost.getLocaleName());
        categoryField.setText(activity.getCategories().get(cost.getIdCategory()).getLocaleName());
        noteField.setText(cost.getLocaleNote(R.string.cost_view_note_null));
        amountField.setText(cost.getAmountAsString());
        updateField.setText(cost.getUpdateAsLocale(R.string.cost_view_update_null));

        return view;
    }
}