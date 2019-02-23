package org.by9steps.shadihall.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.callback.BookFormClickListener;
import org.by9steps.shadihall.model.Booking;

import java.util.List;



public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    public static final int EDIT = 0;
    public static final int DELETE = 1;
    public Context mContext;
    public List<Booking> itemList;
    public BookFormClickListener itemClickListener;

    public BookingAdapter(Context mContext, List<Booking> itemList, Fragment itemClickListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.itemClickListener = (BookFormClickListener) itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.account_name_3_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Booking item = itemList.get(position);

        holder.ed_client_id.setText(item.getClientID());
        holder.ed_net_code.setText(item.getNetCode());
        holder.ed_sys_code.setText(item.getSysCode());
        holder.ed_updated_date.setText(item.getUpdateDate());


        holder.ed_AcName.setText(item.getClientName());
        holder.ed_AcAddress.setText(item.getClientAddress());
        holder.ed_AcMobileNo.setText(item.getClientMobile());
        holder.ed_AcEmailAddress.setText(item.getClientCNIC());
        holder.ed_Salary.setText(item.getCharges());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView ed_client_id, ed_net_code, ed_sys_code, ed_updated_date, ed_group_id, ed_UserRights;
        public TextView ed_AcName, ed_AcAddress, ed_AcMobileNo, ed_AcEmailAddress, ed_Salary, ed_AcContactNo, ed_AcPassword;
        public ImageView btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);


            ed_client_id = (TextView) itemView.findViewById(R.id.ed_client_id1);
            ed_net_code = (TextView) itemView.findViewById(R.id.ed_net_code1);
            ed_sys_code = (TextView) itemView.findViewById(R.id.ed_sys_code1);
            ed_updated_date = (TextView) itemView.findViewById(R.id.ed_updated_date1);

            ed_AcName = (TextView) itemView.findViewById(R.id.ed_AcName1);
            ed_AcAddress = (TextView) itemView.findViewById(R.id.ed_AcAddress1);
            ed_AcMobileNo = (TextView) itemView.findViewById(R.id.ed_AcMobileNo1);
            ed_AcEmailAddress = (TextView) itemView.findViewById(R.id.ed_AcEmailAddress1);
            ed_Salary = (TextView) itemView.findViewById(R.id.ed_Salary1);

            btnEdit = (ImageView) itemView.findViewById(R.id.btnEdit);
            btnDelete = (ImageView) itemView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view == btnEdit) {
                if (itemClickListener != null) {
                    int id = itemList.get(getAdapterPosition()).id;


                    itemClickListener.onClickBooking(id, EDIT,

                            itemList.get(getAdapterPosition()).getClientID(),
                            itemList.get(getAdapterPosition()).getNetCode(),
                            itemList.get(getAdapterPosition()).getSysCode(),
                            itemList.get(getAdapterPosition()).getUpdateDate(),

                            itemList.get(getAdapterPosition()).getClientName(),
                            itemList.get(getAdapterPosition()).getClientAddress(),
                            itemList.get(getAdapterPosition()).getClientMobile(),
                            itemList.get(getAdapterPosition()).getClientCNIC(),
                            itemList.get(getAdapterPosition()).getCharges()

                    );


                }
            } else if (view == btnDelete) {
                if (itemClickListener != null) {
                    int id = itemList.get(getAdapterPosition()).id;
                    itemClickListener.onClickBooking(id, DELETE, null, null,
                            null, null, null, null,
                            null,
                            null, null
                    );
                }
            }
        }
    }
}
