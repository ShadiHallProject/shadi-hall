package org.by9steps.shadihall.chartofaccountdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.CashBook;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class cashCollectionDialog extends DialogFragment implements View.OnClickListener {
    Button btnAdd,btnCancel;
    DatabaseHelper databaseHelper;
    Prefrence prefrence;

    String tableName,billAmount,SalePur1ID,TableSatus,PortaionName,ClientID,TableID;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.cash_collection_dialog,null);
        btnAdd=view.findViewById(R.id.btnCollectionYes);btnAdd.setOnClickListener(this);
        btnCancel=view.findViewById(R.id.btnCollectionCancel);btnCancel.setOnClickListener(this);

        databaseHelper=new DatabaseHelper(getContext());
        prefrence=new Prefrence(getContext());

        tableName=getArguments().getString("TableName");
        billAmount=getArguments().getString("BillAmount");
        SalePur1ID=getArguments().getString("SalePur1ID");
        TableSatus=getArguments().getString("TableSatus");
        PortaionName=getArguments().getString("PortaionName");
        ClientID=getArguments().getString("ClientID");
        TableID=getArguments().getString("TableID");

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setCancelable(false)
                .setOnDismissListener(this)
                .create();
    }

    @Override
    public void onClick(View v) {
        final MediaPlayer sound = MediaPlayer.create(getContext(), R.raw.click);

        if(v.getId()== R.id.btnCollectionCancel)
            this.dismiss();
        if(v.getId()== R.id.btnCollectionYes){
            sound.start();


            //cashbook
            insertIntoCashBook();

            //salepur1BillAmount
            updateSalepur1BillStatus();

            //updating tablestauts=Empty and salpur1ID=0 for next process......Resturent2Table
            updateResturent2TableTableStatusAndSalpur1ID();


            //update table...........FragmentResturent
            if(PortaionName.equals("CashCollection")){
                MenuClickActivity activity = (MenuClickActivity) getContext();
                activity.updateCashcollectionRecyclerView();
            }else {
                MenuClickActivity activity = (MenuClickActivity) getContext();
                activity.updateTableRecyclerView();
            }


            this.dismiss();



           // MNotificationClass.ShowToastTem(getContext(),"click");
        }
    }

    //inserting data into cashBook
    private void insertIntoCashBook(){


        int maxcashbookid = refdb.CashBookTableRef.getmaxCashBookID(databaseHelper, "" + prefrence.getClientIDSession());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

      //  MNotificationClass.ShowToastTem(getContext(),"insertIntoCashBookid "+maxcashbookid +" date "+date);

        databaseHelper.createCashBook(new CashBook(maxcashbookid+"",
                date,  //CBDAte
                prefrence.getClientIDSession(), //DebitAccount
                "8",
                tableName,   //remarks
                billAmount, //Amount
                prefrence.getClientIDSession(),//ClientID
                prefrence.getClientUserIDSession(),//ClientUSerID
                "0",
                "0",
                GenericConstants.NullFieldStandardText,
                SalePur1ID,//TableID
                String.valueOf(maxcashbookid), //serialNo
                "SalPur1_Sales")); //
    }

    private void updateSalepur1BillStatus(){

        String BillStatus="Clear";
        int statusid=databaseHelper.updateSalePur1BillSatus(SalePur1ID,BillStatus,ClientID);
        if (statusid == -1)
            MNotificationClass.ShowToast(getContext(), "Data Not Updated updateSalePur1BillSatus");
        else {
         //   MNotificationClass.ShowToast(getContext(), "Data Updated BillStatus = Clear");
        }
    }

    //updating tablestauts for next process......Resturent2Table
    private void updateResturent2TableTableStatusAndSalpur1ID(){
        String TableStatus="Empty";
        int id=0;

        int statusid=databaseHelper.updateResturent2TableSalePur1ID(tableName,id,TableStatus,TableID);


        // int statusid=databaseHelper.updateResturent2TableTableStatus(tableName,SalePur1ID,TableStatus,ClientID,TableID);
        if (statusid == -1)
            MNotificationClass.ShowToast(getContext(), "Data Not Updated TableSatus");
        else {
         //   MNotificationClass.ShowToast(getContext(), "Data Updated TableStatus = Empty");
        }

    }
}
