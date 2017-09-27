package com.vergiliy.wedding.coasts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vergiliy.wedding.R;

import java.util.List;

interface Updatable {
    void update();
}

public class CoastsFragment extends Fragment implements Updatable {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    private int pageNumber;

    public static CoastsFragment newInstance(int page) {
        CoastsFragment pageFragment = new CoastsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CoastsActivity activity = (CoastsActivity) this.getActivity();

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        TextView pageName = (TextView) view.findViewById(R.id.page_name);
        String text = getString(R.string.test_tasks_page, pageNumber);
        pageName.setText(text);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.page_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        // Get coats from database
        List<Coast> all = activity.database.getAll();
        if (all.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            RecyclerView.Adapter adapter = new CoastsRecyclerAdapter(activity, all);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(activity, R.string.coast_list_none, Toast.LENGTH_LONG).show();
        }

        return view;
    }

    // Update ViewPager fragment
    public void update() {
        Log.e("Update", "Need to update the fragment");
    }
}