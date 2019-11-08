package org.by9steps.shadihall.fragments;

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
import org.by9steps.shadihall.adapters.AddItemCategoryRecyclerAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Item2Group;

import java.util.List;

public class FragmentAddItemCategoryGridView extends Fragment {

    private AddItemCategoryRecyclerAdapter recyclerAdapter;
    private DatabaseHelper databaseHelper;
    private RecyclerView mRecyclerView;
    private List<Item2Group> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_item_catogery_gridview,container,false);

        fillIds(view);
        FillRecyclerViewAdapter();
        return  view;
    }
    private void fillIds(View myView){
        databaseHelper =new DatabaseHelper(getContext());
        mRecyclerView=myView.findViewById(R.id.AddItemCategoryRecyclerView);
    }
    private void FillRecyclerViewAdapter(){
        Prefrence prefrence=new Prefrence(getContext());
        int value =Integer.parseInt(prefrence.getClientIDSession());


        String query="select * from Item2Group where  ClientID = '"+value+"'";
        list=databaseHelper.getItem2GroupData(query);
        recyclerAdapter=new AddItemCategoryRecyclerAdapter(getContext(),list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(recyclerAdapter);
    }
}
