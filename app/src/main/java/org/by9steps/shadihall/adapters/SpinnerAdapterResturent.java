package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.chartofaccountdialog.ResturentDialogAddPortation;
import org.by9steps.shadihall.chartofaccountdialog.ResturentDialogAddTable;

public class SpinnerAdapterResturent extends BaseAdapter {

    Context context;
    String[] Name;
    int [] arrIds;
    LayoutInflater inflter;

    public SpinnerAdapterResturent(Context context, String[] name, int[] arrIds) {
        this.context = context;
        Name = name;
        this.arrIds = arrIds;
        inflter=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Name.length;
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
        convertView=inflter.inflate(R.layout.spinner_layout_resturent,null);
        TextView textViewName=convertView.findViewById(R.id.section_label_itemName);
        TextView textViewEdit=convertView.findViewById(R.id.section_label_Edit);
        textViewName.setText(Name[position]);
        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MenuClickActivity activity=(MenuClickActivity)context;
                Bundle bundle=new Bundle();
                bundle.putString("type","Edit");
                bundle.putInt("key",arrIds[position]);
                ResturentDialogAddPortation obj=new ResturentDialogAddPortation();
                obj.setArguments(bundle);
                obj.show(activity.getSupportFragmentManager(),"TAG");

                MenuClickActivity activity1=(MenuClickActivity)context;
                FragmentManager manager = activity1.getSupportFragmentManager();
                Fragment prev = manager.findFragmentByTag("TABLE");
                if (prev != null) {
                    manager.beginTransaction().remove(prev).commit();
                }



            }
        });

        return convertView;
    }
}
