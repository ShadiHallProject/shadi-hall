package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.BalSheet;
import org.by9steps.shadihall.model.GeneralLedger;

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


        public ItemViewHolder(View itemView) {
            super(itemView);

            clientID = itemView.findViewById(R.id.client_id);
            date = itemView.findViewById(R.id.date);
            accountName = itemView.findViewById(R.id.account_name);
            remarks =itemView.findViewById(R.id.remarks);
            debit = itemView.findViewById(R.id.debit);
            credit = itemView.findViewById(R.id.credit);
            balance = itemView.findViewById(R.id.balance);


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
