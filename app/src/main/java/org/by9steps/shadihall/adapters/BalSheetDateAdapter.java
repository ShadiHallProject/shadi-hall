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
import org.by9steps.shadihall.model.ProfitLoss;
import org.by9steps.shadihall.model.Recovery;

import java.util.List;

public class BalSheetDateAdapter extends RecyclerView.Adapter{

    private Context mCtx;
    List<BalSheet> mList;

    public BalSheetDateAdapter(Context mCtx, List<BalSheet> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            v.findViewById(android.R.id.text1).setBackgroundColor(Color.parseColor("#f0749f"));
            return new BalSheetDateAdapter.MonthViewHolder(v);
        }else if (viewType == 2){
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.bal_sheet_group_total, null);
            return new BalSheetDateAdapter.TotalViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.bal_sheet_list_item, null);
            return new BalSheetDateAdapter.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final BalSheet balSheet = mList.get(position);

        if (balSheet.isRow() == 1) {
            ((ItemViewHolder)viewHolder).date.setText(balSheet.getCBDate());
            ((ItemViewHolder)viewHolder).capital.setText(balSheet.getCapital());
            ((ItemViewHolder)viewHolder).assets.setText(balSheet.getAssets());
            ((ItemViewHolder)viewHolder).profitLoss.setText(balSheet.getProfitLoss());
            ((ItemViewHolder)viewHolder).liabilities.setText(balSheet.getLiabilities());
            ((ItemViewHolder)viewHolder).cPL.setText(balSheet.getC_P_L());

        }else if (balSheet.isRow() == 2){
            ((TotalViewHolder)viewHolder).capital.setText(balSheet.getCapital());
            ((TotalViewHolder)viewHolder).assets.setText(balSheet.getAssets());
            ((TotalViewHolder)viewHolder).profitLoss.setText(balSheet.getProfitLoss());
            ((TotalViewHolder)viewHolder).liabilities.setText(balSheet.getLiabilities());
            ((TotalViewHolder)viewHolder).cPL.setText(balSheet.getC_P_L());
        }else {
            Log.e("MONTH",balSheet.getMonth());
            BalSheetDateAdapter.MonthViewHolder h = (BalSheetDateAdapter.MonthViewHolder) viewHolder;
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
        BalSheet item = mList.get(position);
        if(item.isRow() == 0) {
            return 0;
        }else if (item.isRow() == 2){
            return 2;
        }else {
            return 1;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView date, capital, profitLoss, liabilities, cPL, assets;


        public ItemViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            capital = itemView.findViewById(R.id.capital);
            profitLoss = itemView.findViewById(R.id.profit_loss);
            liabilities = itemView.findViewById(R.id.liabilities);
            cPL = itemView.findViewById(R.id.c_p_l);
            assets = itemView.findViewById(R.id.assets);

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

    public void filterList(List<BalSheet> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }
}
