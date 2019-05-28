package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.model.ProfitLoss;
import org.by9steps.shadihall.model.Recovery;
import org.by9steps.shadihall.model.Summerize;

import java.util.List;

public class SummerizeTrialBalanceAdapter extends RecyclerView.Adapter {

    private Context mCtx;
    List<Summerize> mList;

    public SummerizeTrialBalanceAdapter(Context mCtx, List<Summerize> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            v.findViewById(android.R.id.text1).setBackgroundColor(Color.parseColor("#f0749f"));
            return new SummerizeTrialBalanceAdapter.MonthViewHolder(v);
        }else if (viewType == 2){
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.summerize_trail_bal_group_total, null);
            return new SummerizeTrialBalanceAdapter.TotalViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.summerize_trail_bal_item, null);
            return new SummerizeTrialBalanceAdapter.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final Summerize summerize = mList.get(position);

        if (summerize.isRow() == 1) {
            ((ItemViewHolder)viewHolder).ac_group.setText(summerize.getAcGruopName());
//                ((SummerizeTrailBalance)viewHolder).debit.setText(summerize.getDebit());
//                ((SummerizeTrailBalance)viewHolder).credit.setText(summerize.getCredit());
//                ((SummerizeTrailBalance)viewHolder).balance.setText(summerize.getBal());
            ((ItemViewHolder)viewHolder).debit_bal.setText(summerize.getDebitBL());
            ((ItemViewHolder)viewHolder).credit_bal.setText(summerize.getCreditBL());

            ((ItemViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, MenuClickActivity.class);
                    intent.putExtra("message",summerize.getAcGruopName());
                    intent.putExtra("position",String.valueOf(position));
                    mCtx.startActivity(intent);
                }
            });

        }else if (summerize.isRow() == 2){
            ((TotalViewHolder)viewHolder).debit_bal.setText(summerize.getDebitBL());
            ((TotalViewHolder)viewHolder).credit_bal.setText(summerize.getCreditBL());
        }else {
            SummerizeTrialBalanceAdapter.MonthViewHolder h = (SummerizeTrialBalanceAdapter.MonthViewHolder) viewHolder;
            h.textView.setText(summerize.getMonth());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        Summerize item = mList.get(position);
        if(item.isRow() == 0) {
            return 0;
        }else if (item.isRow() == 2){
            return 2;
        }else {
            return 1;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView ac_type, ac_group, debit, credit, balance, debit_bal, credit_bal;


        public ItemViewHolder(View itemView) {
            super(itemView);

//            ac_type = itemView.findViewById(R.id.ac_type);
            ac_group = itemView.findViewById(R.id.ac_group);
//            debit = itemView.findViewById(R.id.debit);
//            credit = itemView.findViewById(R.id.credit);
//            balance = itemView.findViewById(R.id.balance);
            debit_bal = itemView.findViewById(R.id.deb_balance);
            credit_bal = itemView.findViewById(R.id.cre_balance);

        }
    }

    class TotalViewHolder extends RecyclerView.ViewHolder {

        TextView debit_bal, credit_bal;


        public TotalViewHolder(View itemView) {
            super(itemView);

            debit_bal = itemView.findViewById(R.id.deb_balance);
            credit_bal = itemView.findViewById(R.id.cre_balance);

        }
    }

    class MonthViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    public void filterList(List<Summerize> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }
}
