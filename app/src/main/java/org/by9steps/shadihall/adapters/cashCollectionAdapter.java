package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.chartofaccountdialog.DialogResturentOption;
import org.by9steps.shadihall.chartofaccountdialog.cashCollectionDialog;
import org.by9steps.shadihall.model.joinQueryCashCollection;

import java.util.List;

public class cashCollectionAdapter extends RecyclerView.Adapter<cashCollectionAdapter.mRecycleView>{
    private Context context;
    private List<joinQueryCashCollection> mlist;

    public cashCollectionAdapter(Context context, List<joinQueryCashCollection> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public mRecycleView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.cash_collection_gridview_row,viewGroup,false);
        return new mRecycleView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mRecycleView holder, final int i) {


        holder.tvBillNo.setText(mlist.get(i).getBillNo());
        //imageView=itemView.findViewById(R.id.row1btn);imageView.setOnClickListener(this);
        holder.tvEntryType.setText(mlist.get(i).getEntryType());
        holder.tvBillAmount.setText(mlist.get(i).getBillAmount());
        holder.tvReciced.setText(mlist.get(i).getReceived());
        holder.tvbalnace.setText(mlist.get(i).getBillBalance());
        holder.tvBillStatus.setText(mlist.get(i).getBillStatus());
        holder.tvRemarks.setText(mlist.get(i).getRemarks());
        holder.tvBillTime.setText(mlist.get(i).getSPDate());
        holder.tvUser.setText(mlist.get(i).getUser());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString("TableName",mlist.get(i).getRemarks());
                bundle.putString("BillAmount",mlist.get(i).getBillAmount());
                bundle.putString("SalePur1ID",mlist.get(i).getBillNo());
                bundle.putString("TableSatus",mlist.get(i).getBillStatus());
                bundle.putString("PortaionName","CashCollection");
                bundle.putString("ClientID",mlist.get(i).getClientID());
                bundle.putString("TableID","tableID");
                MenuClickActivity activity=(MenuClickActivity) context;
                FragmentManager fm=activity.getSupportFragmentManager();
                DialogResturentOption dialogResturentOption=new DialogResturentOption();
                dialogResturentOption.setArguments(bundle);
                dialogResturentOption.show(fm,"TAG");
            }
        });




        //holder.dataclick=mlist.get(i);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class mRecycleView extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvBillNo,tvbalnace,tvBillAmount,tvBillTime,tvUser,tvEntryType,tvReciced,tvRemarks,tvBillStatus;
        ImageView imageView;
        LinearLayout layout;
        joinQueryCashCollection dataclick;

        public mRecycleView(@NonNull View itemView) {
            super(itemView);
            tvBillNo=itemView.findViewById(R.id.row1BillNo);
            imageView=itemView.findViewById(R.id.row1btn);imageView.setOnClickListener(this);
            tvbalnace=itemView.findViewById(R.id.row2Balance);
            tvBillAmount=itemView.findViewById(R.id.row3BillAmount);
            tvBillTime=itemView.findViewById(R.id.row4Timing);
            tvUser=itemView.findViewById(R.id.row5User);
            tvEntryType=itemView.findViewById(R.id.row6EntryType);
            tvBillStatus=itemView.findViewById(R.id.row7BillStatus);
            tvRemarks=itemView.findViewById(R.id.row8Remarks);
            tvReciced=itemView.findViewById(R.id.row9Recived);
            layout=itemView.findViewById(R.id.mainLayout);

        }

        @Override
        public void onClick(View v) {

            PopupMenu popupMenu=new PopupMenu(context,v);
            popupMenu.inflate(R.menu.menu_cashcollection);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Bundle bundle=new Bundle();

                    MenuClickActivity activity=(MenuClickActivity)context;
                    FragmentManager fm=activity.getSupportFragmentManager();
                    cashCollectionDialog dialog=new cashCollectionDialog();
                    dialog.show(fm,"TAG");
                    return false;
                }
            });

        }
    }
}
