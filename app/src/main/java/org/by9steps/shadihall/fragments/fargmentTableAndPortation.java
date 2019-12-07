package org.by9steps.shadihall.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.chartofaccountdialog.ResturentDialogAddPortation;
import org.by9steps.shadihall.chartofaccountdialog.ResturentDialogAddTable;
import org.by9steps.shadihall.chartofaccountdialog.VehicleBookingDialog;
import org.by9steps.shadihall.genericgrid.MediatorClass;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;

public class fargmentTableAndPortation extends Fragment implements View.OnClickListener{
    DatabaseHelper databaseHelper;
    Prefrence prefrence;
    Button buttonPortaion,buttonTable;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_table_portation,container,false);
        buttonPortaion=view.findViewById(R.id.btnaddPortation);buttonPortaion.setOnClickListener(this);
        buttonTable=view.findViewById(R.id.btnaddTable);buttonTable.setOnClickListener(this);
        recyclerView=view.findViewById(R.id.recyclerView1);

        prefrence=new Prefrence(getContext());
        databaseHelper=new DatabaseHelper(getContext());
        fillGridView();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnaddPortation){

            Bundle bundle=new Bundle();
            bundle.putString("type","New");
            ResturentDialogAddPortation obj=new ResturentDialogAddPortation();
            obj.setArguments(bundle);
            obj.show(getFragmentManager(),"TAG");

        }else if(v.getId()==R.id.btnaddTable){

            ResturentDialogAddTable obj=new ResturentDialogAddTable();;
            obj.show(getFragmentManager(),"TABLE");

        }
    }

    public void fillGridView(){



        String query="Select * From Restaurant2Table where ClientID = '"+prefrence.getClientIDSession()+"'";

        Cursor cc=databaseHelper.getReadableDatabase().rawQuery(query,null);
        Log.e("aaaaaa",""+cc.getCount());
        final MediatorClass mediatorClass=new MediatorClass(cc,recyclerView);
        mediatorClass.setSortingAllowed(true);
        mediatorClass.ShowGrid();

    }

    @Override
    public void onResume() {
        MNotificationClass.ShowToastTem(getContext(),"onResume");

        super.onResume();





    }

    public void updatefun(){
        String query="Select * From Restaurant2Table where ClientID = '"+prefrence.getClientIDSession()+"'";
        Cursor cc=databaseHelper.getReadableDatabase().rawQuery(query,null);
        Log.e("aaaaaa",""+cc.getCount());
        final MediatorClass mediatorClass=new MediatorClass(cc,recyclerView);
        mediatorClass.setSortingAllowed(true);
        mediatorClass.ShowGrid();
    }
}
