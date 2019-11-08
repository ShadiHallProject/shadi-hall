package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.helper.MNotificationClass;

public class CustomeAdapterForSpinner extends BaseAdapter {

    Context context;
    String[] Name;
    int [] arrIds;
    LayoutInflater inflter;

    public CustomeAdapterForSpinner(Context context, String[] name , int[] arrIds) {
        this.context = context;
        Name = name;
        this.arrIds=arrIds;
        inflter = (LayoutInflater.from(context));
    }




    @Override
    public int getCount() {
        return  Name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView=inflter.inflate(R.layout.spinner_custom_layout,null);
        TextView tvName=convertView.findViewById(R.id.TVSpinner);
        TextView tvEdit=convertView.findViewById(R.id.TVSPEdit);
        tvName.setText(Name[position]);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                customGroupDialog1 dialog1=new customGroupDialog1();
//                dialog1.show();
                MenuClickActivity activity=(MenuClickActivity)context;
                activity.showCustomeGroupDialog(Name[position],arrIds[position]);
                MNotificationClass.ShowToastTem(context,"Name "+Name[position]+" id "+arrIds[position]);
            }
        });
        return convertView;
    }
}
