package com.vergiliy.wedding.coasts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vergiliy.wedding.R;

import java.util.List;

public class CoastsFragment extends Fragment {

    static final String PAGE_NUMBER = "page_number";

    private int page;

    public static CoastsFragment newInstance(int page) {
        CoastsFragment pageFragment = new CoastsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CoastsActivity activity = (CoastsActivity) this.getActivity();

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        TextView pageName = (TextView) view.findViewById(R.id.page_name);
        String text = getString(R.string.test_tasks_page, page);
        pageName.setText(text);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.page_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        // Get coats from db_main
        int section_id = activity.getSectionIdByPosition(page);
        List<Coast> all = activity.getDbMain().getAllBySectionId(section_id);
        if (all.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            RecyclerView.Adapter adapter = new CoastsRecyclerAdapter(activity, all);
            recyclerView.setAdapter(adapter);
        } else {
            // recyclerView.setVisibility(View.GONE);
            Toast.makeText(activity, R.string.coast_list_none, Toast.LENGTH_LONG).show();
        }

        return view;
    }
}