package com.vergiliy.wedding.checklist.subtask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.R;
import com.vergiliy.wedding.checklist.task.TaskActivity;

import java.util.List;

public class SubtaskFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TaskActivity activity = (TaskActivity) getActivity();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subtasks, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.subtasks_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        List<Subtask> all = activity.getSubtasks();
        TextView noneText = view.findViewById(R.id.subtasks_list_none);
        if (all.size() > 0) {
            RecyclerView.Adapter adapter = new SubtaskRecyclerAdapter(activity, all);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            noneText.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noneText.setVisibility(View.VISIBLE);
        }

        return view;
    }
}