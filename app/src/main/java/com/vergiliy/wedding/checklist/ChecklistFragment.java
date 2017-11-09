package com.vergiliy.wedding.checklist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.R;
import com.vergiliy.wedding.checklist.task.Task;

import java.util.List;

public class ChecklistFragment extends Fragment {

    static final String PAGE_NUMBER = "page_number";

    private int page;

    public static ChecklistFragment newInstance(int page) {
        ChecklistFragment pageFragment = new ChecklistFragment();
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
        ChecklistActivity activity = (ChecklistActivity) this.getActivity();

        View view = inflater.inflate(R.layout.fragment_checklist, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.checklist_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        // Get tasks from db_task
        int id_category = activity.getCategoryIdByPosition(page);
        List<Task> all = activity.getDbTask().getAllByIdCategory(id_category);
        if (all.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            RecyclerView.Adapter adapter = new ChecklistRecyclerAdapter(activity, all);
            recyclerView.setAdapter(adapter);
        } else {
            TextView noneText = view.findViewById(R.id.checklist_list_none);
            recyclerView.setVisibility(View.GONE);
            noneText.setVisibility(View.VISIBLE);
        }

        return view;
    }
}