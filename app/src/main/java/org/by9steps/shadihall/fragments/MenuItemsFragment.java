package org.by9steps.shadihall.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.RecyclerViewAdapter;
import org.by9steps.shadihall.model.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuItemsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    Menu menu;

    String title[] = {
            "Booking",
            "Recovery",
            "Web Editing",
            "Cash Book",
            "ChartOfAcc"
    };

    int thumbnails[] = {
            R.drawable.default_avatar,
            R.drawable.default_avatar,
            R.drawable.default_avatar,
            R.drawable.default_avatar,
            R.drawable.default_avatar
    };

    public MenuItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_items_list, container, false);

        for (int i = 0; i< title.length; i++) {
            menu = new Menu(title[i], thumbnails[i]);
        }
        return view;
    }


}
