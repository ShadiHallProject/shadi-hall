package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.GeneralLedgerActivity;
import org.by9steps.shadihall.fragments.ReportsFragment;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.BalSheet;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.CashEntry;
import org.by9steps.shadihall.model.ProfitLoss;
import org.by9steps.shadihall.model.Reports;
import org.by9steps.shadihall.model.Summerize;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter {

    public static final int Cash_Book = 0;
    public static final int Trail_Balance = 1;
    public static final int Summerize_Trail_Balance = 2;
    public static final int Bal_Sheet = 3;
    public static final int Profit_Loss = 4;

    private Context mCtx;
    List<Reports> mList;
    List<Account3Name> mList2;
    List<Summerize> mList3;
    List<BalSheet> mList4;
    List<ProfitLoss> mList5;

    int mCredit = 0, mDebit = 0;
    String baSheet, proLoss;

    int typeSet;

    public ReportsAdapter(Context mCtx, List<Reports> mList, int typeSet) {
        this.mCtx = mCtx;
        this.mList = mList;
        this.typeSet = typeSet;
    }
    public ReportsAdapter(Context mCtx, List<Account3Name> mList2, int typeSet,String desc) {
        this.mCtx = mCtx;
        this.mList2 = mList2;
        this.typeSet = typeSet;
    }
    public ReportsAdapter(Context mCtx, List<Summerize> mList3, int typeSet, String desc, String sumerize) {
        this.mCtx = mCtx;
        this.mList3 = mList3;
        this.typeSet = typeSet;
    }
    public ReportsAdapter(Context mCtx, List<BalSheet> mList4, int typeSet, String desc, String sumerize, String balSheet) {
        this.mCtx = mCtx;
        this.mList4 = mList4;
        this.typeSet = typeSet;
        this.baSheet = balSheet;
    }
    public ReportsAdapter(Context mCtx, List<ProfitLoss> mList5, int typeSet, String desc, String sumerize, String balSheet,String proLoss) {
        this.mCtx = mCtx;
        this.mList5 = mList5;
        this.typeSet = typeSet;
        this.proLoss = proLoss;
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
            case Summerize_Trail_Balance:
                view = inflater.inflate(R.layout.summerize_trail_bal_item,null);
                return new ReportsAdapter.SummerizeTrailBalance(view);
            case Bal_Sheet:
                view = inflater.inflate(R.layout.bal_sheet_list_item,null);
                return new ReportsAdapter.BalanceSheet(view);
            case Profit_Loss:
                view = inflater.inflate(R.layout.profit_loss_list_item,null);
                return new ReportsAdapter.ProfitLos(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        switch (getItemViewType(0)){
            case Cash_Book:
                final Reports cashBook = mList.get(position);

                ((CashBookHolder)viewHolder).ac_name.setText(cashBook.getAcName());
//                ((CashBookHolder)viewHolder).debit.setText(cashBook.getDebit());
//                ((CashBookHolder)viewHolder).credit.setText(cashBook.getCredit());
//                ((CashBookHolder)viewHolder).balance.setText(cashBook.getBal());
                ((CashBookHolder)viewHolder).debit_bal.setText(cashBook.getDebitBal());
                ((CashBookHolder)viewHolder).credit_bal.setText(cashBook.getCreditBal());

                mCredit = mCredit + Integer.valueOf(cashBook.getCreditBal());
                mDebit = mDebit + Integer.valueOf(cashBook.getDebitBal());

                ReportsFragment.cre_total.setText(String.valueOf(mCredit));
                ReportsFragment.deb_total.setText(String.valueOf(mDebit));

                ((CashBookHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCtx.startActivity(new Intent(mCtx, GeneralLedgerActivity.class));
                    }
                });
                break;
//            case Trail_Balance:
//                final Account3Name account3Name = mList2.get(position);
//                ((TrailBalance)viewHolder).ac_type.setText(account3Name.getAcTypeName());
//                ((TrailBalance)viewHolder).ac_group.setText(account3Name.getAcGruopName());
//                ((TrailBalance)viewHolder).ac_name.setText(account3Name.getAcName());
//                ((TrailBalance)viewHolder).debit.setText(account3Name.getDebit());
//                ((TrailBalance)viewHolder).credit.setText(account3Name.getCredit());
//                ((TrailBalance)viewHolder).balance.setText(account3Name.getBal());
//                ((TrailBalance)viewHolder).debit_bal.setText(account3Name.getDebitBL());
//                ((TrailBalance)viewHolder).credit_bal.setText(account3Name.getCreditBL());
//                break;
//            case Summerize_Trail_Balance:
//                final Summerize summerize = mList3.get(position);
//                ((SummerizeTrailBalance)viewHolder).ac_type.setText(summerize.getAcTypeName());
//                ((SummerizeTrailBalance)viewHolder).ac_group.setText(summerize.getAcGruopName());
////                ((SummerizeTrailBalance)viewHolder).debit.setText(summerize.getDebit());
////                ((SummerizeTrailBalance)viewHolder).credit.setText(summerize.getCredit());
////                ((SummerizeTrailBalance)viewHolder).balance.setText(summerize.getBal());
//                ((SummerizeTrailBalance)viewHolder).debit_bal.setText(summerize.getDebitBL());
//                ((SummerizeTrailBalance)viewHolder).credit_bal.setText(summerize.getCreditBL());
//                break;
//            case Bal_Sheet:
//                final BalSheet balSheet = mList4.get(position);
//                ((BalanceSheet)viewHolder).date.setText(balSheet.getCBDate());
//                ((BalanceSheet)viewHolder).capital.setText(balSheet.getCapital());
//                ((BalanceSheet)viewHolder).assets.setText(balSheet.getAssets());
//                ((BalanceSheet)viewHolder).profitLoss.setText(balSheet.getProfitLoss());
//                ((BalanceSheet)viewHolder).liabilities.setText(balSheet.getLiabilities());
//                ((BalanceSheet)viewHolder).cPL.setText(balSheet.getC_P_L());
//
//                if (baSheet.equals("month")){
//                    ((BalanceSheet)viewHolder).sorting.setVisibility(View.VISIBLE);
//                    ((BalanceSheet)viewHolder).sorting.setText(balSheet.getSorting());
//                }
//                break;
//            case Profit_Loss:
//                final ProfitLoss profitLos = mList5.get(position);
//                ((ProfitLos)viewHolder).date.setText(profitLos.getCBDate());
//                ((ProfitLos)viewHolder).income.setText(profitLos.getIncome());
//                ((ProfitLos)viewHolder).expense.setText(profitLos.getExpense());
//                ((ProfitLos)viewHolder).profit.setText(profitLos.getProfit());
//
//
//                if (proLoss.equals("year")){
//                    ((ProfitLos)viewHolder).sorting.setVisibility(View.VISIBLE);
//                    ((ProfitLos)viewHolder).sorting.setText(profitLos.getSorting());
//                }
//                break;
//
        }
    }

    @Override
    public int getItemCount() {
        switch (getItemViewType(0)) {
            case Cash_Book:
                return mList.size();
            case Trail_Balance:
                return mList2.size();
            case Summerize_Trail_Balance:
                return mList3.size();
            case Bal_Sheet:
                return mList4.size();
            case Profit_Loss:
                return mList5.size();
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
//            debit = itemView.findViewById(R.id.debit);
//            credit = itemView.findViewById(R.id.credit);
//            balance = itemView.findViewById(R.id.balance);
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

    class SummerizeTrailBalance extends RecyclerView.ViewHolder {

        TextView ac_type, ac_group, debit, credit, balance, debit_bal, credit_bal;


        public SummerizeTrailBalance(View itemView) {
            super(itemView);

            ac_type = itemView.findViewById(R.id.ac_type);
            ac_group = itemView.findViewById(R.id.ac_group);
//            debit = itemView.findViewById(R.id.debit);
//            credit = itemView.findViewById(R.id.credit);
//            balance = itemView.findViewById(R.id.balance);
            debit_bal = itemView.findViewById(R.id.deb_balance);
            credit_bal = itemView.findViewById(R.id.cre_balance);

        }
    }

    class BalanceSheet extends RecyclerView.ViewHolder {

        TextView date, capital, profitLoss, liabilities, cPL, assets, sorting;


        public BalanceSheet(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            capital = itemView.findViewById(R.id.capital);
            profitLoss = itemView.findViewById(R.id.profit_loss);
            liabilities = itemView.findViewById(R.id.liabilities);
            cPL = itemView.findViewById(R.id.c_p_l);
            assets = itemView.findViewById(R.id.assets);
            sorting = itemView.findViewById(R.id.sorting);

        }
    }

    class ProfitLos extends RecyclerView.ViewHolder {

        TextView date, income, expense, profit, sorting;


        public ProfitLos(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            income = itemView.findViewById(R.id.income);
            expense = itemView.findViewById(R.id.expense);
            profit = itemView.findViewById(R.id.profit);
            sorting = itemView.findViewById(R.id.sorting);

        }
    }

    public void filterList(List<Reports> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }
}
