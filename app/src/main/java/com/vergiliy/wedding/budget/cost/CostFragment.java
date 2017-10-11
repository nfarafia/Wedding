package com.vergiliy.wedding.budget.cost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.R;

public class CostFragment extends Fragment {

    private CostActivity activity;
    private Cost cost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (CostActivity) getActivity();
        cost = activity.getCost();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cost, container, false);

        // Get fields
        TextView nameField = view.findViewById(R.id.cost_view_name);
        TextView categoryField = view.findViewById(R.id.cost_view_category);
        TextView noteField = view.findViewById(R.id.cost_view_note);
        TextView amountField = view.findViewById(R.id.cost_view_amount);
        TextView completeField = view.findViewById(R.id.cost_view_complete);
        TextView updateField = view.findViewById(R.id.cost_view_update);

        nameField.setText(cost.getLocaleName());
        categoryField.setText(activity.getCategories().get(cost.getIdCategory()).getLocaleName());
        noteField.setText(cost.getNote(R.string.cost_view_note_null));
        amountField.setText(cost.getAmountAsString());
        completeField.setText(cost.getCompleteAsString());
        updateField.setText(cost.getUpdateAsString());

        return view;
    }
}