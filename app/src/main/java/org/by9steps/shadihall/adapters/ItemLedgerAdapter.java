package org.by9steps.shadihall.adapters;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.GeneralLedger;
import org.by9steps.shadihall.model.ItemLedger;

import java.util.List;

public class ItemLedgerAdapter extends RecyclerView.Adapter {

    private Context mCtx;
    List<ItemLedger> mList;

    public ItemLedgerAdapter(Context mCtx, List<ItemLedger> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == 0){
//            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
//            v.findViewById(android.R.id.text1).setBackgroundColor(Color.parseColor("#f0749f"));
//            return new ItemLedgerAdapter.MonthViewHolder(v);
//        }else if (viewType == 2){
        if (viewType == 1) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.item_ledger_list_item, null);
            return new ItemLedgerAdapter.ItemTRowViewHolder(view);
        } else  if (viewType == 2) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.item_ledger_list_item, null);
            return new ItemLedgerAdapter.ItemTransRowViewHolder(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.item_ledger_content_row, null);
            return new ItemLedgerAdapter.ItemViewHolder(view);
        }

//        }else {
//            LayoutInflater inflater = LayoutInflater.from(mCtx);
//            View view = inflater.inflate(R.layout.general_ledger_list_item, null);
//            return new ItemLedgerAdapter.ItemViewHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final ItemLedger itemledger = mList.get(position);

//        for (int i = 0; i <mList.get(position).columnsData.length ; i++) {
//
//        }
        if (itemledger.isRow == 1) {
            ////////////////////////Total Row
            ((ItemTRowViewHolder) viewHolder).textview1.setText("Total");
            ((ItemTRowViewHolder) viewHolder).textview2.setText("");
            ((ItemTRowViewHolder) viewHolder).textview3.setText(itemledger.columnsData[3]);
            ((ItemTRowViewHolder) viewHolder).textview4.setText(itemledger.columnsData[5]);
            ((ItemTRowViewHolder) viewHolder).textview5.setText(itemledger.columnsData[6]);
            ((ItemTRowViewHolder) viewHolder).textview6.setText(itemledger.columnsData[9]);
            ((ItemTRowViewHolder) viewHolder).textview7.setText(itemledger.columnsData[7]);
            ((ItemTRowViewHolder) viewHolder).textview8.setText(itemledger.columnsData[8]);
            ((ItemTRowViewHolder) viewHolder).textview9.setText(itemledger.columnsData[10]);
            ((ItemTRowViewHolder) viewHolder).textview10.setText(itemledger.columnsData[12]);
        }else if (itemledger.isRow == 2) {
            ////////////////////////Transaction Total Row
            ((ItemTransRowViewHolder) viewHolder).textview1.setText("Transaction");
            ((ItemTransRowViewHolder) viewHolder).textview2.setText("");
            ((ItemTransRowViewHolder) viewHolder).textview3.setText(itemledger.columnsData[3]);
            ((ItemTransRowViewHolder) viewHolder).textview4.setText(itemledger.columnsData[5]);
            ((ItemTransRowViewHolder) viewHolder).textview5.setText(itemledger.columnsData[6]);
            ((ItemTransRowViewHolder) viewHolder).textview6.setText(itemledger.columnsData[9]);
            ((ItemTransRowViewHolder) viewHolder).textview7.setText(itemledger.columnsData[7]);
            ((ItemTransRowViewHolder) viewHolder).textview8.setText(itemledger.columnsData[8]);
            ((ItemTransRowViewHolder) viewHolder).textview9.setText(itemledger.columnsData[10]);
            ((ItemTransRowViewHolder) viewHolder).textview10.setText(itemledger.columnsData[12]);
        } else {
            ///////////////////////Item Row
            ((ItemViewHolder) viewHolder).textview1.setText(itemledger.Datecol);
            ((ItemViewHolder) viewHolder).textview2.setText(itemledger.BillNocol);
            ((ItemViewHolder) viewHolder).textview3.setText(itemledger.EntryTypecol);
            ((ItemViewHolder) viewHolder).textview4.setText(itemledger.AccountNameCol);
            ((ItemViewHolder) viewHolder).textview5.setText(itemledger.RemarksCol);
            ((ItemViewHolder) viewHolder).textview6.setText(itemledger.PriceCol);
            ((ItemViewHolder) viewHolder).textview7.setText(itemledger.AddCol);
            ((ItemViewHolder) viewHolder).textview8.setText(itemledger.LessCol);
            ((ItemViewHolder) viewHolder).textview9.setText(itemledger.BalCol);
            ((ItemViewHolder) viewHolder).textview10.setText(itemledger.LocCol);
//            if(position%2==0){
//                ((ItemViewHolder) viewHolder).linearLayout.setBackgroundColor(mCtx.getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));
//            }


        }



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        ItemLedger item = mList.get(position);
        if (item.isRow == 1) {
            return 1;
        } else if (item.isRow == 2) {
            return 2;
        } else {
            return 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView textview1, textview2, textview3, textview4, textview5, textview6, textview7, textview8, textview9, textview10;
//        TextView editRecord, printRecord;
//        GeneralLedger refbalsheet;
        LinearLayout linearLayout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            //  itemView.setBackgroundColor(Color.RED);
            linearLayout=itemView.findViewById(R.id.linearlayoutgrid);
            textview1 = itemView.findViewById(R.id.textview1);
            textview2 = itemView.findViewById(R.id.textview2);
            textview3 = itemView.findViewById(R.id.textview3);
            textview4 = itemView.findViewById(R.id.textview4);
            textview5 = itemView.findViewById(R.id.textview5);
            textview6 = itemView.findViewById(R.id.textview6);
            textview7 = itemView.findViewById(R.id.textview7);
            textview8 = itemView.findViewById(R.id.textview8);
            textview9 = itemView.findViewById(R.id.textview9);
            textview10 = itemView.findViewById(R.id.textview10);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                   try{
//                       Log.e("EHllo ",""+refbalsheet.getEntryNo()+" --TblName::"+refbalsheet.getTablename()+" TblID::"+refbalsheet.getTableid());
//                       if(refbalsheet.getTablename()!=null && refbalsheet.getTablename().contains("SalePur1")){
//                           String split[]=refbalsheet.getTablename().split("_");
//                           Intent i=new Intent(v.getContext(), Salepur1AddNewActivity.class);
//                           Log.e("EHllo "," ------PKid"+getPkIdFromSalePur1(refbalsheet.getTableid(),split[1])+" sleas:"+split[1]);
//                           i.putExtra("pkid",getPkIdFromSalePur1(refbalsheet.getTableid(),split[1]));
//                           i.putExtra("salepur1id",refbalsheet.getTableid());
//                           i.putExtra("EntryType",split[1]);
//                           i.putExtra("edit",true);
//                           v.getContext().startActivity(i);
//                       }else if(refbalsheet.getTablename().equals("Null") && refbalsheet.getTableid().equals("0")){
////////////////////////////////////////////Sending To Cash Book Edit Dialog
//                           Log.e("CashBookID","---"+refbalsheet.getEntryNo());
//                           CashBookEntryDialog dialog = new CashBookEntryDialog();
//                           Bundle bb = new Bundle();
//                           bb.putString("BookingID", "0");
//                           bb.putString("Spinner", "View");
//                           bb.putString("EntryType",CashBookEntryDialog.entrytypelist[0]);
//
//                           ////////////////Type either Edit or New
//                           bb.putString("Type", "Edit");
//                           /////////////////////if view Type is edit then must send CashBookID to update
//                           bb.putString("CashBookID", refbalsheet.getEntryNo());
//                           dialog.setArguments(bb);
//                           try {
//                               dialog.show( ((GeneralLedgerActivity)mCtx).getSupportFragmentManager(), "Default");
//                           }catch (Exception e){
//                               e.printStackTrace();
//                           }
//                       }
//
//                   }catch (Exception e){e.printStackTrace();}
                }
            });

        }

//        private int getPkIdFromSalePur1(String sleid, String s) {
//            int returnid=-1;
//            String qq="Select * from SalePur1 where SalePur1ID = "+sleid+" AND EntryType = '"+s+"'";
//            DatabaseHelper helper=new DatabaseHelper(itemView.getContext());
//           List<Salepur1>  list =refdb.SlePur1.GetSalePur1Data(helper,qq);
//           if(list!=null && list.size()>0){
//               return list.get(0).getID();
//           }else
//               return returnid;
//
//        }


    }


    class ItemTRowViewHolder extends RecyclerView.ViewHolder {

        TextView textview1, textview2, textview3, textview4, textview5, textview6, textview7, textview8, textview9, textview10;
//        TextView editRecord, printRecord;
//        GeneralLedger refbalsheet;

        public ItemTRowViewHolder(View itemView) {
            super(itemView);
            //  itemView.setBackgroundColor(Color.RED);
            textview1 = itemView.findViewById(R.id.textview1);
            textview2 = itemView.findViewById(R.id.textview2);
            textview3 = itemView.findViewById(R.id.textview3);
            textview4 = itemView.findViewById(R.id.textview4);
            textview5 = itemView.findViewById(R.id.textview5);
            textview6 = itemView.findViewById(R.id.textview6);
            textview7 = itemView.findViewById(R.id.textview7);
            textview8 = itemView.findViewById(R.id.textview8);
            textview9 = itemView.findViewById(R.id.textview9);
            textview10 = itemView.findViewById(R.id.textview10);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                   try{
//                       Log.e("EHllo ",""+refbalsheet.getEntryNo()+" --TblName::"+refbalsheet.getTablename()+" TblID::"+refbalsheet.getTableid());
//                       if(refbalsheet.getTablename()!=null && refbalsheet.getTablename().contains("SalePur1")){
//                           String split[]=refbalsheet.getTablename().split("_");
//                           Intent i=new Intent(v.getContext(), Salepur1AddNewActivity.class);
//                           Log.e("EHllo "," ------PKid"+getPkIdFromSalePur1(refbalsheet.getTableid(),split[1])+" sleas:"+split[1]);
//                           i.putExtra("pkid",getPkIdFromSalePur1(refbalsheet.getTableid(),split[1]));
//                           i.putExtra("salepur1id",refbalsheet.getTableid());
//                           i.putExtra("EntryType",split[1]);
//                           i.putExtra("edit",true);
//                           v.getContext().startActivity(i);
//                       }else if(refbalsheet.getTablename().equals("Null") && refbalsheet.getTableid().equals("0")){
////////////////////////////////////////////Sending To Cash Book Edit Dialog
//                           Log.e("CashBookID","---"+refbalsheet.getEntryNo());
//                           CashBookEntryDialog dialog = new CashBookEntryDialog();
//                           Bundle bb = new Bundle();
//                           bb.putString("BookingID", "0");
//                           bb.putString("Spinner", "View");
//                           bb.putString("EntryType",CashBookEntryDialog.entrytypelist[0]);
//
//                           ////////////////Type either Edit or New
//                           bb.putString("Type", "Edit");
//                           /////////////////////if view Type is edit then must send CashBookID to update
//                           bb.putString("CashBookID", refbalsheet.getEntryNo());
//                           dialog.setArguments(bb);
//                           try {
//                               dialog.show( ((GeneralLedgerActivity)mCtx).getSupportFragmentManager(), "Default");
//                           }catch (Exception e){
//                               e.printStackTrace();
//                           }
//                       }
//
//                   }catch (Exception e){e.printStackTrace();}
                }
            });

        }

//        private int getPkIdFromSalePur1(String sleid, String s) {
//            int returnid=-1;
//            String qq="Select * from SalePur1 where SalePur1ID = "+sleid+" AND EntryType = '"+s+"'";
//            DatabaseHelper helper=new DatabaseHelper(itemView.getContext());
//           List<Salepur1>  list =refdb.SlePur1.GetSalePur1Data(helper,qq);
//           if(list!=null && list.size()>0){
//               return list.get(0).getID();
//           }else
//               return returnid;
//
//        }
    }
    class ItemTransRowViewHolder extends RecyclerView.ViewHolder {

        TextView textview1, textview2, textview3, textview4, textview5, textview6, textview7, textview8, textview9, textview10;
//        TextView editRecord, printRecord;
//        GeneralLedger refbalsheet;

        public ItemTransRowViewHolder(View itemView) {
            super(itemView);
            //  itemView.setBackgroundColor(Color.RED);
            textview1 = itemView.findViewById(R.id.textview1);
            textview2 = itemView.findViewById(R.id.textview2);
            textview3 = itemView.findViewById(R.id.textview3);
            textview4 = itemView.findViewById(R.id.textview4);
            textview5 = itemView.findViewById(R.id.textview5);
            textview6 = itemView.findViewById(R.id.textview6);
            textview7 = itemView.findViewById(R.id.textview7);
            textview8 = itemView.findViewById(R.id.textview8);
            textview9 = itemView.findViewById(R.id.textview9);
            textview10 = itemView.findViewById(R.id.textview10);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                   try{
//                       Log.e("EHllo ",""+refbalsheet.getEntryNo()+" --TblName::"+refbalsheet.getTablename()+" TblID::"+refbalsheet.getTableid());
//                       if(refbalsheet.getTablename()!=null && refbalsheet.getTablename().contains("SalePur1")){
//                           String split[]=refbalsheet.getTablename().split("_");
//                           Intent i=new Intent(v.getContext(), Salepur1AddNewActivity.class);
//                           Log.e("EHllo "," ------PKid"+getPkIdFromSalePur1(refbalsheet.getTableid(),split[1])+" sleas:"+split[1]);
//                           i.putExtra("pkid",getPkIdFromSalePur1(refbalsheet.getTableid(),split[1]));
//                           i.putExtra("salepur1id",refbalsheet.getTableid());
//                           i.putExtra("EntryType",split[1]);
//                           i.putExtra("edit",true);
//                           v.getContext().startActivity(i);
//                       }else if(refbalsheet.getTablename().equals("Null") && refbalsheet.getTableid().equals("0")){
////////////////////////////////////////////Sending To Cash Book Edit Dialog
//                           Log.e("CashBookID","---"+refbalsheet.getEntryNo());
//                           CashBookEntryDialog dialog = new CashBookEntryDialog();
//                           Bundle bb = new Bundle();
//                           bb.putString("BookingID", "0");
//                           bb.putString("Spinner", "View");
//                           bb.putString("EntryType",CashBookEntryDialog.entrytypelist[0]);
//
//                           ////////////////Type either Edit or New
//                           bb.putString("Type", "Edit");
//                           /////////////////////if view Type is edit then must send CashBookID to update
//                           bb.putString("CashBookID", refbalsheet.getEntryNo());
//                           dialog.setArguments(bb);
//                           try {
//                               dialog.show( ((GeneralLedgerActivity)mCtx).getSupportFragmentManager(), "Default");
//                           }catch (Exception e){
//                               e.printStackTrace();
//                           }
//                       }
//
//                   }catch (Exception e){e.printStackTrace();}
                }
            });

        }

//        private int getPkIdFromSalePur1(String sleid, String s) {
//            int returnid=-1;
//            String qq="Select * from SalePur1 where SalePur1ID = "+sleid+" AND EntryType = '"+s+"'";
//            DatabaseHelper helper=new DatabaseHelper(itemView.getContext());
//           List<Salepur1>  list =refdb.SlePur1.GetSalePur1Data(helper,qq);
//           if(list!=null && list.size()>0){
//               return list.get(0).getID();
//           }else
//               return returnid;
//
//        }
    }
    public void filterList(List<ItemLedger> filterdNames) {
        this.mList = filterdNames;
        notifyDataSetChanged();
    }
}
