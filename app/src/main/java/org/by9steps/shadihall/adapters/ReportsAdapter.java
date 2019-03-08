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
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.Menu;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter {

    public static final int Cash_Book = 0;
    public static final int Trail_Balance = 1;

    private Context mCtx;
    List<CashBook> mList;
    List<Account3Name> mList2;

    int typeSet;

    public ReportsAdapter(Context mCtx, List<CashBook> mList, int typeSet) {
        this.mCtx = mCtx;
        this.mList = mList;
        this.typeSet = typeSet;
    }
    public ReportsAdapter(Context mCtx, List<Account3Name> mList2, int typeSet,String desc) {
        this.mCtx = mCtx;
        this.mList2 = mList2;
        this.typeSet = typeSet;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view;

        switch (viewType) {
            case Cash_Book:
                view = inflater.inflate(R.layout.cash_book_item, null);
                return new ReportsAdapter.CashBookHolder(view);
            case Trail_Balance:
                view = inflater.inflate(R.layout.trail_balance_item, null);
                return new ReportsAdapter.TrailBalance(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        switch (getItemViewType(0)){
            case Cash_Book:
                final CashBook cashBook = mList.get(position);

                ((CashBookHolder)viewHolder).ac_name.setText(cashBook.getAcName());
                ((CashBookHolder)viewHolder).debit.setText(cashBook.getDebit());
                ((CashBookHolder)viewHolder).credit.setText(cashBook.getCredit());
                ((CashBookHolder)viewHolder).balance.setText(cashBook.getBal());
                ((CashBookHolder)viewHolder).debit_bal.setText(cashBook.getDebitBal());
                ((CashBookHolder)viewHolder).credit_bal.setText(cashBook.getCreditBal());

                break;
            case Trail_Balance:
                final Account3Name account3Name = mList2.get(position);
                ((TrailBalance)viewHolder).ac_type.setText(account3Name.getAcTypeName());
                ((TrailBalance)viewHolder).ac_group.setText(account3Name.getAcGruopName());
                ((TrailBalance)viewHolder).ac_name.setText(account3Name.getAcName());
                ((TrailBalance)viewHolder).debit.setText(account3Name.getDebit());
                ((TrailBalance)viewHolder).credit.setText(account3Name.getCredit());
                ((TrailBalance)viewHolder).balance.setText(account3Name.getBal());
                ((TrailBalance)viewHolder).debit_bal.setText(account3Name.getDebitBL());
                ((TrailBalance)viewHolder).credit_bal.setText(account3Name.getCreditBL());
        }
    }

    @Override
    public int getItemCount() {
        switch (getItemViewType(0)) {
            case Cash_Book:
                return mList.size();

            case Trail_Balance:
                return mList2.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return typeSet;
    }

    class CashBookHolder extends RecyclerView.ViewHolder {

        TextView ac_name, debit, credit, balance, debit_bal, credit_bal;


        public CashBookHolder(View itemView) {
            super(itemView);

            ac_name = itemView.findViewById(R.id.ac_name);
            debit = itemView.findViewById(R.id.debit);
            credit = itemView.findViewById(R.id.credit);
            balance = itemView.findViewById(R.id.balance);
            debit_bal = itemView.findViewById(R.id.debit_bal);
            credit_bal = itemView.findViewById(R.id.credit_bal);

        }
    }
    class TrailBalance extends RecyclerView.ViewHolder {

        TextView ac_type, ac_group, ac_name, debit, credit, balance, debit_bal, credit_bal;


        public TrailBalance(View itemView) {
            super(itemView);

            ac_type = itemView.findViewById(R.id.ac_type);
            ac_group = itemView.findViewById(R.id.ac_group);
            ac_name = itemView.findViewById(R.id.ac_name);
            debit = itemView.findViewById(R.id.debit);
            credit = itemView.findViewById(R.id.credit);
            balance = itemView.findViewById(R.id.balance);
            debit_bal = itemView.findViewById(R.id.debit_bal);
            credit_bal = itemView.findViewById(R.id.credit_bal);

        }
    }
}
