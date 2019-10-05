package org.by9steps.shadihall.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.Salepur1AddNewActivity;
import org.by9steps.shadihall.fragments.salepurviewtypes.salepurgridviewfrag;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.MNotificationClass;

import java.util.ArrayList;
import java.util.List;

public class SalePur1Fragment extends Fragment implements AdapterView.OnItemSelectedListener ,
        View.OnClickListener {

    private DatabaseHelper databaseHelper;
    ///////////////Fragment View Related Compnents
    private Spinner spinner;
    private SearchView searchView;
    private ImageView addnew;
    private String EntryType=null;

   Button viewgrid,viewtree,other;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sale_pur1, container, false);
        AssignIdsToViewWidget(view);
        SetItemOnSpinner();
        spinner.setOnItemSelectedListener(this);
        EntryType=getArguments().getString("EntryType");
        return view;
    }

    private void AssignIdsToViewWidget(View vv) {

        databaseHelper=new DatabaseHelper(getContext());


        spinner = vv.findViewById(R.id.salpur_spinner);
        addnew = vv.findViewById(R.id.salpur_add);
        addnew.setOnClickListener(this);
        searchView = vv.findViewById(R.id.salpur_search);



        viewgrid=vv.findViewById(R.id.btnhorizontal1);viewgrid.setOnClickListener(this);
        viewtree=vv.findViewById(R.id.btnhorizontal2);viewtree.setOnClickListener(this);
        other=vv.findViewById(R.id.btnhorizontal3);other.setOnClickListener(this);




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
//            try {
//                createPdf();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
//            if (isConnected()) {
//                refereshTables(getContext());
//            } else {
//                Toast.makeText(getContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MNotificationClass.ShowToastTem(getContext(),position+"");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(R.id.salpur_add==v.getId())
        {
            Intent intent=new Intent(getContext(), Salepur1AddNewActivity.class);
          intent.putExtra("EntryType",EntryType);
            startActivity(intent);
        }
        switch (v.getId())
        {
            case R.id.btnhorizontal1:
                MNotificationClass.ShowToastTem(getContext(),"Grid View ");
                Bundle bundle=new Bundle();
                bundle.putString("EntryType",EntryType);
                salepurgridviewfrag salepurgridviewfrag=new salepurgridviewfrag();
                salepurgridviewfrag.setArguments(bundle);
                getChildFragmentManager().beginTransaction()
                        .add(R.id.containersalepur1, salepurgridviewfrag)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.btnhorizontal2:
                MNotificationClass.ShowToastTem(getContext(),"Tree View ");

                break;
            case R.id.btnhorizontal3:
                MNotificationClass.ShowToastTem(getContext(),"other View ");

                break;
        }
    }
    //////////////////////SetItemOnSpinner
    public void SetItemOnSpinner(){
        List<String> spinner_list = new ArrayList<String>();
        spinner_list.add("Select");
        spinner_list.add("No");
        spinner_list.add("Date");
        spinner_list.add("AccountName");
        spinner_list.add("Remarks");
        spinner_list.add("BillAmount");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinner_list);
            spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseHelper.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}
