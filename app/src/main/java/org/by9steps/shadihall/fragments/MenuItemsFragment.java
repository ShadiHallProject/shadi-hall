package org.by9steps.shadihall.fragments;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.RecyclerViewAdapter;
import org.by9steps.shadihall.adapters.SectionViewAdapter;
import org.by9steps.shadihall.model.Menu;
import org.by9steps.shadihall.model.SectionModel;

import java.util.List;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuItemsFragment extends Fragment {

    List<Menu> mEntries, mReports;

    List<SectionModel> modelList;


    public MenuItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_item_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        modelList = new ArrayList<>();

        mEntries = new ArrayList<>();
        mEntries.add(new Menu("Booking",R.drawable.default_avatar));
        mEntries.add(new Menu("Recovery",R.drawable.default_avatar));
        mEntries.add(new Menu("Web Editing",R.drawable.default_avatar));
        mEntries.add(new Menu("Cash Book",R.drawable.default_avatar));
        mEntries.add(new Menu("ChartOfAcc",R.drawable.default_avatar));

        mReports = new ArrayList<>();
        mReports.add(new Menu("Cash Book",R.drawable.default_avatar));
        mReports.add(new Menu("Booking",R.drawable.default_avatar));
        mReports.add(new Menu("Cash and Bank",R.drawable.default_avatar));
        mReports.add(new Menu("Employee",R.drawable.default_avatar));
        mReports.add(new Menu("Expense",R.drawable.default_avatar));
        mReports.add(new Menu("Fixed Asset",R.drawable.default_avatar));
        mReports.add(new Menu("Supplier",R.drawable.default_avatar));
        mReports.add(new Menu("Client",R.drawable.default_avatar));
        mReports.add(new Menu("Revenue",R.drawable.default_avatar));
        mReports.add(new Menu("Capital",R.drawable.default_avatar));
        mReports.add(new Menu("Website",R.drawable.default_avatar));
        mReports.add(new Menu("Trail Balance",R.drawable.default_avatar));
        mReports.add(new Menu("Profit/Loss",R.drawable.default_avatar));
        mReports.add(new Menu("Bal Sheet",R.drawable.default_avatar));

        modelList.add(new SectionModel("Entries",mEntries));
        modelList.add(new SectionModel("Reports",mReports));



        SectionViewAdapter adapter = new SectionViewAdapter(getContext(),modelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

}
