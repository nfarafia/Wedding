package com.vergiliy.wedding.budget;

import android.support.v4.view.ViewPager;

import com.vergiliy.wedding.budget.category.Category;
import com.vergiliy.wedding.budget.cost.CostDatabase;

import java.util.List;

public interface BudgetInterface {
    CostDatabase getDbMain();
    ViewPager getViewPager();
    List<Category> getCategories();
}
