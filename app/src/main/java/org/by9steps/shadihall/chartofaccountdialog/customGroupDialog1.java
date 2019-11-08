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
import android.widget.Button;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Account5customGroup1;

public class customGroupDialog1 extends DialogFragment implements View.OnClickListener {
    public static final String EDITED_MODE="Edit";
    DatabaseHelper databaseHelper;
    Prefrence prefrence;
    Button addData;
    TextInputEditText GroupName;
    String DialogMode;
    String getname;
    int id;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.customgroup_dialog, null);
        databaseHelper=new DatabaseHelper(getContext());
        prefrence=new Prefrence(getContext());
        GroupName=view.findViewById(R.id.tvGroupName);
        addData=view.findViewById(R.id.btnAddCustomGroup);addData.setOnClickListener(this);

        DialogMode=getArguments().getString("mode");


        if(DialogMode==EDITED_MODE)
        {
            getname=getArguments().getString("name");
            id=getArguments().getInt("id");
            GroupName.setText(getname);
            addData.setText("UpDate");
          return  EditData(view);
        }
        else {

            return new AlertDialog.Builder(getContext())
                    .setView(view)
                    // .setTitle("Add Vehicle")
                    .setCancelable(false)
                    .setOnDismissListener(this)
                    .create();
        }


    }


    public Dialog EditData(View myView){

        return new AlertDialog.Builder(getContext())
                .setView(myView)
                // .setTitle("Add Vehicle")
                .setCancelable(false)
                .setOnDismissListener(this)
                .create();
    }


    public void saveData(){


        if(GroupName.getText().toString().isEmpty() ) {
            MNotificationClass.ShowToast(getContext(), "Some Fields Empty");
            return;
        }


        int maxId=databaseHelper.getMaxValueOfAccount5customGroup1(prefrence.getClientIDSession());

          MNotificationClass.ShowToastTem(getContext()," maxid "+maxId);


        Account5customGroup1 account5customGroup1=new Account5customGroup1();
        account5customGroup1.setCustomGroup1ID(String.valueOf(maxId));
        account5customGroup1.setCustomGroupName(GroupName.getText().toString());
        account5customGroup1.setClientID(prefrence.getClientIDSession());
        account5customGroup1.setClientUserID(prefrence.getClientUserIDSession());
        account5customGroup1.setNetCode("0");
        account5customGroup1.setSysCode("0");
        account5customGroup1.setUpdatedDate(GenericConstants.NullFieldStandardText);





        long status=databaseHelper.createAccount5customGroup1(account5customGroup1);
        if(status!=-1){
            MNotificationClass.ShowToast(getContext(),"Data Added");
            setFieldsToEmpty();

        }else {
            MNotificationClass.ShowToast(getContext(),"Sorry Error Found..!");
        }


    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.btnAddCustomGroup && DialogMode!=EDITED_MODE){
            saveData();
        }
        else {
            if(GroupName.getText().toString().isEmpty() ) {
                MNotificationClass.ShowToast(getContext(), "Some Fields Empty");
                return;
            }
            String query = "Update Account5customGroup1 SET CustomGroupName = '" +GroupName.getText().toString() + "',UpdatedDate = '" + GenericConstants.NullFieldStandardText + "' WHERE ID = '"+ id+"'";
            databaseHelper.updateAccount5CustomGroup1DataOnLocal(query);
            MNotificationClass.ShowToast(getContext(),"Data Updated ");
            dismiss();
        }
    }
   public void  setFieldsToEmpty(){
        GroupName.setText("");
    }
}
