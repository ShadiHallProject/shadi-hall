package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.BalSheet;
import org.by9steps.shadihall.model.ProfitLoss;

import java.util.List;

public class ProfitLossDateAdapter extends RecyclerView.Adapter{

    private Context mCtx;
    List<ProfitLoss> mList;

    public ProfitLossDateAdapter(Context mCtx, List<ProfitLoss> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            v.findViewById(android.R.id.text1).setBackgroundColor(Color.parseColor("#f0749f"));
            return new ProfitLossDateAdapter.MonthViewHolder(v);
        }else if (viewType == 2){
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.profit_loss_group_total, null);
            return new ProfitLossDateAdapter.TotalViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.profit_loss_list_item, null);
            return new ProfitLossDateAdapter.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final ProfitLoss profitLoss = mList.get(position);

        if (profitLoss.isRow() == 1) {
            ((ItemViewHolder)viewHolder).date.setText(profitLoss.getCBDate());
            ((ItemViewHolder)viewHolder).income.setText(profitLoss.getIncome());
            ((ItemViewHolder)viewHolder).expense.setText(profitLoss.getExpense());
            ((ItemViewHolder)viewHolder).profit.setText(profitLoss.getProfit());

        }else if (profitLoss.isRow() == 2){
            ((TotalViewHolder)viewHolder).income.setText(profitLoss.getIncome());
            ((TotalViewHolder)viewHolder).expense.setText(profitLoss.getExpense());
            ((TotalViewHolder)viewHolder).profit.setText(profitLoss.getProfit());
        }else {
            ProfitLossDateAdapter.MonthViewHolder h = (ProfitLossDateAdapter.MonthViewHolder) viewHolder;
            h.textView.setText(profitLoss.getMonth());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        ProfitLoss item = mList.get(position);
        if(item.isRow() == 0) {
            return 0;
        }else if (item.isRow() == 2){
            return 2;
        }else {
            return 1;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView date, income, expense, profit, sorting;


        public ItemViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            income = itemView.findViewById(R.id.income);
            expense = itemView.findViewById(R.id.expense);
            profit = itemView.findViewById(R.id.profit);
            sorting = itemView.findViewById(R.id.sorting);

        }
    }

    class TotalViewHolder extends RecyclerView.ViewHolder {

        TextView date, income, expense, profit;


        public TotalViewHolder(View itemView) {
            super(itemView);

            income = itemView.findViewById(R.id.income);
            expense = itemView.findViewById(R.id.expense);
            profit = itemView.findViewById(R.id.profit);

        }
    }

    class MonthViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    public void filterList(List<ProfitLoss> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }
}
