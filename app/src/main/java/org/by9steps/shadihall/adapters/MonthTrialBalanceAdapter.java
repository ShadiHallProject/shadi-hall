package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.GeneralLedgerActivity;
import org.by9steps.shadihall.model.MonthTb;

import java.util.List;

public class MonthTrialBalanceAdapter extends RecyclerView.Adapter{

    private Context mCtx;
    List<MonthTb> mList;

    public MonthTrialBalanceAdapter(Context mCtx, List<MonthTb> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            v.findViewById(android.R.id.text1).setBackgroundColor(Color.parseColor("#f0749f"));
            return new MonthViewHolder(v);
        }else if (viewType == 2){
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.month_trial_balance_item, null);
            return new ItemViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.month_trial_balance_item, null);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final MonthTb monthTb = mList.get(position);

        if (monthTb.isRow() == 1) {

            ((ItemViewHolder)viewHolder).mtb_acname.setText(monthTb.getAcName());
            ((ItemViewHolder)viewHolder).mtb_predebit.setText(monthTb.getPrvDebit());
            ((ItemViewHolder)viewHolder).mtb_precredit.setText(monthTb.getPrvCredit());
            ((ItemViewHolder)viewHolder).mtb_tradebit.setText(monthTb.getTraDebit());
            ((ItemViewHolder)viewHolder).mtb_tracredit.setText(monthTb.getTraCredit());
            ((ItemViewHolder)viewHolder).mtb_clodebit.setText(monthTb.getClosingDebit());
            ((ItemViewHolder)viewHolder).mtb_clocredit.setText(monthTb.getClosingCredit());

            ((ItemViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx,GeneralLedgerActivity.class);
                    intent.putExtra("AcID",monthTb.getAccountID());
                    mCtx.startActivity(intent);
                }
            });

        }else if (monthTb.isRow() == 2){

            ((ItemViewHolder)viewHolder).mtb_acname.setTypeface(((ItemViewHolder)viewHolder).mtb_acname.getTypeface(),Typeface.BOLD);
            ((ItemViewHolder)viewHolder).mtb_predebit.setTypeface(((ItemViewHolder)viewHolder).mtb_predebit.getTypeface(),Typeface.BOLD);
            ((ItemViewHolder)viewHolder).mtb_precredit.setTypeface(((ItemViewHolder)viewHolder).mtb_precredit.getTypeface(),Typeface.BOLD);
            ((ItemViewHolder)viewHolder).mtb_tradebit.setTypeface(((ItemViewHolder)viewHolder).mtb_tradebit.getTypeface(),Typeface.BOLD);
            ((ItemViewHolder)viewHolder).mtb_tracredit.setTypeface(((ItemViewHolder)viewHolder).mtb_tracredit.getTypeface(),Typeface.BOLD);
            ((ItemViewHolder)viewHolder).mtb_clodebit.setTypeface(((ItemViewHolder)viewHolder).mtb_clodebit.getTypeface(),Typeface.BOLD);
            ((ItemViewHolder)viewHolder).mtb_clocredit.setTypeface(((ItemViewHolder)viewHolder).mtb_clocredit.getTypeface(),Typeface.BOLD);


            ((ItemViewHolder)viewHolder).mtb_acname.setText("Total");
            ((ItemViewHolder)viewHolder).mtb_predebit.setText(monthTb.getPrvDebit());
            ((ItemViewHolder)viewHolder).mtb_precredit.setText(monthTb.getPrvCredit());
            ((ItemViewHolder)viewHolder).mtb_tradebit.setText(monthTb.getTraDebit());
            ((ItemViewHolder)viewHolder).mtb_tracredit.setText(monthTb.getTraCredit());
            ((ItemViewHolder)viewHolder).mtb_clodebit.setText(monthTb.getClosingDebit());
            ((ItemViewHolder)viewHolder).mtb_clocredit.setText(monthTb.getClosingCredit());

        }else {
            MonthTrialBalanceAdapter.MonthViewHolder h = (MonthTrialBalanceAdapter.MonthViewHolder) viewHolder;
            h.textView.setText(monthTb.getAcTypeName());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        MonthTb item = mList.get(position);
        if(item.isRow() == 0) {
            return 0;
        }else if (item.isRow() == 2){
            return 2;
        }else {
            return 1;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mtb_acname, mtb_predebit, mtb_precredit, mtb_tradebit, mtb_tracredit, mtb_clodebit, mtb_clocredit;


        public ItemViewHolder(View itemView) {
            super(itemView);
            mtb_acname = itemView.findViewById(R.id.mtb_acname);
            mtb_predebit = itemView.findViewById(R.id.mtb_predebit);
            mtb_precredit = itemView.findViewById(R.id.mtb_precredit);
            mtb_tradebit = itemView.findViewById(R.id.mtb_tradebit);
            mtb_tracredit = itemView.findViewById(R.id.mtb_tracredit);
            mtb_clodebit = itemView.findViewById(R.id.mtb_clodebit);
            mtb_clocredit = itemView.findViewById(R.id.mtb_clocredit);

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

    public void filterList(List<MonthTb> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }
}
