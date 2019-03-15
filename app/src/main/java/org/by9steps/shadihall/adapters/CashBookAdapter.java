package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cash_book_entry_item, null);
        return new CashBookAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final CashEntry cashEntry = mList.get(position);

        ((ItemViewHolder)viewHolder).user_name.setText(cashEntry.getUserName());
        ((ItemViewHolder)viewHolder).deb_account.setText(cashEntry.getDebitAccountName());
        ((ItemViewHolder)viewHolder).cre_account.setText(cashEntry.getCreditAccountName());
        ((ItemViewHolder)viewHolder).amount.setText(cashEntry.getAmount());
        ((ItemViewHolder)viewHolder).remarks.setText(cashEntry.getCBRemarks());
        ((ItemViewHolder)viewHolder).date.setText(cashEntry.getCBDate());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView user_name, deb_account, cre_account, amount, remarks, date;


        public ItemViewHolder(View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            cre_account = itemView.findViewById(R.id.cre_account);
            deb_account = itemView.findViewById(R.id.deb_account);
            amount = itemView.findViewById(R.id.amount);
            remarks = itemView.findViewById(R.id.remarks);
            date = itemView.findViewById(R.id.date);

        }
    }
}
