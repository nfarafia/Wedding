package com.vergiliy.wedding.budget.payment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.R;
import com.vergiliy.wedding.budget.cost.CostActivity;

import java.util.List;

public class PaymentsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CostActivity activity = (CostActivity) getActivity();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payments, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.payments_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        List<Payment> all = activity.getPayments();
        if (all.size() > 0) {
            RecyclerView.Adapter adapter = new PaymentsRecyclerAdapter(activity, all);
            recyclerView.setAdapter(adapter);
        } else {
            TextView noneText = view.findViewById(R.id.payments_list_none);

            recyclerView.setVisibility(View.GONE);
            noneText.setVisibility(View.VISIBLE);
        }

        return view;
    }
}