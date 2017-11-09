package com.vergiliy.wedding.checklist;

import android.support.v4.view.ViewPager;

import com.vergiliy.wedding.category.Category;
import com.vergiliy.wedding.checklist.task.TaskDatabase;

import java.util.List;

public interface ChecklistInterface {
    TaskDatabase getDbTask();
    ViewPager getViewPager();
    List<Category> getCategories();
}
