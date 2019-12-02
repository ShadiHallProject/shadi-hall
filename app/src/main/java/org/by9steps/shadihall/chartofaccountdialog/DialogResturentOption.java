package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.activities.ResturentAddItemActivity;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.CashBook;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DialogResturentOption extends DialogFragment implements View.OnClickListener {

    DatabaseHelper databaseHelper;
    Prefrence prefrence;

    Button buttonAddItem,buttonCashCollection,btnpartpaymentCollection,btnbillPrint;


    String tableName,billAmount,SalePur1ID,TableSatus,PortaionName,ClientID,TableID;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.dialog_resturent_option,null);

        databaseHelper=new DatabaseHelper(getContext());
        prefrence=new Prefrence(getContext());

        buttonAddItem=view.findViewById(R.id.btnAddItem);buttonAddItem.setOnClickListener(this);
        buttonCashCollection=view.findViewById(R.id.btnCashCollection);buttonCashCollection.setOnClickListener(this);
        btnpartpaymentCollection=view.findViewById(R.id.btnPartPaymentCollection);btnpartpaymentCollection.setOnClickListener(this);
        btnbillPrint=view.findViewById(R.id.btnBillPrint);btnbillPrint.setOnClickListener(this);

        tableName=getArguments().getString("TableName");
        billAmount=getArguments().getString("BillAmount");
        SalePur1ID=getArguments().getString("SalePur1ID");
        TableSatus=getArguments().getString("TableSatus");
        PortaionName=getArguments().getString("PortaionName");
        ClientID=getArguments().getString("ClientID");
        TableID=getArguments().getString("TableID");


        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Options")
                .setOnDismissListener(this)
                .setCancelable(false)
                .create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        final MediaPlayer sound = MediaPlayer.create(getContext(), R.raw.click);
        if (v.getId()== R.id.btnAddItem) {
            sound.start();
            Bundle bundle=new Bundle();
            bundle.putString("TableName",tableName);
            bundle.putString("BillAmount",billAmount);
            bundle.putString("SalePur1ID",SalePur1ID);
            bundle.putString("TableSatus",TableSatus);
            bundle.putString("PortaionName",PortaionName);
            bundle.putString("ClientID",ClientID);
            bundle.putString("TableID",TableID);
            Intent intent=new Intent(getContext(), ResturentAddItemActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            this.dismiss();
        }
        else if(v.getId()== R.id.btnCashCollection) {
            sound.start();

//            //cashbook
//            insertIntoCashBook();
//
//            //salepur1BillAmount
//            updateSalepur1BillStatus();
//
//            //updating tablestauts=Empty and salpur1ID=0 for next process......Resturent2Table
//            updateResturent2TableTableStatusAndSalpur1ID();
//
//            this.dismiss();
//
//            //update table........... will be soon


            Bundle bundle=new Bundle();
            bundle.putString("TableName",tableName);
            bundle.putString("BillAmount",billAmount);
            bundle.putString("SalePur1ID",SalePur1ID);
            bundle.putString("TableSatus",TableSatus);
            bundle.putString("PortaionName",PortaionName);
            bundle.putString("ClientID",ClientID);
            bundle.putString("TableID",TableID);
            cashCollectionDialog dialog=new cashCollectionDialog();
            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(),"TAG");
            this.dismiss();
        }
        else if(v.getId()== R.id.btnPartPaymentCollection) {

/////////////////Sending To Cash Book Edit Dialog
           // Log.e("CashBookID","---"+refbalsheet.getEntryNo());
            CashBookEntryDialog dialog = new CashBookEntryDialog();
            Bundle bb = new Bundle();
            bb.putString("BookingID", "0");
            bb.putString("Spinner", "View");
            bb.putString("EntryType","CashCollection");


            ////////////////Type either Edit or New
            bb.putString("Type", "Edit");
            /////////////////////if view Type is edit then must send CashBookID to update
            bb.putString("CashBookID", "1");
            bb.putString("tableName",tableName);
            dialog.setArguments(bb);
            try {
                dialog.show( ((MenuClickActivity)getContext()).getSupportFragmentManager(), "Default");
            }catch (Exception e){
                e.printStackTrace();
            }



        }
        else if(v.getId()== R.id.btnBillPrint){
        MNotificationClass.ShowToastTem(getContext(),"print");
        }

    }

    private void updateSalepur1BillStatus(){

        String BillStatus="Clear";
        int statusid=databaseHelper.updateSalePur1BillSatus(SalePur1ID,BillStatus,ClientID);
        if (statusid == -1)
            MNotificationClass.ShowToast(getContext(), "Data Not Updated updateSalePur1BillSatus");
        else
            MNotificationClass.ShowToast(getContext(), "Data Updated BillStatus = Clear");



    }

    //updating tablestauts for next process......Resturent2Table
    private void updateResturent2TableTableStatusAndSalpur1ID(){
        String TableStatus="Empty";
        int id=0;

      int statusid=databaseHelper.updateResturent2TableSalePur1ID(tableName,id,TableStatus,TableID);


       // int statusid=databaseHelper.updateResturent2TableTableStatus(tableName,SalePur1ID,TableStatus,ClientID,TableID);
        if (statusid == -1)
            MNotificationClass.ShowToast(getContext(), "Data Not Updated TableSatus");
        else
            MNotificationClass.ShowToast(getContext(), "Data Updated TableStatus = Empty");


    }

    //inserting data into cashBook
    private void insertIntoCashBook(){


        int maxcashbookid = refdb.CashBookTableRef.getmaxCashBookID(databaseHelper, "" + prefrence.getClientIDSession());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

        MNotificationClass.ShowToastTem(getContext(),"insertIntoCashBookid "+maxcashbookid +" date "+date);

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




}
