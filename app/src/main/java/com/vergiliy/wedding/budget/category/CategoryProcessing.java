package com.vergiliy.wedding.budget.category;

import android.view.View;
import android.widget.Toast;

// Listener clicks on Edit Tabs button (edit or add new cost)
public class CategoryProcessing implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), view.getContentDescription(), Toast.LENGTH_LONG).show();
    }
}