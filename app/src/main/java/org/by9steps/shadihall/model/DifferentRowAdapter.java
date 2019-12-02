package org.by9steps.shadihall.model;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.ResturentAddItemActivity;
import org.by9steps.shadihall.helper.MNotificationClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.by9steps.shadihall.model.ResturentSectionModel3.HEADER_EVENT_TYPE;
import static org.by9steps.shadihall.model.ResturentSectionModel3.HEADER_TYPE;

public class DifferentRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ResturentSectionModel3> mlist;
    private String billAmount;
    private onClickBillAmountInterface mOnClickBillAmount;
    private ArrayList<ResturentSectionModel3> arraylist;


    public DifferentRowAdapter(Context context, List<ResturentSectionModel3> mlist, String billAmount ) {
        this.context = context;
        this.mlist = mlist;
        this.billAmount=billAmount;
        mOnClickBillAmount=(onClickBillAmountInterface)context;

        this.arraylist = new ArrayList<ResturentSectionModel3>();
        this.arraylist.addAll(mlist);
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
                return new HeaderViewHolder(view);
            case HEADER_EVENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_row_layout_addorder, parent, false);
                return new EventViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {

        final ResturentSectionModel3 object = mlist.get(i);
        final int index=i;
        if (object != null) {
            switch (object.getType()) {
                case HEADER_TYPE:
                    ((HeaderViewHolder) holder).mTitle.setText(object.getSectionLabel());
                    break;
                case HEADER_EVENT_TYPE:
                    final MediaPlayer sound = MediaPlayer.create(context, R.raw.selectionclick);

                    ((EventViewHolder) holder).itemName.setText(object.getItemArrayListJoinQuery().getItemName());

                    ((EventViewHolder) holder).itemPrice.setText(object.getItemArrayListJoinQuery().getSalePrice());
                    ((EventViewHolder) holder).itemCount.setText(""+object.getQuantity());

                    if(object.getItemArrayListJoinQuery().getIsselected())
                        ((EventViewHolder)holder).linearLayoutItemHold.setBackgroundColor(Color.parseColor("#A4D1A2"));
                    else
                        ((EventViewHolder)holder).linearLayoutItemHold.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    //layoutclick
                    ((EventViewHolder)holder).linearLayoutItemHold.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sound.start();

                            String count=((EventViewHolder) holder).itemCount.getText().toString();
                            int quantity=new Integer(count);
                            quantity ++;
                            Log.d("aa", "onClick: "+count);
                            object.setQuantity(quantity);


                            ((EventViewHolder)holder).linearLayoutItemHold.setBackgroundColor(Color.parseColor("#A4D1A2"));

                            object.getItemArrayListJoinQuery().setIsselected(true);
                            mOnClickBillAmount.updateBillAmount(mlist);

                            ((EventViewHolder) holder).itemCount.setText(""+object.getQuantity());

                        }
                    });
                    ((EventViewHolder)holder).itemCount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            sound.start();

                            String count=((EventViewHolder) holder).itemCount.getText().toString();
                            int quantity=new Integer(count);
                            quantity ++;
                            Log.d("aa", "onClick: "+count);
                            object.setQuantity(quantity);


                            ((EventViewHolder)holder).linearLayoutItemHold.setBackgroundColor(Color.parseColor("#A4D1A2"));

                            object.getItemArrayListJoinQuery().setIsselected(true);
                            mOnClickBillAmount.updateBillAmount(mlist);

                            ((EventViewHolder) holder).itemCount.setText(""+object.getQuantity());

                        }
                    });



                    ((EventViewHolder)holder).itemCount.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            ResturentAddItemActivity activity = (ResturentAddItemActivity) context;
                            activity.showQuantityDialog(object.getItemArrayListJoinQuery().getItemName(),object.getItemArrayListJoinQuery().getSalePrice(),object.getQuantity(),index);
                            return true;
                        }
                    });
                    break;
            }
        }


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mlist != null) {
            ResturentSectionModel3 object = mlist.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }



    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder {


        private TextView itemName,itemPrice;
        private TextView itemCount;
        private LinearLayout linearLayoutItemHold;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.item_label_ItemName);
            itemPrice=itemView.findViewById(R.id.item_label_ItemPrice);
            itemCount=itemView.findViewById(R.id.item_label_itemCount);
            linearLayoutItemHold=itemView.findViewById(R.id.item_label_itemHoldOrderView);
        }
    }


    public List<ResturentSectionModel3> myfun(){

        return mlist;
    }

    public interface onClickBillAmountInterface{
        void updateBillAmount(List<ResturentSectionModel3> list);
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mlist.clear();
        if (charText.length() == 0) {
            mlist.addAll(arraylist);
        } else {
            for (ResturentSectionModel3 wp : arraylist) {
                if (wp.getItemArrayListJoinQuery().getItemName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    //getting value from dialog (DialogResturentGetQuantity/ResturentAddItemActivity)

    public void getdatefromDialogQuantity(int index, String Quantity, String Price) {

        MNotificationClass.ShowToastTem(context,"DifferentRowAdapter");

       int qty=Integer.parseInt(Quantity);
        if(qty>0) {
            mlist.get(index).getItemArrayListJoinQuery().setIsselected(true);
            mlist.get(index).setQuantity(qty);
            mlist.get(index).getItemArrayListJoinQuery().setSalePrice(Price);



            mOnClickBillAmount.updateBillAmount(mlist);

            notifyDataSetChanged();

           // MNotificationClass.ShowToastTem(context,"DifferentRowAdapter  i " +index+" qq "+Quantity+"  pp "+Price);


        }


    }
    public boolean isHeader(int position) {

        if(getItemViewType(position)==1)
            return true;
        else
            return false;

    }
}
