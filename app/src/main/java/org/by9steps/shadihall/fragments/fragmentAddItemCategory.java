package org.by9steps.shadihall.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.chartofaccountdialog.Dialog2ForAddNewItemEntry;

public class fragmentAddItemCategory extends Fragment implements View.OnClickListener {

    ImageView AddButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_item_category,container,false);


        AddButton=view.findViewById(R.id.addItemCategory);
        AddButton.setOnClickListener(this);

        loadData();

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.addItemCategory)
        {
            Bundle bundle=new Bundle();
            bundle.putInt("keyid",0);
            bundle.putString("type","new");
            bundle.putString("item1typeid","null");
            bundle.putString("item2groupName","null");
            Dialog2ForAddNewItemEntry AddItemEntry = new Dialog2ForAddNewItemEntry();
            AddItemEntry.setArguments(bundle);
            AddItemEntry.show(getFragmentManager(), "TAG");
        }

    }
    public void loadData(){

        FragmentAddItemCategoryGridView fragment=new FragmentAddItemCategoryGridView();
        getChildFragmentManager().beginTransaction()
                .add(R.id.GridViewcontainer,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cb_menu, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (item.getItemId() == R.id.action_print) {
            //do something
        } else if (item.getItemId() == R.id.action_refresh) {
            //do something
        }
        return super.onOptionsItemSelected(item);
    }


}
