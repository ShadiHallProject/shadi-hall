package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.EventCashBookActivity;
import org.by9steps.shadihall.activities.GeneralLedgerActivity;
import org.by9steps.shadihall.activities.Salepur1AddNewActivity;
import org.by9steps.shadihall.chartofaccountdialog.CashBookEntryDialog;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.BalSheet;
import org.by9steps.shadihall.model.GeneralLedger;
import org.by9steps.shadihall.model.salepur1data.Salepur1;

import java.util.List;

public class GeneralLedgerAdapter extends RecyclerView.Adapter{

    private Context mCtx;
    List<GeneralLedger> mList;

    public GeneralLedgerAdapter(Context mCtx, List<GeneralLedger> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            v.findViewById(android.R.id.text1).setBackgroundColor(Color.parseColor("#f0749f"));
            return new GeneralLedgerAdapter.MonthViewHolder(v);
        }else if (viewType == 2){
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.general_ledger_list_item, null);
            return new GeneralLedgerAdapter.ItemViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.general_ledger_list_item, null);
            return new GeneralLedgerAdapter.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final GeneralLedger balSheet = mList.get(position);

        if (balSheet.isRow() == 1) {
            ((ItemViewHolder)viewHolder).refbalsheet=balSheet;
            ((ItemViewHolder)viewHolder).clientID.setText(balSheet.getEntryNo());
            ((ItemViewHolder)viewHolder).date.setText(balSheet.getDate());
            ((ItemViewHolder)viewHolder).accountName.setText(balSheet.getAccountName());
            ((ItemViewHolder)viewHolder).remarks.setText(balSheet.getParticulars());
            ((ItemViewHolder)viewHolder).debit.setText(balSheet.getDebit());
            ((ItemViewHolder)viewHolder).credit.setText(balSheet.getCredit());
            ((ItemViewHolder)viewHolder).balance.setText(balSheet.getBalance());

        }else if (balSheet.isRow() == 2){
            ((ItemViewHolder)viewHolder).clientID.setTypeface(((ItemViewHolder)viewHolder).clientID.getTypeface(), Typeface.BOLD);
            ((ItemViewHolder)viewHolder).debit.setTypeface(((ItemViewHolder)viewHolder).debit.getTypeface(), Typeface.BOLD);
            ((ItemViewHolder)viewHolder).credit.setTypeface(((ItemViewHolder)viewHolder).credit.getTypeface(), Typeface.BOLD);
            ((ItemViewHolder)viewHolder).balance.setTypeface(((ItemViewHolder)viewHolder).balance.getTypeface(), Typeface.BOLD);
            ((ItemViewHolder)viewHolder).clientID.setText(balSheet.getEntryNo());
            ((ItemViewHolder)viewHolder).date.setText(balSheet.getDate());
            ((ItemViewHolder)viewHolder).accountName.setText(balSheet.getAccountName());
            ((ItemViewHolder)viewHolder).remarks.setText(balSheet.getParticulars());
            ((ItemViewHolder)viewHolder).debit.setText(balSheet.getDebit());
            ((ItemViewHolder)viewHolder).credit.setText(balSheet.getCredit());
            ((ItemViewHolder)viewHolder).balance.setText(balSheet.getBalance());
        }else {
            GeneralLedgerAdapter.MonthViewHolder h = (GeneralLedgerAdapter.MonthViewHolder) viewHolder;
            h.textView.setText(balSheet.getMonth());
        }
        String cLID=((ItemViewHolder)viewHolder).clientID.getText().toString();
        Log.e("kkey"," Row "+balSheet.isRow()+"  "+cLID);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        GeneralLedger item = mList.get(position);
        if(item.isRow() == 0) {
            return 0;
        }else if (item.isRow() == 2){
            return 2;
        }else {
            return 1;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView clientID, date, accountName, remarks, debit, credit, balance;
        TextView editRecord,printRecord;
        GeneralLedger refbalsheet;
        public ItemViewHolder(View itemView) {
            super(itemView);
          itemView.setBackgroundColor(Color.RED);
            clientID = itemView.findViewById(R.id.client_id);
            date = itemView.findViewById(R.id.date);
            accountName = itemView.findViewById(R.id.account_name);
            remarks =itemView.findViewById(R.id.remarks);
            debit = itemView.findViewById(R.id.debit);
            credit = itemView.findViewById(R.id.credit);
            balance = itemView.findViewById(R.id.balance);
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try{
                       Log.e("EHllo ",""+refbalsheet.getEntryNo()+" --TblName::"+refbalsheet.getTablename()+" TblID::"+refbalsheet.getTableid());
                       if(refbalsheet.getTablename()!=null && refbalsheet.getTablename().contains("SalePur1")){
                           String split[]=refbalsheet.getTablename().split("_");
                           Intent i=new Intent(v.getContext(), Salepur1AddNewActivity.class);
                           Log.e("EHllo "," ------PKid"+getPkIdFromSalePur1(refbalsheet.getTableid(),split[1])+" sleas:"+split[1]);
                           i.putExtra("pkid",getPkIdFromSalePur1(refbalsheet.getTableid(),split[1]));
                           i.putExtra("salepur1id",refbalsheet.getTableid());
                           i.putExtra("EntryType",split[1]);
                           i.putExtra("edit",true);
                           v.getContext().startActivity(i);
                       }else if(refbalsheet.getTablename().equals("Null") && refbalsheet.getTableid().equals("0")){
//////////////////////////////////////////Sending To Cash Book Edit Dialog
                           Log.e("CashBookID","---"+refbalsheet.getEntryNo());
                           CashBookEntryDialog dialog = new CashBookEntryDialog();
                           Bundle bb = new Bundle();
                           bb.putString("BookingID", "0");
                           bb.putString("Spinner", "View");
                           bb.putString("EntryType",CashBookEntryDialog.entrytypelist[0]);

                           ////////////////Type either Edit or New
                           bb.putString("Type", "Edit");
                           /////////////////////if view Type is edit then must send CashBookID to update
                           bb.putString("CashBookID", refbalsheet.getEntryNo());
                           dialog.setArguments(bb);
                           try {
                               dialog.show( ((GeneralLedgerActivity)mCtx).getSupportFragmentManager(), "Default");
                           }catch (Exception e){
                               e.printStackTrace();
                           }
                       }

                   }catch (Exception e){e.printStackTrace();}
               }
           });

        }

        private int getPkIdFromSalePur1(String sleid, String s) {
            int returnid=-1;
            String qq="Select * from SalePur1 where SalePur1ID = "+sleid+" AND EntryType = '"+s+"'";
            DatabaseHelper helper=new DatabaseHelper(itemView.getContext());
           List<Salepur1>  list =refdb.SlePur1.GetSalePur1Data(helper,qq);
           if(list!=null && list.size()>0){
               return list.get(0).getID();
           }else
               return returnid;

        }


    }



    class MonthViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
    public void filterList(List<GeneralLedger> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }
}
