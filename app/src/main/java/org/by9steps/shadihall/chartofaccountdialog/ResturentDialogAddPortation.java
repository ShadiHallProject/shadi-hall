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
import org.by9steps.shadihall.model.Restaurant1Potion;
import org.by9steps.shadihall.model.Vehicle2Name;

public class ResturentDialogAddPortation extends DialogFragment implements View.OnClickListener{
    public static final String DIALOG_MODE="Edit";
    String type;
    int key;

    TextInputEditText portationName;
    Button buttonAdd,buttonCancel;
    DatabaseHelper databaseHelper;
    Prefrence prefrence;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.resturent_dialog_add_portaion,null);
        portationName=view.findViewById(R.id.tietPortaionName);
        buttonAdd=view.findViewById(R.id.btnResturentPortationAdd);buttonAdd.setOnClickListener(this);
        buttonCancel=view.findViewById(R.id.btnResturentPortationCancel);buttonCancel.setOnClickListener(this);
        databaseHelper=new DatabaseHelper(getContext());
        prefrence=new Prefrence(getContext());

        type=getArguments().getString("type");
        if(type.equals(DIALOG_MODE))
        {
            key=getArguments().getInt("key");
           return EditModeDialog(view);
        }else{

            return new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setTitle("Add Portation")
                    .setCancelable(false)
                    .setOnDismissListener(this)
                    .create();
        }


    }

    public Dialog EditModeDialog(View view){
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("update Portation")
                .setCancelable(false)
                .setOnDismissListener(this)
                .create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnResturentPortationAdd && type.equals(DIALOG_MODE)){
            updateResturentPoration();
        }
        else if(v.getId()==R.id.btnResturentPortationAdd && type!=DIALOG_MODE){
            saveResturentPoration();
        }
        else if(v.getId()==R.id.btnResturentPortationCancel){
            this.dismiss();
        }
    }

    private void saveResturentPoration(){


        if(portationName.getText().toString().isEmpty()) {
            MNotificationClass.ShowToast(getContext(), "Some Fields Empty");
            return;
        }
        int maxId=databaseHelper.getMaxValueOfRestaurant1Potion(prefrence.getClientIDSession());
              MNotificationClass.ShowToastTem(getContext()," maxid "+maxId);


        Restaurant1Potion restaurant1Potion=new Restaurant1Potion();
        restaurant1Potion.setPotionID(String.valueOf(maxId));
        restaurant1Potion.setPotionName(portationName.getText().toString());
        restaurant1Potion.setPotionDescriptions("abc");
        restaurant1Potion.setClientID(prefrence.getClientIDSession());
        restaurant1Potion.setClientUserID(prefrence.getClientUserIDSession());
        restaurant1Potion.setNetCode("0");
        restaurant1Potion.setSysCode("0");
        restaurant1Potion.setUpdatedDate(GenericConstants.NullFieldStandardText);


        long status=databaseHelper.createRestaurant1Potion(restaurant1Potion);
        if(status!=-1){
            MNotificationClass.ShowToast(getContext(),"Data Added");
            portationName.setText("");
        }else {
            MNotificationClass.ShowToast(getContext(),"Sorry Error Found..!");
        }

    }

    private void updateResturentPoration(){

        if(portationName.getText().toString().isEmpty()) {
            MNotificationClass.ShowToast(getContext(), "Some Fields Empty");
            return;
        }
        long id=databaseHelper.updateResturent1Portation(key,portationName.getText().toString());

        if (id == -1){
           // MNotificationClass.ShowToast(getContext(), "Data Not Updated SalePur1ID Resturent2");
            }
        else {
            MNotificationClass.ShowToast(getContext(), "Data Updated Resturent1Portation");
            ResturentDialogAddTable obj=new ResturentDialogAddTable();
            obj.show(getFragmentManager(),"TAG");
            this.dismiss();
        }


    }

}
