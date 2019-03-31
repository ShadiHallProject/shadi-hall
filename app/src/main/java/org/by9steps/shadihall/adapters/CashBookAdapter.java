package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.CashEntry;
import org.by9steps.shadihall.model.ProfitLoss;
import org.by9steps.shadihall.model.Recovery;

import java.util.List;

public class CashBookAdapter extends RecyclerView.Adapter{

    private Context mCtx;
    List<CashEntry> mList;

    public CashBookAdapter(Context mCtx, List<CashEntry> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
        Log.e("LIST","sss");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            v.findViewById(android.R.id.text1).setBackgroundColor(Color.parseColor("#f0749f"));
            return new CashBookAdapter.MonthViewHolder(v);
        }else if (viewType == 2){
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.cash_book_group_total, null);
            return new CashBookAdapter.TotalViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.cash_book_entry_item, null);
            return new CashBookAdapter.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final CashEntry cashEntry = mList.get(position);

        if (cashEntry.isRow() == 1) {
            ((ItemViewHolder) viewHolder).cashBookID.setText(cashEntry.getCashBookID());
            ((ItemViewHolder) viewHolder).deb_account.setText(cashEntry.getDebitAccountName());
            ((ItemViewHolder) viewHolder).cre_account.setText(cashEntry.getCreditAccountName());
            ((ItemViewHolder) viewHolder).amount.setText(cashEntry.getAmount());
            ((ItemViewHolder) viewHolder).remarks.setText(cashEntry.getCBRemarks());
            ((ItemViewHolder) viewHolder).date.setText(cashEntry.getCBDate());
        }else if (cashEntry.isRow() == 2) {
            ((TotalViewHolder) viewHolder).amount.setText(cashEntry.getAmount());
        }else {
            CashBookAdapter.MonthViewHolder h = (CashBookAdapter.MonthViewHolder) viewHolder;
            h.textView.setText(cashEntry.getMonth());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        CashEntry item = mList.get(position);
        if(item.isRow() == 0) {
            return 0;
        }else if (item.isRow() == 2){
            return 2;
        }else {
            return 1;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView cashBookID, deb_account, cre_account, amount, remarks, date;


        public ItemViewHolder(View itemView) {
            super(itemView);

            cashBookID = itemView.findViewById(R.id.cashBookID);
            cre_account = itemView.findViewById(R.id.cre_account);
            deb_account = itemView.findViewById(R.id.deb_account);
            amount = itemView.findViewById(R.id.amount);
            remarks = itemView.findViewById(R.id.remarks);
            date = itemView.findViewById(R.id.date);

        }
    }

    class TotalViewHolder extends RecyclerView.ViewHolder {

        TextView amount, total;


        public TotalViewHolder(View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            total = itemView.findViewById(R.id.total);

        }
    }

    class MonthViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
