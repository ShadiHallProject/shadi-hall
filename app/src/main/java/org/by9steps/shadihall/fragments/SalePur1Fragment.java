package org.by9steps.shadihall.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.Salepur1AddNewActivity;
import org.by9steps.shadihall.adapters.SalePur1RecyclerAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.model.SalePur1Fragmentmodel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SalePur1Fragment extends Fragment implements AdapterView.OnItemSelectedListener ,
        View.OnClickListener {

    private DatabaseHelper databaseHelper;
    ///////////////Fragment View Related Compnents
    private Spinner spinner;
    private SearchView searchView;
    private ImageView addnew;
    private RecyclerView mrecyclerview;
    private SalePur1RecyclerAdapter recyclerAdapter;
    //////////////TAble Column
   private TextView c1num, c2date, c3acname, c4remarks, c5billamount;
    ////////////////////////////////////////////////////Comp End


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
        /////////////////////////////Sample TEsting
        List<SalePur1Fragmentmodel> list=new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new SalePur1Fragmentmodel("col1"+i,
                    "col2","col3",
                    "col4","col5"));
        }
        recyclerAdapter=new SalePur1RecyclerAdapter(getContext(),list);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mrecyclerview.setAdapter(recyclerAdapter);
        ///////////////////////////////////////////////
        return view;
    }

    private void AssignIdsToViewWidget(View vv) {

        databaseHelper=new DatabaseHelper(getContext());
        //////////////TAble Column
        TextView c1num, c2date, c3acname, c4remarks, c5billamount;

        spinner = vv.findViewById(R.id.salpur_spinner);
        addnew = vv.findViewById(R.id.salpur_add);
        addnew.setOnClickListener(this);
        searchView = vv.findViewById(R.id.salpur_search);

        c1num = vv.findViewById(R.id.col1no);
        c2date = vv.findViewById(R.id.col2date);
        c3acname = vv.findViewById(R.id.col3acname);
        c4remarks = vv.findViewById(R.id.col4remark);
        c5billamount = vv.findViewById(R.id.col5billamnt);

        mrecyclerview=vv.findViewById(R.id.salepurrecyclerview);
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
            startActivity(new Intent(getContext(), Salepur1AddNewActivity.class));
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
