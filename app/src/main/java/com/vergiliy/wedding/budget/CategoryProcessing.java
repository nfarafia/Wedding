package com.vergiliy.wedding.budget;

import android.view.View;
import android.widget.Toast;

// Listener clicks on Edit Tabs button (edit or add new coast)
class CategoryProcessing implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), view.getContentDescription(), Toast.LENGTH_LONG).show();
    }
}