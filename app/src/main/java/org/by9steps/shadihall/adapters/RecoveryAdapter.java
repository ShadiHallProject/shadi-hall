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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.AddExpenseActivity;
import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.model.Recovery;

import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecoveryAdapter extends RecyclerView.Adapter {

    private Context mCtx;
    List<Recovery> mList;

    public RecoveryAdapter(Context mCtx, List<Recovery> mList) {
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
            View view = inflater.inflate(R.layout.recovery_group_total, null);
            return new RecoveryAdapter.TotalViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.recovery_list_item, null);
            return new RecoveryAdapter.ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final Recovery recovery = mList.get(position);

        if (recovery.isRow() == 1) {
            ((ItemViewHolder) viewHolder).recieved.setText(recovery.getRecieved());
            ((ItemViewHolder) viewHolder).expensed.setText(recovery.getExpensed());
            ((ItemViewHolder) viewHolder).total_charges.setText(recovery.getChargesTotal());
            ((ItemViewHolder) viewHolder).balance.setText(recovery.getBalance());
            ((ItemViewHolder) viewHolder).profit.setText(recovery.getProfit());
            ((ItemViewHolder) viewHolder).event_name.setText(recovery.getEventName());

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sf.parse(recovery.getEventDate());
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                String d = s.format(date);
                ((ItemViewHolder) viewHolder).event_date.setText(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((ItemViewHolder) viewHolder).client_name.setText(recovery.getClientName());

            if (AppController.addCB.equals("Hide")) {
                ((ItemViewHolder) viewHolder).add.setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) viewHolder).add.setVisibility(View.VISIBLE);
            }

            ((ItemViewHolder) viewHolder).add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, CashCollectionActivity.class);
                    intent.putExtra("BookingID", recovery.getBookingID());
                    intent.putExtra("Spinner", "Hide");
                    mCtx.startActivity(intent);

                }
            });

            ((ItemViewHolder) viewHolder).add_expense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, AddExpenseActivity.class);
                    intent.putExtra("BookingID", recovery.getBookingID());
                    intent.putExtra("Spinner", "Hide");
                    mCtx.startActivity(intent);
                }
            });
        }else if (recovery.isRow() == 2){
            ((TotalViewHolder) viewHolder).recieved.setText(recovery.getRecieved());
            ((TotalViewHolder) viewHolder).expensed.setText(recovery.getExpensed());
            ((TotalViewHolder) viewHolder).total_charges.setText(recovery.getChargesTotal());
            ((TotalViewHolder) viewHolder).balance.setText(recovery.getBalance());
            ((TotalViewHolder) viewHolder).profit.setText(recovery.getProfit());
        }else {
            MonthViewHolder h = (MonthViewHolder) viewHolder;
            h.textView.setText(recovery.getMonth());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        Recovery item = mList.get(position);
        if(item.isRow() == 0) {
            return 0;
        }else if (item.isRow() == 2){
            return 2;
        }else {
            return 1;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView recieved, expensed, total_charges, balance, profit, event_name, event_date, client_name, month;
        ImageButton add, add_expense;
        LinearLayout totalLayout;


        public ItemViewHolder(View itemView) {
            super(itemView);

            recieved = itemView.findViewById(R.id.recieved);
            expensed = itemView.findViewById(R.id.expensed);
            total_charges = itemView.findViewById(R.id.total_charges);
            balance = itemView.findViewById(R.id.balance);
            profit = itemView.findViewById(R.id.profit);
            event_name = itemView.findViewById(R.id.event_name);
            event_date = itemView.findViewById(R.id.event_date);
            client_name = itemView.findViewById(R.id.client_name);
            add = itemView.findViewById(R.id.add);
            add_expense = itemView.findViewById(R.id.add_expense);
            totalLayout = itemView.findViewById(R.id.total_layout);
            month = itemView.findViewById(R.id.month);

        }
    }

    class TotalViewHolder extends RecyclerView.ViewHolder {

        TextView recieved, expensed, total_charges, balance, profit;
        ImageButton add, add_expense;
        LinearLayout totalLayout;


        public TotalViewHolder(View itemView) {
            super(itemView);

            recieved = itemView.findViewById(R.id.total_recieved);
            expensed = itemView.findViewById(R.id.total_expensed);
            total_charges = itemView.findViewById(R.id.g_total_charges);
            balance = itemView.findViewById(R.id.total_balance);
            profit = itemView.findViewById(R.id.total_profit);

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
