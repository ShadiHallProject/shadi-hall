package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.model.JoinQueryAddVehicle;

import java.util.List;

public class AddVehicleAdapter extends RecyclerView.Adapter<AddVehicleAdapter.mRecycleView> {

    private Context mContext;
    private List<JoinQueryAddVehicle> datalist;

    public AddVehicleAdapter(Context mContext, List<JoinQueryAddVehicle> datalist) {
        this.mContext = mContext;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public mRecycleView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view= inflater.inflate(R.layout.addvehiclegridview_row,viewGroup,false);
        return new mRecycleView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mRecycleView mRecycleView, int i) {

        mRecycleView.col1.setText(datalist.get(i).getVehicleID());
        mRecycleView.col2.setText(datalist.get(i).getVehicleName());
        mRecycleView.col3.setText(datalist.get(i).getRegistrationNo());
        mRecycleView.col4.setText(datalist.get(i).getStatus());


        mRecycleView.dataClick=datalist.get(i);

        if(i%2==0)
            mRecycleView.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.common_google_signin_btn_text_light_disabled));

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class mRecycleView extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView col1,col2,col3,col4;
        ImageView imageView;
        JoinQueryAddVehicle dataClick;

        public mRecycleView(@NonNull View itemView) {
            super(itemView);
            col1=itemView.findViewById(R.id.col1rowVehicleID);
            col2=itemView.findViewById(R.id.col2rowVehicleName);
            col3=itemView.findViewById(R.id.col3rowRegistrationNo);
            col4=itemView.findViewById(R.id.col4rowStatus);

            imageView=itemView.findViewById(R.id.col7rowImage);
            imageView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            PopupMenu popupMenu=new PopupMenu(mContext,v);
            popupMenu.inflate(R.menu.additemcategorymenu);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    MenuClickActivity activity=(MenuClickActivity)mContext;
                    activity.showVehicleDialog(dataClick.getVehicleID(),dataClick.getVehicleName(),dataClick.getColour(),dataClick.getModel(),dataClick.getRegistrationNo(),dataClick.getContactNo(),dataClick.getBrand(),dataClick.getVehicleGroupID(),dataClick.getDriverName());


                    return false;
                }
            });

        }
    }
}
