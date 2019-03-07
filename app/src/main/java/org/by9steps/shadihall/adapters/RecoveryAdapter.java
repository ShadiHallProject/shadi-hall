package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.model.Menu;
import org.by9steps.shadihall.model.Recovery;

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recovery_list_item, null);
        return new RecoveryAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final Recovery recovery = mList.get(position);

        ((ItemViewHolder)viewHolder).recieved.setText(recovery.getRecieved());
        ((ItemViewHolder)viewHolder).expensed.setText(recovery.getExpensed());
        ((ItemViewHolder)viewHolder).total_charges.setText(recovery.getChargesTotal());
        ((ItemViewHolder)viewHolder).balance.setText(recovery.getBalance());
        ((ItemViewHolder)viewHolder).profit.setText(recovery.getProfit());
        ((ItemViewHolder)viewHolder).event_name.setText(recovery.getEventName());
        ((ItemViewHolder)viewHolder).event_date.setText(recovery.getEventDate());
        ((ItemViewHolder)viewHolder).client_name.setText(recovery.getClientName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView recieved, expensed, total_charges, balance, profit, event_name, event_date, client_name;


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

        }
    }
}
