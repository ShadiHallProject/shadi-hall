package org.by9steps.shadihall.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.AddItem1RecyclerAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.item3name.Item3Name_;

import java.util.List;

public class FragmentAddItemCompleteStockGridView extends Fragment {

    private AddItem1RecyclerAdapter recyclerAdapter;
    private DatabaseHelper databaseHelper;
    private RecyclerView mRecyclerView;
    private Context mcontext;
    private List<Item3Name_> list;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_complete_stock, container, false);
        AssignIdsToViewWidget(view);

        FillRecyclerViewAdapter();

        return view;
    }

    private void AssignIdsToViewWidget(View vv) {

        databaseHelper = new DatabaseHelper(getContext());
        mRecyclerView = vv.findViewById(R.id.ItemEntry_CompleteStock_recyclerview);
    }

    private void FillRecyclerViewAdapter() {


        Prefrence prefrence=new Prefrence(getContext());
        int value =Integer.parseInt(prefrence.getClientIDSession());
        String query = "select * from Item3Name where  ClientID ='"+value+"'";

        list=databaseHelper.getItem3NameData(query);

        recyclerAdapter=new AddItem1RecyclerAdapter(getContext(),list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(recyclerAdapter);
    }
}
