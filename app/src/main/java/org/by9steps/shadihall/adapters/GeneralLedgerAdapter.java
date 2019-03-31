package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.graphics.Color;
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
            View view = inflater.inflate(R.layout.bal_sheet_group_total, null);
            return new GeneralLedgerAdapter.TotalViewHolder(view);
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
            ((ItemViewHolder)viewHolder).clientID.setText(balSheet.getClientID());
            ((ItemViewHolder)viewHolder).entryNo.setText(balSheet.getEntryNo());
            ((ItemViewHolder)viewHolder).date.setText(balSheet.getDate());
            ((ItemViewHolder)viewHolder).selectedAc.setText(balSheet.getSelectedAc());
            ((ItemViewHolder)viewHolder).againstAc.setText(balSheet.getAgainstAc());
            ((ItemViewHolder)viewHolder).accountName.setText(balSheet.getAccountName());
            ((ItemViewHolder)viewHolder).particulars.setText(balSheet.getParticulars());
            ((ItemViewHolder)viewHolder).debit.setText(balSheet.getDebit());
            ((ItemViewHolder)viewHolder).credit.setText(balSheet.getCredit());
            ((ItemViewHolder)viewHolder).entryOf.setText(balSheet.getEntryOf());
            ((ItemViewHolder)viewHolder).balance.setText(balSheet.getBalance());

        }else if (balSheet.isRow() == 2){
//            ((BalSheetDateAdapter.TotalViewHolder)viewHolder).capital.setText(balSheet.getCapital());
//            ((BalSheetDateAdapter.TotalViewHolder)viewHolder).assets.setText(balSheet.getAssets());
//            ((BalSheetDateAdapter.TotalViewHolder)viewHolder).profitLoss.setText(balSheet.getProfitLoss());
//            ((BalSheetDateAdapter.TotalViewHolder)viewHolder).liabilities.setText(balSheet.getLiabilities());
//            ((BalSheetDateAdapter.TotalViewHolder)viewHolder).cPL.setText(balSheet.getC_P_L());
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

        TextView clientID, entryNo, date, selectedAc, againstAc, accountName, particulars, debit, credit, entryOf, balance;


        public ItemViewHolder(View itemView) {
            super(itemView);

            clientID = itemView.findViewById(R.id.client_id);
            entryNo = itemView.findViewById(R.id.entry_no);
            date = itemView.findViewById(R.id.date);
            selectedAc = itemView.findViewById(R.id.selected_ac);
            againstAc = itemView.findViewById(R.id.against_ac);
            accountName = itemView.findViewById(R.id.account_name);
            particulars =itemView.findViewById(R.id.particulars);
            debit = itemView.findViewById(R.id.debit);
            credit = itemView.findViewById(R.id.credit);
            entryOf = itemView.findViewById(R.id.entry_of);
            balance = itemView.findViewById(R.id.balance);


        }
    }

    class TotalViewHolder extends RecyclerView.ViewHolder {

        TextView capital, profitLoss, liabilities, cPL, assets;


        public TotalViewHolder(View itemView) {
            super(itemView);

            capital = itemView.findViewById(R.id.capital);
            profitLoss = itemView.findViewById(R.id.profit_loss);
            liabilities = itemView.findViewById(R.id.liabilities);
            cPL = itemView.findViewById(R.id.c_p_l);
            assets = itemView.findViewById(R.id.assets);

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
