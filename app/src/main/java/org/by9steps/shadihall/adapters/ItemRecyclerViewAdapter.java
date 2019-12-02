package org.by9steps.shadihall.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.activities.ResturentAddItemActivity;
import org.by9steps.shadihall.chartofaccountdialog.DialogResturentOption;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.model.ResturentSectionModel1;
import org.by9steps.shadihall.model.joinQueryForResturent;

import java.util.ArrayList;
import java.util.Locale;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

    public static final String TABLE_STATUS_EMPTY="Empty";
    public static final String TABLE_STATUS_BOOKED="Booked";
    public static final String TABLE_STATUS_CLEAR="Clear";
    public static final String TABLE_STATUS_RUNNING="Running";

    private Context context;
    private ArrayList<ResturentSectionModel1> sectionModelArrayList;
  //  private ArrayList<ResturentSectionModel1> arraylistSearchView;

    private ArrayList<joinQueryForResturent> arrayList;
    private ArrayList<joinQueryForResturent> listSerchview;

    public ItemRecyclerViewAdapter(Context context, ArrayList<joinQueryForResturent> arrayList, ArrayList<ResturentSectionModel1> sectionModelArrayList) {
        this.sectionModelArrayList=sectionModelArrayList;
        this.context = context;
        this.arrayList = arrayList;


//        this.arraylistSearchView = new ArrayList<ResturentSectionModel1>();
//        this.arraylistSearchView.addAll(sectionModelArrayList);

        this.listSerchview=new ArrayList<joinQueryForResturent>();
        this.listSerchview.addAll(arrayList);

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_custom_row_layout,parent,false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder( @NonNull final ItemViewHolder holder, final int i ) {
        holder.itemLabelTable.setText(arrayList.get(i).getTableName());
        holder.itemLabelPrice.setText(arrayList.get(i).getBillAmount());

        if(arrayList.get(i).getTableStatus()!=null && arrayList.get(i).getTableStatus().equals(TABLE_STATUS_BOOKED))
        {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#FF0000"));//red
            holder.itemLabelTable.setTextColor(Color.WHITE);
            holder.itemLabelPrice.setTextColor(Color.WHITE);
        }
        else if(arrayList.get(i).getTableStatus()!=null && arrayList.get(i).getTableStatus().equals(TABLE_STATUS_EMPTY))
        {
            //no check is needed
           // holder.itemLabelPrice.setText(arrayList.get(i).getTableStatus());
        }
        else if (arrayList.get(i).getTableStatus()!=null && arrayList.get(i).getTableStatus().equals(TABLE_STATUS_CLEAR))
        {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#A4D1A2"));//lightgreen
        }
        else if(arrayList.get(i).getTableStatus()!=null && arrayList.get(i).getTableStatus().equals(TABLE_STATUS_RUNNING))
        {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#1A993C"));//green
            holder.itemLabelTable.setTextColor(Color.WHITE);
            holder.itemLabelPrice.setTextColor(Color.WHITE);
        }

        final MediaPlayer sound = MediaPlayer.create(context, R.raw.click);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   MNotificationClass.ShowToastTem(context,"Status  "+arrayList.get(i).getTableStatus());
                if(arrayList.get(i).getTableStatus()==null)
                    arrayList.get(i).setTableStatus("Empty");
                if( arrayList.get(i).getTableStatus().equals("Running") ){
                    sound.start();
                    Bundle bundle=new Bundle();
                    bundle.putString("TableName",arrayList.get(i).getTableName());
                    bundle.putString("BillAmount",arrayList.get(i).getBillAmount());
                    bundle.putString("SalePur1ID",arrayList.get(i).getSalPur1ID());
                    bundle.putString("TableSatus",arrayList.get(i).getTableStatus());
                    bundle.putString("PortaionName",arrayList.get(i).getPotionName());
                    bundle.putString("ClientID",arrayList.get(i).getClientID());
                    bundle.putString("TableID",arrayList.get(i).getTableID());
                    MenuClickActivity activity=(MenuClickActivity) context;
                    FragmentManager fm=activity.getSupportFragmentManager();
                    DialogResturentOption dialogResturentOption=new DialogResturentOption();
                    dialogResturentOption.setArguments(bundle);
                    dialogResturentOption.show(fm,"TAG");
                }else {
                    sound.start();
                    Bundle bundle=new Bundle();
                    bundle.putString("TableName",arrayList.get(i).getTableName());
                    bundle.putString("BillAmount",arrayList.get(i).getBillAmount());
                    bundle.putString("SalePur1ID",arrayList.get(i).getSalPur1ID());
                    bundle.putString("TableSatus",arrayList.get(i).getTableStatus());
                    bundle.putString("PortaionName",arrayList.get(i).getPotionName());
                    bundle.putString("ClientID",arrayList.get(i).getClientID());
                    bundle.putString("TableID",arrayList.get(i).getTableID());

                    Context context = v.getContext();
                    Intent intent = new Intent(context, ResturentAddItemActivity.class);

                    intent.putExtras(bundle);
                    ((Activity) context).startActivityForResult(intent,1);


                }



            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView itemLabelPrice,itemLabelTable;
        private LinearLayout linearLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemLabelTable=(TextView)itemView.findViewById(R.id.item_label_table);
            itemLabelPrice = (TextView) itemView.findViewById(R.id.item_label_price);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.myTableLayout);
        }
    }


    // Filter Class
    public void filter1(String s) {
        s = s.toLowerCase(Locale.getDefault());
        arrayList.clear();
        if (s.length() == 0) {
            arrayList.addAll(listSerchview);
        } else {

            for (int i = 0; i < sectionModelArrayList.size(); i++) {
                for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayListJoinQuery().size(); j++) {
                    if(sectionModelArrayList.get(i).getItemArrayListJoinQuery().get(j).getTableName().toLowerCase(Locale.getDefault()).contains(s)) {
                        arrayList.add(sectionModelArrayList.get(i).getItemArrayListJoinQuery().get(j));
                    }

                }
            }

        }
        notifyDataSetChanged();

        MNotificationClass.ShowToastTem(context,"notifydatasetchanhe2");
    }



}
