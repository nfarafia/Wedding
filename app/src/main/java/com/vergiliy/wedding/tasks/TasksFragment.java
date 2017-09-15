package com.vergiliy.wedding.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vergiliy.wedding.R;

public class TasksFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    private final String data[] = new String[] {
            "one one one one one one one one one",
            "two two two two two two two two two two two two",
            "three three three three three three three three",
            "four four four four four four four",
            "five five five five five five five five",
            "six six six six six six six six six six six six six six six six six six",
            "seven seven seven seven seven seven seven seven seven seven",
            "eight eight eight eight eight eight eight",
            "nine nine nine nine nine nine nine nine nine nine",
            "ten ten ten ten ten ten ten ten ten ten",
            "eleven eleven eleven eleven eleven eleven eleven eleven eleven eleven eleven",
            "twelve twelve twelve twelve twelve twelve twelve twelve twelve",
            "thirteen thirteen thirteen thirteen thirteen thirteen",
            "fourteen fourteen fourteen fourteen fourteen fourteen",
            "fifteen fifteen fifteen fifteen fifteen",
            "sixteen sixteen sixteen sixteen"};

    private int pageNumber;

    public static TasksFragment newInstance(int page) {
        TasksFragment pageFragment = new TasksFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        TextView pageName = (TextView) view.findViewById(R.id.page_name);
        String text = getString(R.string.test_tasks_page, pageNumber);
        pageName.setText(text);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.page_list);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new TasksRecyclerAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }
}