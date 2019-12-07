package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.adapters.SpinnerAdapterResturent;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Restaurant1Potion;
import org.by9steps.shadihall.model.Restaurant2Table;

import java.util.List;

public class ResturentDialogAddTable extends DialogFragment implements View.OnClickListener{

    TextInputEditText tableName;
    Spinner spinner;
    Button buttonAdd,buttonCancel;
    DatabaseHelper databaseHelper;
    Prefrence prefrence;
    List<Restaurant1Potion> spinnerlist;
    String [] spData;
    int [] spIDs;
    int itemselectindex;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.resturent_dialog_add_table,null);

        prefrence=new Prefrence(getContext());
        databaseHelper=new DatabaseHelper(getContext());

        spinner=view.findViewById(R.id.spPortation);
        tableName=view.findViewById(R.id.tietTableName);
        buttonAdd=view.findViewById(R.id.btnResturentTableAdd);buttonAdd.setOnClickListener(this);
        buttonCancel=view.findViewById(R.id.btnResturentTableCancel);buttonCancel.setOnClickListener(this);



//        Restaurant1Potion


        //spinner Vehicle Category data
        String query="select * from Restaurant1Potion Where ClientID = '"+prefrence.getClientIDSession()+"'";
        spinnerlist=databaseHelper.getRestaurant1Potion(query);
        spData=new String[spinnerlist.size()];
        spIDs=new int[spinnerlist.size()];
        for (int i = 0; i < spinnerlist.size(); i++) {
            spData[i]=spinnerlist.get(i).getPotionName();
            spIDs[i]=spinnerlist.get(i).getID();
        }
      //  ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,spData);
        SpinnerAdapterResturent arrayAdapter=new SpinnerAdapterResturent(getContext(),spData,spIDs);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemselectindex=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add Table")
                .setCancelable(false)
                .setOnDismissListener(this)
                .create();
    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnResturentTableCancel){
            this.dismiss();

        }else if(v.getId()==R.id.btnResturentTableAdd){

            saveResturent2Table();
        }
    }

    private void saveResturent2Table(){


        if(tableName.getText().toString().isEmpty()) {
            MNotificationClass.ShowToast(getContext(), "Some Fields Empty");
            return;
        }
        int maxId=databaseHelper.getMaxValueOfRestaurant2Table(prefrence.getClientIDSession());
        MNotificationClass.ShowToastTem(getContext()," maxid "+maxId);


        Restaurant2Table resTable=new Restaurant2Table();
        resTable.setTableID(String.valueOf(maxId));
        resTable.setPotionID(spinnerlist.get(itemselectindex).getPotionID());
        resTable.setTabelName(tableName.getText().toString());
        resTable.setTableDescription("abc");
        resTable.setTableStatus("Empty");
        resTable.setClientID(prefrence.getClientIDSession());
        resTable.setClientUserID(prefrence.getClientUserIDSession());
        resTable.setNetCode("0");
        resTable.setSysCode("0");
        resTable.setUpdatedDate(GenericConstants.NullFieldStandardText);
        resTable.setSalPur1ID("0");

        long status=databaseHelper.createRestaurant2Table(resTable);
        if(status!=-1){
            MNotificationClass.ShowToast(getContext(),"Data Added");
            tableName.setText("");
            MenuClickActivity activity=(MenuClickActivity)getContext();
            activity.updateTableGrid();
        }else {
            MNotificationClass.ShowToast(getContext(),"Sorry Error Found..!");
        }

    }
}
